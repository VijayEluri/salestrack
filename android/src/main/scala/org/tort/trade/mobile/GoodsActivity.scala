package org.tort.trade.mobile

import android.os.{AsyncTask, Bundle}
import android.widget._
import android.app.Activity
import android.view.View.{OnLongClickListener, OnClickListener}
import android.view.View
import java.util.Date
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import GoodsActivity._
import android.widget.RadioGroup.OnCheckedChangeListener

class GoodsActivity extends TypedActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.goods)

    setDirectionText()
    updateShortcuts()
    updateGoods()
  }

  private def updateShortcuts() {
    val layout = findViewById(R.id.shortcutsGridLayout).asInstanceOf[GridLayout]

    addShortcut("КАПРИ", layout)
    addShortcut("СИН", layout)
  }


  private def addShortcut(shortcut: String, layout: GridLayout) {
    val testShortcutButton = new ToggleButton(this)
    testShortcutButton.setTextOff(shortcut)
    testShortcutButton.setTextOn(shortcut)
    testShortcutButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener {
      def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) = {
        isChecked match {
          case true => addToFilter(buttonView.getText)
          case false => removeFromFilter(buttonView.getText)
        }
      }
    })

    layout.addView(testShortcutButton)
  }

  addToFilter

  private def updateGoods() {
    val goodsGrid = findViewById(R.id.goodsGridLayout).asInstanceOf[GridLayout]
    viewObserverTo(goodsGrid, () => setRowCount(goodsGrid))
    loadGoods(goodsGrid, "")
  }

  private def viewObserverTo(view: GridLayout, action: () => Unit) {
    view.getViewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener {
      def onGlobalLayout() = {
        view.getViewTreeObserver.removeOnGlobalLayoutListener(this)
        action()
      }
    })
  }

  private def setRowCount(goodsGrid: GridLayout) {
    val heightMargins = cellMargin * 2
    val textHeight = getIntent.getIntExtra(TextHeightKey, 0)
    val rowsNumber = goodsGrid.getMeasuredHeight / (textHeight + heightMargins)
    goodsGrid.setRowCount(rowsNumber)
  }

  private def loadGoods(gridLayout: GridLayout, subname: String) {
    new GoodsTask(subname, this, gridLayout).execute()
  }

  private def setDirectionText() {
    val direction = getIntent.getStringExtra("direction")
    val directionView = findViewById(R.id.transitionDirection).asInstanceOf[TextView]
    directionView.setText(direction)
  }
}

object GoodsActivity {
  val cellMargin = 10

  val TextHeightKey = "com.tort.trade.mobile.TextHeight"
}

class GoodsTask(subname: String, activity: Activity, goodsGrid: GridLayout) extends AsyncTask[AnyRef, Int, Seq[String]] {
  def doInBackground(params: AnyRef*) = {
    val goods = findGoods(subname)
    activity.runOnUiThread(new Runnable() {
      def run() {
        goods match {
          case good :: Nil =>
            insertTransitionView(good)
          case xs =>
            xs foreach addGoodView
        }
      }
    })
    goods
  }

  private def insertTransitionView(good: String) {
    import NoCGLibTransition._
    import NoCGLibSale._

    val view: TextView = activity.getLayoutInflater.inflate(R.layout.good_selected_view, null).asInstanceOf[TextView]
    view.setText(good)
    view.setClickable(true)
    view.setOnLongClickListener(new OnLongClickListener {
      def onLongClick(v: View) = {
        new SQLiteDAO(activity).insertTransition(
          NoCGLibTransition(saleId(2.toString), saleId(3.toString), quantity(1), new Date(), saleId(2.toString), NoCGLibGood.id(208.toString))
        )
        true
      }
    })
    goodsGrid.addView(view)
  }

  private def addGoodView(good: String) = {
    val view: TextView = activity.getLayoutInflater.inflate(R.layout.good_name_view, null).asInstanceOf[TextView]
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
    val params = new GridLayout.LayoutParams()
    params.setMargins(cellMargin, cellMargin, cellMargin, cellMargin)
    view.setLayoutParams(params)
    view
  }

  private def findGoods(sub: String): Seq[String] = {
    val subname = sub.trim + "%"
    val subnameLength = sub.length + 1

    mats(subnameLength, subname, 0)
  }

  private val dao: DAO = new SQLiteDAO(activity)

  private def mats(subnameLength: Int, subname: String, prevSize: Int): List[String] = {
    dao.matsBy(subnameLength, subname) match {
      case x if x.size < 15 && maxLength(x) > prevSize =>
        mats(subnameLength + 1, subname, maxLength(x))
      case x => x
    }
  }


  private def maxLength(x: List[String]): Int = {
    x.map(_.length).max
  }
}
