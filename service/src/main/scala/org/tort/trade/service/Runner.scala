package org.tort.trade.service

import java.io.File
import java.util.{Date, Properties}

import org.joda.time.DateTime
import org.tort.trade.mobile.{NoCGLibGood, NoCGLibSale, NoCGLibTransition}

import scala.io.Source
import scala.slick.jdbc.JdbcBackend
import scala.slick.jdbc.JdbcBackend.Database
import scalaz.Scalaz._
import scalaz.{ValidationNel, Failure, Success, Validation}

object Runner {

  val provider = NoCGLibSale.saleId("1")
  val natasha = NoCGLibSale.saleId("5")

  def main(args: Array[String]) {
    readConfig map database match {
      case Failure(msgs) =>
        msgs foreach println
      case Success(db) =>
        parseAndExecute(args, db)
    }
  }

  def parseAndExecute(args: Array[String], db: JdbcBackend.DatabaseDef) {
    args.toList match {
      case Nil => println("usage trans")
      case "trans" :: Nil => db |> service |> addTransition
      case "goods" :: xs => db |> service |> goodsSearch(xs)
      case "check" :: Nil =>
        service(db).matchJournals.foreach(t => s"${t.date}\t${t.from}\t${t.to}\t${t.good}\t${t.quant}" |> println)
      case "balance" :: journalId :: Nil =>
        service(db)
          .balance(journalId.toLong)
          .foreach(x => s"${x._1.name}\t${x._2}" |> println)
      case "overall" :: month :: Nil =>
        service(db)
          .overall(parsePeriod(month.toInt))
          .foreach(x => s"${x._1}\t${x._2}" |> println)
    }
  }

  def homeDir = new File(System.getProperty("user.home"))

  def cfgFile = new File(homeDir, "trade.cfg")

  def service(db: Database) = {
    new Service(db)
  }

  def database: (Config) => JdbcBackend.DatabaseDef = {
    case Config(ip, url) =>
      Database.forURL(s"jdbc:h2:tcp://$ip/$url;IFEXISTS=TRUE", "sa", "", new Properties(), "org.h2.Driver")
  }

  def readConfig: ValidationNel[String, Config] = {
    val lines = Source.fromFile(cfgFile).getLines().toList
    def url = lines.find(line => line.startsWith("url=")).map(_.split('=')(1)).toSuccess("IP not found in config").toValidationNel
    def ip = lines.find(line => line.startsWith("ip=")).map(_.split('=')(1)).toSuccess("URL not found in config").toValidationNel
    (ip |@| url) {Config(_, _)}
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
