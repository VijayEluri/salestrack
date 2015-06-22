package org.tort.trade.service

import java.util.{Date, Properties}

import org.joda.time.DateTime
import org.tort.trade.mobile.{NoCGLibGood, NoCGLibSale, NoCGLibTransition}

import scala.slick.jdbc.JdbcBackend
import scala.slick.jdbc.JdbcBackend.Database
import scalaz.Scalaz._

object Runner extends DBHelper {

  val provider = NoCGLibSale.saleId("1")
  val natasha = NoCGLibSale.saleId("5")

  def main(args: Array[String]) {
    initDB(parseAndExecute(args))
  }

  def parseAndExecute(args: Array[String])(db: JdbcBackend.DatabaseDef) {
    args.toList match {
      case "trans" :: Nil => db |> service |> addTransition
      case "goods" :: xs => db |> service |> goodsSearch(xs)
      case "check" :: Nil =>
        service(db).matchJournals.foreach(t => s"${t.date}\t${t.from}\t${t.to}\t${t.good}\t${t.quant}" |> println)
      case "balance" :: journalId :: Nil =>
        service(db)
          .balance(journalId.toLong, Nil)
          .foreach(x => s"${x._1.name}\t${x._2}" |> println)
      case "overall" :: month :: Nil =>
        service(db)
          .overall(parsePeriod(month.toInt))
          .foreach(x => s"${x._1}\t${x._2}" |> println)
      case _ => println(
        """Usage:
          |goods substring - поиск товаров по названию. example: goods беж
          |check - сравнение журналов на сегодня. показывает передачи, не подтверждающиеся другими журналами
          |balance - остатки по журналу на сегодня. example: balance 8
          |overall - оборот за месяц. месяц относительно текущего. example: overall -1   - оборот за прошлый месяц""".stripMargin)
    }
  }

  def service(db: Database) = {
    new Service(db)
  }

  override def database: (Config) => JdbcBackend.DatabaseDef = {
    case Config(ip, url) =>
      Database.forURL(s"jdbc:h2:tcp://$ip/$url;IFEXISTS=TRUE", "sa", "", new Properties(), "org.h2.Driver")
  }

  private def parsePeriod(month: Int): (DateTime, DateTime) = {
    if (month > 0)
      (new DateTime(), new DateTime().plusMonths(month))
    else
      (new DateTime().plusMonths(month), new DateTime())
  }

  def addTransition(service: Service) {
    NoCGLibTransition(
      provider,
      natasha,
      NoCGLibTransition.quantity(1),
      NoCGLibTransition.price(Some(250)),
      new Date(),
      natasha,
      NoCGLibGood.id("232")
    ) |> service.addTransition
  }

  private def goodsSearch(xs: List[String])(service: Service) {
    xs match {
      case Nil =>
        println("usage: goods regex")
      case xs =>
        service.goodsBy(xs).foreach(g => println(s"${g.id}\t${g.name}"))
    }
  }
}

case class Config(ip: String, url: String)
