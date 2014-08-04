package org.tort.trade.service

import org.tort.trade.mobile.NoCGLibTransition
import com.tort.trade.model.Schema
import scala.slick.driver.H2Driver
import scala.slick.jdbc.JdbcBackend.DatabaseDef
import scala.slick.jdbc.JdbcBackend.Database.dynamicSession

class Service(db: DatabaseDef) {
  val schema = new Schema(H2Driver)

  def addTransition(transition: NoCGLibTransition) {
    db.withDynSession {
      schema.insertTransition(transition)
    }
  }
}
