package org.tort.trade.mobile

import android.app.Activity
import android.os.AsyncTask
import com.fasterxml.uuid.{Generators, EthernetAddress}
import android.content.{Intent, Context}
import scalaz._
import Scalaz._

class SyncTask(activity: Activity) extends AsyncTask[AnyRef, Int, Unit] {
  def doInBackground(params: AnyRef*) = {
    syncMaterials
  }

  def syncMaterials = {
    val ipH2 = Option(activity.getPreferences(Context.MODE_PRIVATE).getString(Settings.RemoteServerIpKey, null))
    ipH2 match {
      case None => new Intent(activity, classOf[EditRemoteServerIpActivity]) |> activity.startActivity
      case Some(ip) => sync(ip)
    }
  }

  private def sync(ipH2: String) {
    val sqliteDAO: SQLiteDAO = new SQLiteDAO(activity)
    val remoteMats: Set[NoCGLibGood] = H2DBDAO(ipH2).allMats
    val localMats: Set[NoCGLibGood] = sqliteDAO.allMats
    def ids(coll: Set[NoCGLibGood]) = coll.map(_.id)
    val diff = ids(remoteMats) -- (ids(localMats))
    val diffGoods = remoteMats.filter(m => diff.contains(m.id))
    diffGoods.foreach(sqliteDAO.insert(_))
  }
}

class NoCGLibGood(val id: String, val name: String)

object NoCGLibGood {

  trait Id

  def id(goodId: String): String @@ Id = Tag(goodId)

  def goodsEquality: Equal[NoCGLibGood] = Equal.equal((left, right) => left.id === right.id)
}

object UUIDGenerator {
  val nic = EthernetAddress.fromInterface
  val uuidGenerator = Generators.timeBasedGenerator(nic)

  def generate = uuidGenerator.generate
}
