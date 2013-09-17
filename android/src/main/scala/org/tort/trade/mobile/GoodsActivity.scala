package org.tort.trade.mobile

import android.os.{AsyncTask, Bundle}
import android.widget._
import android.app.Activity
import scala.slick.jdbc.{StaticQuery => Q}
import Q.interpolation
import scala.slick.session.Database
import Database.threadLocalSession
import android.view.View.OnClickListener
import android.view.View

class GoodsActivity extends TypedActivity {
  override def onCreate(savedInstanceState: Bundle) {
    val context = this
    super.onCreate(savedInstanceState)
    setContentView(R.layout.goods)

    setDirectionText
    val goodsGrid = findViewById(R.id.goodsGridLayout).asInstanceOf[GridLayout]
    loadGoods(goodsGrid, "")
  }

  def loadGoods(gridLayout: GridLayout, subname: String) {
    new GoodsTask(subname, this, gridLayout).execute()
  }

  private def setDirectionText {
    val direction = getIntent.getStringExtra("direction")
    val directionView = findViewById(R.id.transitionDirection).asInstanceOf[TextView]
    directionView.setText(direction)
  }
}

class GoodsTask(subname: String, activity: Activity, goodsGrid: GridLayout) extends AsyncTask[AnyRef, Int, Seq[String]] {
  def doInBackground(params: AnyRef*) = {
    val goods = findGoods(subname)
    activity.runOnUiThread(new Runnable() {
      def run() {
        goods.foreach {
          case good =>
            val view: TextView = activity.getLayoutInflater.inflate(R.layout.tile, null).asInstanceOf[TextView]
            view.setText(good)
            view.setClickable(true)
            view.setOnClickListener(new OnClickListener {
              def onClick(v: View) {
                val textView = v.asInstanceOf[TextView]
                goodsGrid.removeAllViews()
                new GoodsTask(textView.getText.toString, activity, goodsGrid).execute()
              }
            })
            goodsGrid.addView(view)
        }
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
      case x if x.size < 15 && maxLength(x) > prevSize =>
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
