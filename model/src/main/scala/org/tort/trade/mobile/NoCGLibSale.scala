package org.tort.trade.mobile

import scalaz._
import Scalaz._
import org.tort.trade.mobile.NoCGLibSale.{SaleName, SaleId}

class NoCGLibSale(val id: String @@ SaleId, val name: String @@ SaleName) extends Serializable {
  override def equals(o: scala.Any) = o match {
    case s: NoCGLibSale => this === s
    case _ => false
  }

  override def hashCode() = id.hashCode
}

object NoCGLibSale {
  trait SaleId
  trait SaleName

  val CustomerSaleId = saleId("2")

  def saleId(id: String): String @@ SaleId = Tag(id)
  def saleName(name: String): String @@ SaleName = Tag(name)

  implicit def salesEquality: Equal[NoCGLibSale] = Equal.equal(_.id === _.id)

  implicit def saleIdEquality: Equal[String @@ SaleId] = Equal.equal(_ == _)

  def apply(id: String @@ SaleId, name: String @@ SaleName) = new NoCGLibSale(id, name)

  def unapply(sale: NoCGLibSale): Option[Tuple2[String, String]] = Some((sale.id, sale.name))

  def tupled(tuple: (String, String)) = new NoCGLibSale(saleId(tuple._1), saleName(tuple._2))
}
