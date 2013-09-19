package org.tort.trade.mobile

import android.database.sqlite.{SQLiteDatabase, SQLiteOpenHelper}
import android.content.Context
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import scala.slick.session.Database
import Database.threadLocalSession
import android.util.Log

object H2DBDAO extends DAO {

  import scala.slick.session.Database

  Class.forName("org.h2.Driver")
  val DbUrl: String = """jdbc:h2:tcp://192.168.1.155:9092/~/workspace/salestrack/trade"""
  val user = "sa"
  val password = ""

  val db: Database = Database.forURL(DbUrl, user, password, driver = "org.h2.Driver")

  def matsBy(subnameLength: Int, subname: String): List[String] = {
    db withSession {
      sql"select distinct substring(name, 0, $subnameLength) as subname from MAT where name is not null and name like $subname order by subname asc".as[String].list
    }
  }
}

class SQLiteDAO(context: Context) extends DAO {
  def matsBy(subnameLength: Int, subname: String) = {
    val query: String = s"select distinct substr(name, 0, $subnameLength) as subname from mat where name is not null and name like '$subname' order by subname asc"
    val cursor = new DBHelper(context).getReadableDatabase().rawQuery(query, Array())
    val goods = scala.collection.mutable.ListBuffer[String]()
    while (!cursor.isAfterLast) {
      val good = cursor.getString(0)
      goods += good
    }

    cursor.close()
    goods += "TEST"
    goods.toList
  }
}

trait DAO {
  def matsBy(subnameLength: Int, subname: String): List[String]
}

class DBHelper(context: Context, val databaseVersion: Int = 2) extends SQLiteOpenHelper(context, "trade.db", null, databaseVersion) {

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
