package org.tort.trade.service

import java.sql.Timestamp

import org.joda.time.DateTime

import scala.slick.jdbc.JdbcBackend.Database
import java.util.{Date, Properties}
import org.tort.trade.mobile.{NoCGLibGood, NoCGLibSale, NoCGLibTransition}
import scalaz._
import Scalaz._

object Runner {
  val h2 = Database.forURL("jdbc:h2:tcp://0.0.0.0/~/workspace/salestrack/trade;IFEXISTS=TRUE", "sa", "", new Properties(), "org.h2.Driver")
  val service = new Service(h2);

  val provider = NoCGLibSale.saleId("1")
  val natasha = NoCGLibSale.saleId("5")

  def main(args: Array[String]) {
    args.toList match {
      case Nil => println("usage trans")
      case "trans" :: Nil => addTransition
      case "goods" :: xs => goodsSearch(xs)
      case "check" :: Nil => service.matchJournals.foreach(t => s"${t.date}\t${t.from}\t${t.to}\t${t.good}\t${t.quant}" |> println)
      case "balance" :: journalId :: Nil =>
        service
          .balance(journalId.toLong)
          .foreach(x => s"${x._1.name}\t${x._2}" |> println)
      case "overall" :: month :: Nil =>
        service
          .overall(parsePeriod(month.toInt))
          .foreach(x => s"${x._1}\t${x._2}" |> println)
    }
  }

  private def parsePeriod(month: Int): (DateTime, DateTime) = {
    if (month > 0)
      (new DateTime(), new DateTime().plusMonths(month))
    else
      (new DateTime().plusMonths(month), new DateTime())
  }

  def addTransition {
    NoCGLibTransition(
      provider,
      natasha,
      NoCGLibTransition.quantity(1),
      new Date(),
      natasha,
      NoCGLibGood.id("232")
    ) |> service.addTransition
  }

  private def goodsSearch(xs: List[String]) {
    xs match {
      case Nil =>
        println("usage: goods regex")
      case xs =>
        service.goodsBy(xs).foreach(g => println(s"${g.id}\t${g.name}"))
    }
  }
}
