package org.tort.trade.mobile

import android.app.{AlertDialog, Activity}
import android.os.AsyncTask
import android.content.{DialogInterface, Intent, Context}
import scalaz._
import Scalaz._
import Settings._
import android.content.DialogInterface.OnClickListener
import scala.slick.jdbc.JdbcBackend.Database
import com.fasterxml.uuid.{Generators, EthernetAddress}

class SyncTask(activity: Activity) extends AsyncTask[AnyRef, Int, Unit] {
  def doInBackground(params: AnyRef*) = {
    syncAll()
  }

  def syncAll() = {
    val ip = Option(activity.getSharedPreferences(PreferencesFileName, Context.MODE_PRIVATE).getString(Settings.RemoteServerIpKey, null))
    val path = Option(activity.getSharedPreferences(PreferencesFileName, Context.MODE_PRIVATE).getString(Settings.RemoteServerPathKey, null))
    val user = none
    val password = none
    val dbType = OracleDb
    (ip, path, dbType, user, password) match {
      case (Some(i), Some(sid), OracleDb, user, password) =>
        val DbUrl: String = s"""jdbc:oracle:thin:@${i}:1521/${sid}"""
        Class.forName("oracle.jdbc.OracleDriver")
        val db = Database.forURL(DbUrl, user.getOrElse("torhriph"), password.getOrElse("nfufymqjhr"), driver = "oracle.jdbc.OracleDriver")
        val oracleDAO = new OracleDAO(db)
        sync(syncTransitionsOracle)(oracleDAO)
      case _ =>
        new Intent(activity, classOf[EditRemoteServerIpActivity]) |> activity.startActivity
    }
  }

  trait DbType
  case class OracleDb() extends DbType

  private def sync(syncTransitions: (SlickDAO, SQLiteDAO) => Unit)(remoteDAO: SlickDAO) {
    val sqliteDB = new DBHelper(activity).getWritableDatabase()
    val localDAO: SQLiteDAO = new SQLiteDAO(sqliteDB)

    try {
      sqliteDB.beginTransaction()
      remoteDAO.db.withDynSession {
        syncGoods(remoteDAO, localDAO)
        syncSales(remoteDAO, localDAO)
        syncTransitions(remoteDAO, localDAO)
      }
      sqliteDB.endTransaction()
      greetingDialog()
    } catch {
      case e: Exception =>
        activity.runOnUiThread(new Runnable() {
          def run() {
            val builder = new AlertDialog.Builder(activity)
            builder.setPositiveButton("Close", doNothing())
            val dialog = builder.create()
            dialog.setTitle("Ошибка работы с базой")
            dialog.setMessage(e.getMessage)
            dialog.show()
          }
        })
    } finally {
      sqliteDB.close()
    }
  }

  def doNothing() = new OnClickListener {
    def onClick(dialog: DialogInterface, which: Int): Unit = {}
  }

  private def greetingDialog() {
    activity.runOnUiThread(new Runnable() {
      def run() {
        val builder = new AlertDialog.Builder(activity)
        builder.setPositiveButton("Close", doNothing())
        val dialog = builder.create()
        dialog.setMessage("Синхронизация прошла успешно")
        dialog.show()
      }
    })
  }

  private def syncTransitionsH2(h2DAO: SlickDAO, sqliteDAO: SQLiteDAO) {
    val maxRemoteTransitionDate = h2DAO.maxTransitionDate
    val localToSync = sqliteDAO.transitionsLaterThan(maxRemoteTransitionDate)
    localToSync foreach h2DAO.insertTransition
  }

  private def syncTransitionsOracle(remoteDAO: SlickDAO, localDAO: SQLiteDAO) {
    val localTransitionsToSync = remoteDAO.maxTransitionDate |> localDAO.transitionsLaterThan
    val updatedTransitions = localTransitionsToSync |> replaceIds(remoteDAO)
    updatedTransitions foreach remoteDAO.insertTransition

    localTransitionsToSync zip updatedTransitions map {
      case (local, remote) =>
        localDAO.updateTransitionId(local.id, remote.id)
    }
  }

  private def replaceIds(remoteDAO: SlickDAO)(transitions: Seq[NoCGLibTransition]): Seq[NoCGLibTransition] = {
    val freeIds = remoteDAO.getFreeIds(transitions.size)
    transitions zip freeIds map {
      case (t, id) => NoCGLibTransition(t.from, t.to, t.quant, t.date, t.me, t.good)
    }
  }

  private def syncSales(h2DAO: SlickDAO, sqliteDAO: SQLiteDAO) {
    val remoteSales = h2DAO.allSales
    val localSales = sqliteDAO.allSales
    def ids(coll: Set[NoCGLibSale]) = coll.map(_.id)
    val diff = ids(remoteSales.toSet) -- ids(localSales.toSet)
    val diffSales = remoteSales.filter(s => diff.contains(s.id))
    diffSales foreach sqliteDAO.insertSale
  }

  private def syncGoods(h2DAO: SlickDAO, sqliteDAO: SQLiteDAO) {
    val remoteMats: Set[NoCGLibGood] = h2DAO.allMats
    val localMats: Set[NoCGLibGood] = sqliteDAO.allMats
    def ids(coll: Set[NoCGLibGood]) = coll.map(_.id)
    val diff = ids(remoteMats) -- ids(localMats)
    val diffGoods = remoteMats.filter(m => diff.contains(m.id))
    diffGoods foreach sqliteDAO.insertGood
  }
}

object UUIDGenerator {
  val nic = EthernetAddress.fromInterface
  val uuidGenerator = Generators.timeBasedGenerator(nic)

  def generate = uuidGenerator.generate
}
