package com.tort.trade.model

import scala.slick.driver.JdbcProfile
import java.sql.{Date, Timestamp}
import org.tort.trade.mobile.{NoCGLibTransition, NoCGLibSale, NoCGLibGood}
import scalaz._
import Scalaz._
import scala.slick.jdbc.{GetResult, StaticQuery => Q}
import Q.interpolation

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

  def matchJournals(implicit session: Session): Seq[SuspiciousTransition] = {
    implicit val toSuspiciousTransitions = GetResult(r => SuspiciousTransition(r.<<, r.<<, r.<<, r.<<, r.<<))
    sql"""select df.dep_name, dt.dep_name, d, mm.name, q, c
          from (
          select trd_from as f, trd_to as t, trd_date as d, trd_mat as m, trd_quant as q, count(trd_seq) as c
          from trade_src
          where trd_from > 2
          and trd_to > 2
          and trd_jref > 2
          group by trd_from, trd_to, trd_date, trd_mat, trd_quant
          having (count(trd_seq) % 2) <> 0
          ) st join dep df on df.dep_seq = st.f
          join dep dt on dt.dep_seq = st.t
          join mat mm on mm.seq_m = m
          order by d desc, mm.name, df.dep_name, dt.dep_name""".as[SuspiciousTransition].list()
  }

  case class SuspiciousTransition(from: String, to: String, date: Date, good: String, quant: Int)

  def balance(journalId: Long)(implicit session: Session): Map[NoCGLibGood, Option[Long]] = {
    val groupedIncome = for {
      (t, g) <- transitions innerJoin goods on (_.good === _.id)
      if (t.me === journalId.toString)
      if (t.date <= today)
      if (t.from != t.me)
      if (t.to === t.me)
    } yield (g, t.quant)

    val income = groupedIncome.groupBy(_._1).map {
      case (good, css) => good -> css.map(_._2).sum
    }

    val groupedOutcome = for {
      (t, g) <- transitions innerJoin goods on (_.good === _.id)
      if (t.me === journalId.toString)
      if (t.date <= today)
      if (t.from === t.me)
      if (t.to != t.me)
    } yield (g, t.quant)

    val outcome = groupedOutcome.groupBy(_._1).map {
      case (good, css) => good -> css.map(_._2).sum
    }

    val res = for {
      (i, o) <- income innerJoin outcome on (_._1.id === _._1.id)
      if (i._2 - o._2) > 0L
    } yield (i._1, i._2 - o._2)

    println(res.selectStatement)
    res.toMap
  }

  private def today = new Timestamp(new java.util.Date().getTime)

//  class SalesAlias(tag: Tag) extends Table[SalesAlias](tag, "SALESALIAS") {
//    def id = column[String]("ID")
//    def sales = column[String]("SALES")
//
//    def * = (id, sales) <> (SalesAlias.tupled, SalesAlias.unapply)
//  }
}
