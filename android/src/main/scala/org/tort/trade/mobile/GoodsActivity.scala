package org.tort.trade.mobile

import android.os.{AsyncTask, Bundle}
import android.widget._
import java.util
import scala.collection.JavaConversions._
import android.app.Activity
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import scala.slick.session.Database
import Database.threadLocalSession

class GoodsActivity extends TypedActivity {
  override def onCreate(savedInstanceState: Bundle) {
    val context = this
    super.onCreate(savedInstanceState)
    setContentView(R.layout.goods)

    setDirectionText
    val goodsListView = findViewById(R.id.goodsListView).asInstanceOf[ListView]
    setGoodsList(context, goodsListView, "")
  }


  private def setGoodsList(context: GoodsActivity, goodsListView: ListView, subname: String) {
    goodsListView.setOnItemClickListener(new OnItemClickListener() {
      def onItemClick(parent: AdapterView[_], view: View, position: Int, id: Long) {
        val layout = findViewById(R.id.goodsLayout).asInstanceOf[LinearLayout]
        val listView: ListView = new ListView(context)
        val layoutParams = new LinearLayout.LayoutParams(100, -1, 1F)
        layout.addView(listView)
        listView.setLayoutParams(layoutParams)
        val textView = view.asInstanceOf[TextView]
        println("SUBNAME: " + textView.getText.toString)
        setGoodsList(context, listView, textView.getText.toString)
      }
    })

    loadGoods(goodsListView, subname)
  }


  def loadGoods(goodsListView: ListView, subname: String) {
    new GoodsTask(subname, this, goodsListView).execute()
  }

  private def setDirectionText {
    val direction = getIntent.getStringExtra("direction")
    val directionView = findViewById(R.id.transitionDirection).asInstanceOf[TextView]
    directionView.setText(direction)
  }
}

class GoodsTask(subname: String, activity: Activity, goodsListView: ListView) extends AsyncTask[AnyRef, Int, Seq[String]] {
  def doInBackground(params: AnyRef*) = {
    val goods = findGoods(subname)
    activity.runOnUiThread(new Runnable() {
      def run() {
        val goodsAdapter = new ArrayAdapter[String](activity, R.layout.tile, new util.ArrayList[String]())
        val layoutParams = new LinearLayout.LayoutParams(100, -1, 1F)
        goodsListView.setLayoutParams(layoutParams)
        goodsListView.setAdapter(goodsAdapter)
        goodsAdapter.clear()
        goodsAdapter.addAll(goods)
        goodsAdapter.notifyDataSetChanged()
      }
    })
    goods
  }


  def findGoods(sub: String): Seq[String] = {
    val subname = sub.trim + "%"
    val subnameLength = sub.length + 1

    mats(subnameLength, subname, 0)
  }


  private def mats(subnameLength: Int, subname: String, prevSize: Int): List[String] = {
    matsBy(subnameLength, subname) match {
      case x if x.size < 10 && maxLength(x) > prevSize =>
        mats(subnameLength + 1, subname, maxLength(x))
      case x => x
    }
  }


  private def maxLength(x: List[String]): Int = {
    x.map(_.length).max
  }

  private def matsBy(subnameLength: Int, subname: String): List[String] = {
    DB.db withSession {
      sql"select distinct substring(name, 0, $subnameLength) as subname from MAT where name is not null and name like $subname order by subname asc".as[String].list
    }
  }
}

object DB {

  import scala.slick.session.Database

  Class.forName("org.h2.Driver")
  val DbUrl: String = "jdbc:h2:tcp://192.168.1.155:9092/~/workspace/salestrack/trade"
  val user = "sa"
  val password = ""

  val db: Database = Database.forURL(DbUrl, user, password, driver = "org.h2.Driver")
}
