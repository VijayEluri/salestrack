package org.tort.trade.mobile

import scalaz._
import Scalaz._
import org.tort.trade.mobile.NoCGLibSale.{SaleName, SaleId}

class NoCGLibSale(val id: String @@ SaleId, val name: String @@ SaleName)

object NoCGLibSale {
  trait SaleId
  trait SaleName

  def saleId(id: String): String @@ SaleId = Tag(id)
  def saleName(name: String): String @@ SaleName = Tag(name)

  implicit def salesEquality: Equal[NoCGLibSale] = Equal.equal(_.id === _.id)

  implicit def saleIdEquality: Equal[String @@ SaleId] = Equal.equal(_ == _)

  def apply(id: String @@ SaleId, name: String @@ SaleName) = new NoCGLibSale(id, name)
}
