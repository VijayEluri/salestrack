package org.tort.trade.mobile

import android.os.{AsyncTask, Bundle}
import android.widget._
import android.view.View.{OnLongClickListener, OnClickListener}
import android.view.View
import java.util.Date
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import GoodsActivity._
import android.widget.AdapterView.OnItemClickListener
import scalaz._
import Scalaz._

class GoodsActivity extends TypedActivity {
  var shortcutFilters = Map("КАПРИ" -> false, "СИН" -> true)
  var stages = Seq("")

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.goods)

    setDirectionText()
    updateShortcuts()
    updateSearchHistory()
    updateGoods()
  }

  private def updateSearchHistory() {
    val historyView = findViewById(R.id.searchHistoryView).asInstanceOf[ListView]
    val stagesToDraw = stages.collect {
      case "" => "СБРОС"
      case x => x
    }.toArray
    historyView.setAdapter(new ArrayAdapter[String](this, R.layout.history_element_view, stagesToDraw))
    historyView.setOnItemClickListener(new OnItemClickListener {
      def onItemClick(parent: AdapterView[_], view: View, position: Int, id: Long): Unit = {
        stages = stages.take(position + 1)
        updateSearchHistory()
        updateGoods()
      }
    })
  }

  private def updateShortcuts() {
    val layout = findViewById(R.id.shortcutsGridLayout).asInstanceOf[GridLayout]

    shortcutFilters.foreach(filterWithState => addShortcut(filterWithState, layout))
  }


  private def addShortcut(filterWithState: (String, Boolean), layout: GridLayout) {
    val (shortcut, enabled) = filterWithState
    val testShortcutButton = new ToggleButton(this)
    testShortcutButton.setText(shortcut)
    testShortcutButton.setTextOff(shortcut)
    testShortcutButton.setTextOn(shortcut)
    testShortcutButton.setChecked(enabled)
    testShortcutButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener {
      def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) = {
        isChecked match {
          case true => enableFilter(buttonView.getText.toString)
          case false => disableFilter(buttonView.getText.toString)
        }
        updateGoods()
      }
    })

    layout.addView(testShortcutButton)
  }

  private def disableFilter(filter: String) {
    shortcutFilters = shortcutFilters.updated(filter, false)
  }

  private def enableFilter(filter: String) {
    shortcutFilters = shortcutFilters.updated(filter, true)
  }

  private def updateGoods() {
    val goodsGrid = findViewById(R.id.goodsGridLayout).asInstanceOf[GridLayout]
    viewObserverTo(goodsGrid, () => setRowCount(goodsGrid))
    val filters = shortcutFilters.filter(_._2).map(_._1).toSet
    goodsGrid.removeAllViews()
    new GoodsTask(stages.last, filters, this, goodsGrid).execute()
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

  def addStage(stage: String) {
    stages = stages :+ stage
    updateSearchHistory()
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

class GoodsTask(subname: String, filters: Set[String], goodsActivity: GoodsActivity, goodsGrid: GridLayout) extends AsyncTask[AnyRef, Int, Seq[String]] {
  def doInBackground(params: AnyRef*) = {
    val goods = findGoods(subname)
    goodsActivity.runOnUiThread(new Runnable() {
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
    val quantityView = goodsActivity.getLayoutInflater.inflate(R.layout.number_view, null)
    goodsGrid.addView(quantityView)

    val selectedGoodView = quantityView.findViewById(R.id.goodSelectedView).asInstanceOf[TextView]
    selectedGoodView.setText(good)

    val numbers = Seq(1, 3, 5)
    val countLayout = quantityView.findViewById(R.id.countLayout).asInstanceOf[LinearLayout]
    val numberView = countLayout.findViewById(R.id.numberSelectedView).asInstanceOf[TextView]
    val negative = countLayout.findViewById(R.id.negative).asInstanceOf[LinearLayout]
    numbers.reverse.foreach(n => addButton(negative, numberView, (n * -1)))
    val positive = countLayout.findViewById(R.id.positive).asInstanceOf[LinearLayout]
    numbers.foreach(n => addButton(positive, numberView, n))
  }

  private def addButton(parent: LinearLayout, numberView: TextView, number: Int) {
    val button = new Button(goodsActivity)
    button.setText(number.toString)
    button.setOnClickListener(new OnClickListener {
      def onClick(v: View): Unit = {
        val currentNumber = numberView.getText.toString.toInt
        numberView.setText((currentNumber + number).toString)
      }
    })
    parent.addView(button)
  }

  private def addSelectedGoodView(good: String, view: TextView) {
    import NoCGLibTransition._
    import NoCGLibSale._

    view.setText(good)
    view.setClickable(true)
    view.setOnLongClickListener(new OnLongClickListener {
      def onLongClick(v: View) = {
        new SQLiteDAO(goodsActivity).insertTransition(
          NoCGLibTransition(saleId(2.toString), saleId(3.toString), quantity(1), new Date(), saleId(2.toString), NoCGLibGood.id(208.toString))
        )
        true
      }
    })
    goodsGrid.addView(view)
  }

  private def addGoodView(good: String) = {
    val view: TextView = goodsActivity.getLayoutInflater.inflate(R.layout.good_name_view, null).asInstanceOf[TextView]
    view.setText(good)
    view.setClickable(true)
    view.setOnClickListener(new OnClickListener {
      def onClick(v: View) {
        val substr = v.asInstanceOf[TextView].getText.toString
        goodsActivity.addStage(substr)
        goodsGrid.removeAllViews()
        new GoodsTask(substr, filters, goodsActivity, goodsGrid).execute()
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

  private val dao: DAO = new SQLiteDAO(goodsActivity)

  private def mats(subnameLength: Int, subname: String, prevSize: Int): List[String] = {
    dao.matsBy(subnameLength, subname, filters) match {
      case Nil => Nil
      case goodsNames if goodsNames.size < 15 && maxLength(goodsNames) > prevSize =>
        mats(subnameLength + 1, subname, maxLength(goodsNames))
      case goodsNames => goodsNames
    }
  }

  private def maxLength(goodNames: List[String]): Int = {
    goodNames.map(_.length).max
  }
}
