package org.tort.trade.mobile

import NoCGLibTransition._
import java.util.Date
import scalaz._
import Scalaz._
import org.tort.trade.mobile.NoCGLibSale.SaleId
import com.tort.trade.model.UUIDGenerator
import java.sql.Timestamp

class NoCGLibTransition(val id: String @@ NoCGLibTransition.Id,
                        val from: String @@ SaleId,
                        val to: String @@ SaleId,
                        val quant: Long @@ Quantity,
                        val date: Date,
                        val me: String @@ SaleId,
                        val good: String @@ NoCGLibGood.Id,
                        val sellPrice: Option[Long @@ Price])

object NoCGLibTransition {
  trait Id
  trait Quantity
  trait Price

  def id(aI: String): String @@ Id = Tag(aI)
  def quantity(q: Long): Long @@ Quantity = Tag(q)
  def price(p: Option[Long]): Option[Long @@ Price] = p.map(Tag(_))

  def apply(from: String @@ SaleId,
            to: String @@ SaleId,
            quant: Long @@ Quantity,
            price: Option[Long @@ Price],
            date: Date,
            me: String @@ SaleId,
            good: String @@ NoCGLibGood.Id): NoCGLibTransition = new NoCGLibTransition(
    id = UUIDGenerator.generate.toString |> id,
    from = from,
    to = to,
    quant = quant,
    date = date,
    me = me,
    good = good,
    sellPrice = price
  )

  def tupled(transition: (String, String, String, Long, Date, String, String, Option[Long])) = new NoCGLibTransition(
    id(transition._1),
    NoCGLibSale.saleId(transition._2),
    NoCGLibSale.saleId(transition._3),
    quantity(transition._4),
    transition._5,
    NoCGLibSale.saleId(transition._6),
    NoCGLibGood.id(transition._7),
    price(transition._8)
  )
  
  def unapply(transition: NoCGLibTransition): Option[Tuple8[String, String, String, Long, Timestamp, String, String, Option[Long]]] = Some((
    transition.id,
    transition.from,
    transition.to,
    transition.quant,
    new Timestamp(transition.date.getTime),
    transition.me,
    transition.good,
    transition.sellPrice
  ))
}
