package com.tort.trade.model

import java.util.Date
import scalaz._
import Scalaz._
import org.squeryl.Schema
import org.squeryl.annotations.Column

object SalestrackSchema extends Schema {

  trait SalesIdTag

  trait GoodIdTag

  trait SalesNameTag

  trait SalesAliasIdTag

  trait GoodNameTag

  trait TransitionIdTag

  trait QuantityTag

  type SalesId = String @@ SalesIdTag
  type GoodId = String @@ GoodIdTag
  type SalesName = String @@ SalesNameTag
  type SalesAliasId = String @@ SalesAliasIdTag
  type GoodName = String @@ GoodNameTag
  type TransitionId = String @@ TransitionIdTag
  type Quantity = Long @@ QuantityTag

  def salesId(id: String) = Tag[String, SalesIdTag](id)

  def goodId(id: String) = Tag[String, GoodIdTag](id)

  def salesName(name: String) = Tag[String, SalesNameTag](name)

  def salesAliasId(id: String) = Tag[String, SalesAliasIdTag](id)

  def goodName(name: String) = Tag[String, GoodNameTag](name)

  def transitionId(id: String) = Tag[String, TransitionIdTag](id)

  val transition = table[Transition]("TRADE_SRC")
  val sales = table[Sales]("DEP")
  val good = table[Good]("MAT")
}

import SalestrackSchema._

class Transition(
                  @Column("TRD_SEQ") val id: TransitionId,
                  @Column("TRD_FROM") val from: SalesId,
                  @Column("TRD_TO") val to: SalesId,
                  @Column("TRD_QUANT") val quant: Quantity,
                  @Column("TRD_DATE") val date: Date,
                  @Column("TRD_JREF") val me: SalesId,
                  @Column("TRD_MAT") val good: GoodId,
                  @Column("TRD_PRICE") val sellPrice: BigDecimal = BigDecimal(0),
                  @Column("TRD_BUYPRICE") val buyPrice: BigDecimal = BigDecimal(0)
                  )

object Transition {
  def apply(from: SalesId,
            to: SalesId,
            quant: Quantity,
            date: Date,
            me: SalesId,
            good: GoodId,
            sellPrice: BigDecimal,
            buyPrice: BigDecimal) = new Transition(id, from, to, quant, date, me, good, sellPrice, buyPrice)

  def id = UUIDGenerator.generate.toString |> transitionId
}

class Sales(
             @Column(name = "DEP_SEQ") val id: SalesId,
             @Column(name = "DEP_NAME") val name: SalesName,
             val alias: SalesAliasId
             )

class SalesAlias(val id: SalesAliasId, val sales: SalesId)

class Good(
            @Column(name = "SEQ_M") val id: GoodId,
            @Column(name = "NAME") val name: GoodName
            )
