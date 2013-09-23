package org.tort.trade.mobile

import android.app.Activity
import android.os.AsyncTask

class SyncTask(activity: Activity) extends AsyncTask[AnyRef, Int, Unit] {
  def doInBackground(params: AnyRef*) = {
    syncMaterials
  }

  def syncMaterials = {
    val serverMats: List[NoCGLibGood] = H2DBDAO.allMats
    val sQLiteDAO: SQLiteDAO = new SQLiteDAO(activity)
    val localMats: List[NoCGLibGood] = sQLiteDAO.allMats
    def ids(coll: List[NoCGLibGood]) = coll.map(_.id)

    val diff = ids(serverMats).diff(ids(localMats))
    val diffGoods = serverMats.filter(m => diff.contains(m.id))
    diffGoods.foreach(sQLiteDAO.insert(_))
  }
}

class NoCGLibGood(val id: String, val name: String)
