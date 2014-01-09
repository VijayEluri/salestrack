package org.tort.trade.mobile

import NoCGLibTransition._
import java.util.Date
import scalaz._
import Scalaz._

class NoCGLibTransition(val id: String @@ NoCGLibTransition.Id,
                        val from: String @@ SaleId,
                        val to: String @@ SaleId,
                        val quant: Int @@ Quantity,
                        val date: Date,
                        val me: String @@ SaleId,
                        val good: String @@ NoCGLibGood.Id,
                        val sellPrice: Option[Int @@ Price])

object NoCGLibTransition {
  trait Id
  trait Quantity
  trait SaleId
  trait Price

  def id(aI: String): String @@ Id = Tag(aI)
  def saleId(id: String): String @@ SaleId = Tag(id)
  def quantity(q: Int): Int @@ Quantity = Tag(q)
  def price(p: Int): Int @@ Price = Tag(p)

  def apply(from: String @@ SaleId,
            to: String @@ SaleId,
            quant: Int @@ Quantity,
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
    sellPrice = None
  )
}
