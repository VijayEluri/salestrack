package com.tort.trade.replicator

import scala.slick.jdbc.JdbcBackend.Database
import java.util.Properties
import com.tort.trade.model.Schema
import scala.slick.driver.H2Driver
import com.typesafe.slick.driver.oracle.OracleDriver
import scala.slick.jdbc.meta.MTable

class Replicator(val ip: String, sid: String) {
  val oracle = Database.forURL("jdbc:oracle:thin:@%s:1521/%s".format(ip, sid), "torhriph", "nfufymqjhr", new Properties(), "oracle.jdbc.OracleDriver")
  val h2 = Database.forURL("jdbc:h2:trade;AUTO_SERVER=TRUE", "sa", "", new Properties(), "org.h2.Driver")

  createSchema()
  replicate()

  private def replicate() {
    val oracleSession = oracle.createSession()
    val h2Session = h2.createSession()

    oracleSession.withTransaction {
      h2Session.withTransaction {
        val oracleSchema = new Schema(OracleDriver)
        val h2Schema = new Schema(H2Driver)

        oracleSchema.listGoods(oracleSession) foreach {
          case good => h2Schema.insertGood(good)(h2Session)
        }

        oracleSchema.listSales(oracleSession) foreach {
          case sales => h2Schema.insertSale(sales)(h2Session)
        }

        oracleSchema.listTransitions(oracleSession) foreach {
          case transition => h2Schema.insertTransition(transition)(h2Session)
        }
      }
    }
  }

  private def createSchema() = {
    val h2Schema = new Schema(H2Driver)
    import h2Schema.driver.simple._

    h2.withSession {
      implicit session: Session =>
        if(!MTable.getTables(h2Schema.sales.baseTableRow.tableName).list().isEmpty) {
          h2Schema.drop
          println("schema droppped")
        }
        h2Schema.create
        println("schema created")
    }
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

