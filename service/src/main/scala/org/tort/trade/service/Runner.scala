package org.tort.trade.service

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
      case "trans" :: Nil =>
        NoCGLibTransition(
          provider,
          natasha,
          NoCGLibTransition.quantity(1),
          new Date(),
          natasha,
          NoCGLibGood.id("232")
        ) |> service.addTransition
    }
  }
}

