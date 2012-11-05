package com.tort.trade.replicator

import org.squeryl.{Table, Session}
import org.squeryl.adapters.H2Adapter
import org.squeryl.PrimitiveTypeMode._
import com.tort.trade.model.SalestrackSchema._
import com.tort.trade.model.Transition
import scalaz._
import Scalaz._

object ReplicatorRun extends App with Replicator {
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
}

trait Replicator {
  Class.forName("org.h2.Driver")

  def h2Session = {
    val connection = java.sql.DriverManager.getConnection(
      "jdbc:h2:trade;AUTO_SERVER=TRUE",
      "sa",
      ""
    )
    connection.setAutoCommit(false)
    Session.create(connection, new H2Adapter)
  }

  Class.forName("oracle.jdbc.driver.OracleDriver")

  def oracleSession = {
    val connection = java.sql.DriverManager.getConnection(
      "jdbc:oracle:thin:@192.168.1.218:1521:trade",
      "torhriph",
      "nfufymqjhr"
    )
    connection.setAutoCommit(false)
    Session.create(connection, new H2Adapter)
  }

  def replicate[T](table: Table[T], transform: Seq[T] => Seq[T] = (x: Seq[T]) => x) = {
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

  def createSchema = transaction(h2Session) {
    drop
    println("schema droppped")
    create
    println("schema created")
  }
}
