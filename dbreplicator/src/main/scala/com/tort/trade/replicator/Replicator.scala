package com.tort.trade.replicator

import org.squeryl.{Table, Session}
import org.squeryl.adapters.H2Adapter
import org.squeryl.PrimitiveTypeMode._
import com.tort.trade.model.SalestrackSchema._
import com.tort.trade.model.Transition
import scalaz._
import Scalaz._

class Replicator(val ip: String, sid: String) {
  Class.forName("org.h2.Driver")

  createSchema
  replicate(good)
  replicate(sales)
  replicate(transition, transform)

  private def transform: (Seq[Transition]) => Seq[Transition] = {
    (x) => x.map(transition =>
      new Transition(
        transition.id,
        transition.from,
        transition.to,
        transition.quant,
        transition.date,
        transition.me,
        transition.good,
        transition.sellPrice)
    )
  }

  private def h2Session = {
    val connection = java.sql.DriverManager.getConnection(
      "jdbc:h2:trade;AUTO_SERVER=TRUE",
      "sa",
      ""
    )
    connection.setAutoCommit(false)
    Session.create(connection, new H2Adapter)
  }

  private def oracleSession = {
    val connection = java.sql.DriverManager.getConnection(
      "jdbc:oracle:thin:@%s:1521:%s".format(ip, sid),
      "torhriph",
      "nfufymqjhr"
    )
    connection.setAutoCommit(false)
    Session.create(connection, new H2Adapter)
  }

  private def replicate[T](table: Table[T], transform: Seq[T] => Seq[T] = (x: Seq[T]) => x) = {
    println("copying " + table.name)

    transaction(oracleSession) {
      val entities = from(table)((e) => select(e)).toSeq
      println("finished reading")

      transaction(h2Session) {
        val t: Seq[T] = entities |> transform
        table.insert(t)
      }
      println("finished inserting")
    }
  }

  private def createSchema = transaction(h2Session) {
    drop
    println("schema droppped")
    create
    println("schema created")
  }
}

object Runner {
  def main(args: Array[String]) {
    if (args.length != 2)
      println("Params: IP SID")
    else
      new Replicator(args(0), args(1))
  }
}

