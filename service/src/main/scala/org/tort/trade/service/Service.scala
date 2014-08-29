package org.tort.trade.service

import org.tort.trade.mobile.{NoCGLibGood, NoCGLibTransition}
import com.tort.trade.model.Schema
import scala.slick.driver.H2Driver
import scala.slick.jdbc.JdbcBackend.DatabaseDef
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession
import scala.util.matching.Regex

class Service(db: DatabaseDef) {
  val schema = new Schema(H2Driver)

  def addTransition(transition: NoCGLibTransition) {
    db.withDynSession {
      schema.insertTransition(transition)
    }
  }

  def goodsBy(substrings: Seq[String]) = {
    db.withDynSession {
      schema.goodsBy(substrings)
    }
  }

  def matchJournals = {
    db.withDynSession {
      schema.matchJournals
    }
  }

  def balance(journalId: Long): Map[NoCGLibGood, Long] = {
    db.withDynSession {
      schema.balance(journalId).map(x => x._1 -> x._2.getOrElse(0L))
    }
  }
}
