package org.tort.trade.mobile

import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import android.content.Context
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import scala.slick.session.Database
import Database.threadLocalSession
import android.database.Cursor
import scalaz._
import Scalaz._
import java.util.Date
import NoCGLibSale._
import android.app.Activity

class SlickDAO(val db: Database) extends DAO {
  def matsBy(subnameLength: Int, subname: String, filters: Set[String]): List[String] = db withSession {
    sql"select distinct substring(name, 0, $subnameLength) as subname from MAT where name is not null and name like $subname order by subname asc".as[String].list
  }

  def allMats = db withSession {
    val list: Set[(String, String)] = sql"select m1.seq_m, m1.name from mat m1".as[(String, String)].list.toSet
    list.map {
      case (goodId, goodName) =>
        new NoCGLibGood(goodId |> NoCGLibGood.id, goodName)
    }
  }

  def allSales = db withSession {
    val list = sql"select dep_seq, dep_name from dep".as[(String, String)].list
    list map {
      case (sid, sname) =>
        NoCGLibSale(saleId(sid), saleName(sname))
    }
  }

  def maxTransitionDate: Date = db withSession {
    sql"select max(trd_date) from trade_src".as[java.sql.Timestamp].list.head
  }

  def insertTransition(transition: NoCGLibTransition) = db withSession {
    val tdate = new java.sql.Timestamp(transition.date.getTime)
    sqlu"""insert into trade_src(trd_seq, trd_from, trd_to, trd_quant, trd_date, trd_jref, trd_mat) values (${transition.id}, ${transition.from}, ${transition.to}, ${transition.quant}, $tdate, ${transition.me}, ${transition.good})""".first()
  }

  def getFreeIds(number: Int): Seq[String] = throw new RuntimeException("not implemented")
}

object SlickDAO {
  def apply(db: Database) = new SlickDAO(db)
}

class OracleDAO(db: Database) extends SlickDAO(db) {
  override def getFreeIds(number: Int): Seq[String] = db withSession {
    (1 to number) map {
      case i =>
        sql"select seq.nextval from dual".as[(String)].list.head
    }
  }
}

class SQLiteDAO(db: SQLiteDatabase) extends DAO {
  def matsBy(subnameLength: Int, subname: String, filters: Set[String]) = {
    val renderedFilters = filters.map(f => s"name like '%$f%'") match {
      case s if s.isEmpty => "1 = 1"
      case xs => xs.reduceLeft((acc, f) => acc + " and " + f)
    }
    val query: String = s"select distinct substr(name, 1, $subnameLength) as subname from mat where name is not null and name like '$subname' and ($renderedFilters) order by subname asc"
    val cursor = db.rawQuery(query, Array())

    iterate[String](cursor, extractString)
  }

  private def extractTransition(cursor: Cursor): NoCGLibTransition = {
    import NoCGLibTransition._
    new NoCGLibTransition(
      id = cursor.getString(0) |> id,
      from = cursor.getString(1) |> saleId,
      to = cursor.getString(2) |> saleId,
      quant = cursor.getInt(3) |> quantity,
      date = new Date(cursor.getLong(4)),
      me = cursor.getString(5) |> saleId,
      good = cursor.getString(6) |> NoCGLibGood.id,
      sellPrice = if (cursor.isNull(7)) None else cursor.getInt(7) |> price |> some
    )
  }

  private def extractString(cursor: Cursor) = cursor.getString(0)

  def transitionsLaterThan(date: Date) = {
    val query: String = "select trd_seq, trd_from, trd_to, trd_quant, trd_date, trd_jref, trd_mat, trd_price from trade_src where trd_date > ? order by trd_date desc"
    val cursor = db.rawQuery(query, Array(date.getTime.toString))

    iterate[NoCGLibTransition](cursor, extractTransition)
  }

  def transitionsByJournal(journalId: String @@ SaleId) = {
    val query: String = "select trd_seq, trd_from, trd_to, trd_quant, trd_date, trd_jref, trd_mat, trd_price from trade_src order by trd_date desc"
    val cursor = db.rawQuery(query, Array())

    iterate[NoCGLibTransition](cursor, extractTransition)
  }

  def allMats = {
    val query = s"select m.seq_m, m.name from mat m"
    val cursor = db.rawQuery(query, Array())
    iterate[NoCGLibGood](cursor, extractGood).toSet
  }

  def allSales = {
    val query = "select dep_name, dep_seq from dep order by dep_name"
    val cursor = db.rawQuery(query, Array())
    iterate[NoCGLibSale](cursor, extractSale)
  }

