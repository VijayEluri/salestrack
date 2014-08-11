package com.tort.trade.model

import scala.slick.driver.JdbcProfile
import java.sql.Timestamp
import org.tort.trade.mobile.{NoCGLibTransition, NoCGLibSale, NoCGLibGood}
import scalaz._
import Scalaz._

class Schema(val driver: JdbcProfile) {
  import driver.simple._

  class Transitions(tag: Tag) extends Table[NoCGLibTransition](tag, "TRADE_SRC"){
    def id = column[String]("TRD_SEQ", O.PrimaryKey)
    def from = column[String]("TRD_FROM")
    def to = column[String]("TRD_TO")
    def quant = column[Long]("TRD_QUANT")
    def date = column[Timestamp]("TRD_DATE")
    def me = column[String]("TRD_JREF")
    def good = column[String]("TRD_MAT")
    def sellPrice = column[Option[Long]]("TRD_PRICE")

    def * = (id, from, to, quant, date, me, good, sellPrice) <> (NoCGLibTransition.tupled, NoCGLibTransition.unapply)
  }

  class Sales(tag: Tag) extends Table[NoCGLibSale](tag, "DEP") {
    def id = column[String]("DEP_SEQ", O.PrimaryKey)
    def name = column[String]("DEP_NAME")

    def * = (id, name) <> (NoCGLibSale.tupled, NoCGLibSale.unapply)
  }

  class Goods(tag: Tag) extends Table[NoCGLibGood](tag, "MAT") {
    def id = column[String]("SEQ_M", O.PrimaryKey)
    def name = column[String]("NAME", O.Nullable)

    def * = (id, name) <> (NoCGLibGood.tupled, NoCGLibGood.unapply)
  }

  val transitions: TableQuery[Transitions] = TableQuery[Transitions]
  val sales = TableQuery[Sales]
  val goods: TableQuery[Goods] = TableQuery[Goods]

  private def assembleLikeExpr(base: Seq[String]) = "%" + base.reduce((acc, i) => acc + "%" + i) + "%"

  def goodsBy(substrings: Seq[String])(implicit session: Session): Seq[NoCGLibGood] = {
    val likeExpr = substrings |> assembleLikeExpr
    goods.filter(_.name.toLowerCase like likeExpr.toLowerCase).list()
  }

  def listGoods(implicit session: Session) = goods.list()
  def listSales(implicit session: Session) = sales.list()
  def listTransitions(implicit session: Session) = transitions.list()
  def insertGood(g: NoCGLibGood)(implicit session: Session) = goods.insert(g)
  def insertSale(s: NoCGLibSale)(implicit session: Session) = sales.insert(s)
  def insertTransition(t: NoCGLibTransition)(implicit session: Session) = transitions.insert(t)

  def drop(implicit session: Session) {
    (transitions.ddl ++ goods.ddl ++ sales.ddl).drop
  }

  def create(implicit session: Session) {
    (goods.ddl ++ sales.ddl ++ transitions.ddl).create
  }

//  class SalesAlias(tag: Tag) extends Table[SalesAlias](tag, "SALESALIAS") {
//    def id = column[String]("ID")
//    def sales = column[String]("SALES")
//
//    def * = (id, sales) <> (SalesAlias.tupled, SalesAlias.unapply)
//  }
}
