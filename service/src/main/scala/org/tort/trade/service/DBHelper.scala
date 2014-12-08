package org.tort.trade.service

import java.util.Properties
import scala.slick.jdbc.JdbcBackend
import scala.slick.jdbc.JdbcBackend.Database
import scalaz.{Success, Failure}

trait DBHelper extends DBConfigReader {
  def database: (Config) => JdbcBackend.DatabaseDef = {
    case Config(ip, url) =>
      Database.forURL(s"jdbc:h2:tcp://$ip/$url;IFEXISTS=TRUE", "sa", "", new Properties(), "org.h2.Driver")
  }

  def initDB(action: JdbcBackend.DatabaseDef => Unit) {
    readConfig map database match {
      case Failure(msgs) => msgs foreach println
      case Success(db) => action(db)
    }
  }
}