  def goodByName(goodName: String) = {
    val query = s"select m.seq_m, m.name from mat m where m.name = ?"
    val cursor = db.rawQuery(query, Array(goodName))
    iterate[NoCGLibGood](cursor, extractGood).head
  }

  private def extractSale(cursor: Cursor) = {
    val name = cursor.getString(0) |> NoCGLibSale.saleName
    val id = cursor.getString(1) |> NoCGLibSale.saleId
    NoCGLibSale(id, name)
  }

  private def extractGood(cursor: Cursor) = {
    val id = cursor.getString(0) |> NoCGLibGood.id
    val name = cursor.getString(1)
    new NoCGLibGood(id, name)
  }

  def insertTransition(transition: NoCGLibTransition) {
    val query = """insert into trade_src(trd_seq, trd_from, trd_to, trd_quant, trd_date, trd_jref, trd_mat) values (?, ?, ?, ?, ?, ?, ?)"""
    db.execSQL(query, Array[AnyRef](
      transition.id,
      transition.from,
      transition.to,
      transition.quant,
      transition.date.getTime.toString,
      transition.me,
      transition.good
    ))
  }

  def insertGood(good: NoCGLibGood) = {
    val query = """insert into MAT(seq_m, name) values(?, ?)"""
    db.execSQL(query, Array(good.id, good.name))
  }

  def insertSale(sale: NoCGLibSale) = {
    val query = """insert into dep(dep_seq, dep_name) values(?, ?)"""
    db.execSQL(query, Array(sale.id, sale.name))
  }

  private def iterate[T](cursor: Cursor, item: (Cursor) => T): List[T] = {
    val items = scala.collection.mutable.ListBuffer[T]()

    if (cursor.getCount > 0) {
      if (cursor.moveToFirst()) {
        do {
          items += item(cursor)
        } while (cursor.moveToNext())
      }
    }

    cursor.close()
    items.toList
  }

  def updateTransitionId(oldId: String, newId: String) {
    val query = "update trade_src set trd_seq = ? where trd_seq = ?"
    db.execSQL(query, Array(oldId, newId))
  }
}

object SQLiteDAO {
  def apply(activity: Activity): SQLiteDAO = {
    val sqliteDB = new DBHelper(activity).getWritableDatabase()
    new SQLiteDAO(sqliteDB)
  }
}

trait DAO {
  def matsBy(subnameLength: Int, subname: String, filters: Set[String]): List[String] //seq because alphabetically ordered by default

  def allMats: Set[NoCGLibGood]

  def allSales: List[NoCGLibSale]
}

class DBHelper(context: Context, val databaseVersion: Int = 3) extends SQLiteOpenHelper(context, "trade.db", null, databaseVersion) {

  val CreateTransitionsTableQuery = "CREATE TABLE TRADE_SRC(" +
    "TRD_PRICE BIGINT, " +
    "TRD_QUANT BIGINT NOT NULL, " +
    "TRD_MAT VARCHAR(128) NOT NULL,    " +
    "TRD_TO VARCHAR(128) NOT NULL,    " +
    "TRD_SEQ VARCHAR(128) NOT NULL,    " +
    "TRD_DATE DATE NOT NULL,    " +
    "TRD_JREF VARCHAR(128) NOT NULL,    " +
    "TRD_FROM VARCHAR(128) NOT NULL );     "

  val CreateDepartmentsTableQuery = "CREATE TABLE DEP(    " +
    "DEP_NAME VARCHAR(128) NOT NULL,    " +
    "DEP_SEQ VARCHAR(128) NOT NULL); "

  val CreateMaterialsTableQuery = "CREATE TABLE MAT(    " +
    "NAME VARCHAR(128),    " +
    "SEQ_M VARCHAR(128) NOT NULL " +
    "); "

  val CreateAliasTableQuery =
    "CREATE TABLE SALESALIAS(    " +
      "ID VARCHAR(128) NOT NULL," +
      "SALES VARCHAR(128) NOT NULL);"

  def dropTableQuery(table: String) = s"DROP TABLE IF EXISTS $table;"

  val MaterialNameIndexQuery = "CREATE INDEX MAT_NAME_INDEX ON MAT(NAME);"

  def onCreate(db: SQLiteDatabase) {
    db.execSQL(CreateMaterialsTableQuery)
    db.execSQL(MaterialNameIndexQuery)
    db.execSQL(CreateDepartmentsTableQuery)
    db.execSQL(CreateTransitionsTableQuery)
  }

  def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.execSQL(dropTableQuery("MAT"))
    db.execSQL(dropTableQuery("DEP"))
    db.execSQL(dropTableQuery("SALESALIAS"))
    db.execSQL(dropTableQuery("TRADE_SRC"))
    onCreate(db)
  }
}
