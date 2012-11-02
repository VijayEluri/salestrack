package com.tort.trade.model

import java.util.Date
import scalaz._
import org.squeryl.Schema
import org.squeryl.annotations.Column

object SalestrackSchema extends Schema {
  trait SalesIdString
  trait GoodIdString
  trait SalesNameString
  trait SalesAliasIdString
  trait GoodNameString
  type SalesId = String @@ SalesIdString
  type GoodId = String @@ GoodIdString
  type SalesName = String @@ SalesNameString
  type SalesAliasId = String @@ SalesAliasIdString
  type GoodName = String @@ GoodNameString

  def salesId(id: String) = Tag[String, SalesIdString](id)
  def goodId(id: String) = Tag[String, GoodIdString](id)
  def salesName(name: String) = Tag[String, SalesNameString](name)
  def salesAliasId(id: String) = Tag[String, SalesAliasIdString](id)
  def goodName(name: String) = Tag[String, GoodNameString](name)

  val transition = table[Transition]("TRADE_SRC")
  val sales = table[Sales]("DEP")
  val good = table[Good]("MAT")
}

import SalestrackSchema._
class Transition(
                  @Column("TRD_SEQ") val id: Long,
                  @Column("TRD_FROM") val from: SalesId,
                  @Column("TRD_TO") val to: SalesId,
                  @Column("TRD_QUANT") val quant: Long,
                  @Column("TRD_DATE") val date: Date,
                  @Column("TRD_JREF") val me: SalesId,
                  @Column("TRD_MAT") val good: GoodId,
                  @Column("TRD_PRICE") val sellPrice: BigDecimal = BigDecimal(0),
                  @Column("TRD_BUYPRICE") val buyPrice: BigDecimal = BigDecimal(0)
                  )

class Sales(
             @Column(name = "DEP_SEQ") val id: SalesId,
             @Column(name = "DEP_NAME") val name: SalesName,
             val alias: SalesAliasId
             )

class SalesAlias (val id: SalesAliasId, val sales: SalesId)

class Good(
            @Column(name = "SEQ_M") val id: GoodId,
            @Column(name = "NAME") val name: GoodName
)
