package org.tort.trade.mobile

import android.os.{AsyncTask, Bundle}
import android.widget._
import android.view.View.{OnLongClickListener, OnClickListener}
import android.view.{MenuItem, Menu, View}
import java.util.Date
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import org.tort.trade.mobile.GoodsActivity._
import android.widget.AdapterView.OnItemClickListener
import scalaz._
import Scalaz._
import android.content.Intent
import Settings._

class GoodsActivity extends TypedActivity {

  var activityState = ActivityState(
    Map(),
    Seq(""),
    None,
    None
  )

  private def shortcutFilters: Map[String, Boolean] = (shortcuts map (_ -> false)).toMap

  private implicit val activity = this

  object Menu {
    val EditShortcutsActionId = 0
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    super.onCreateOptionsMenu(menu)

    val editShortcutsMenuItem = menu.add(0, Menu.EditShortcutsActionId, 1, "Фильтры")
    editShortcutsMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

    true
  }

  override def onMenuItemSelected(featureId: Int, item: MenuItem): Boolean = {
    item.getItemId match {
      case Menu.EditShortcutsActionId =>
        new Intent(this, classOf[EditShortcutActivity]) |> this.startActivity
        true
    }
  }

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.goods)
    
    activityState = activityState.copy(shortcutFilters = shortcutFilters)

    redrawDirectionText()
    redrawShortcuts(activityState)
    activityState.searchHistory |> stagesToDraw |> redrawSearchHistory
    updateGoods()
  }

  def nextActivity() {
    startActivity(new Intent(this, classOf[Journal]))
  }


  override def onRestart(): Unit = {
    super.onRestart()
    
    activityState = activityState.copy(shortcutFilters = shortcutFilters)

    redrawShortcuts(activityState)
  }

  private def getTransitionSession: TransitionSession =
    getIntent.getSerializableExtra(Journal.TransitionSessionKey).asInstanceOf[TransitionSession]

  private def stagesToDraw(history: Seq[String]) = history.collect {
    case "" => "СБРОС"
    case x => x
  }.toArray

  private def redrawSearchHistory(stageLabels: Array[String]) {
    val historyView = findViewById(R.id.searchHistoryView).asInstanceOf[ListView]
    historyView.setAdapter(new ArrayAdapter[String](this, R.layout.history_element_view, stageLabels))
    historyView.setOnItemClickListener(new OnItemClickListener {
      def onItemClick(parent: AdapterView[_], view: View, position: Int, id: Long): Unit = {
        val newState = activityState.cutHistory(position + 1)
        activityState = newState
        newState.searchHistory |> stagesToDraw |> redrawSearchHistory
        updateGoods()
      }
    })
  }

  private def redrawShortcuts(state: ActivityState) {
    val layout = findViewById(R.id.shortcutsGridLayout).asInstanceOf[GridLayout]
    layout.removeAllViews()

    state.shortcutFilters.foreach(filterWithState => addShortcut(filterWithState, layout))
  }


  private def addShortcut(filterWithState: (String, Boolean), layout: GridLayout) {
    val (shortcut, enabled) = filterWithState
    val shortcutButtonView = new ToggleButton(this)
    shortcutButtonView.setText(shortcut)
    shortcutButtonView.setTextOff(shortcut)
    shortcutButtonView.setTextOn(shortcut)
    shortcutButtonView.setChecked(enabled)
    shortcutButtonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener {
      def onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) = {
        activityState = newState(activityState, isChecked, buttonView.getText.toString)
        updateGoods()
      }
    })

    layout addView shortcutButtonView
  }
  
  private def newState(state: ActivityState, isChecked: Boolean, text: String): ActivityState = isChecked match {
    case true =>
      state.enableFilter(text)
    case false =>
      state.disableFilter(text)
  }

  private def updateGoods() {
    val goodsGrid = findViewById(R.id.goodsGridLayout).asInstanceOf[GridLayout]
    viewObserverTo(goodsGrid, () => setRowCount(goodsGrid))
    val filters = activityState.shortcutFilters.filter(_._2).map(_._1).toSet
    goodsGrid.removeAllViews()
    new GoodsTask(activityState.searchHistory.last, filters, this, goodsGrid, getTransitionSession, SQLiteDAO(activity)).execute()
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
    val newState = activityState.addStage(stage)
    activityState = newState
    newState.searchHistory |> stagesToDraw |> redrawSearchHistory
  }

  private def redrawDirectionText() {
    val direction = getIntent.getStringExtra("direction")
    val directionView = findViewById(R.id.transitionDirection).asInstanceOf[TextView]
    directionView.setText(direction)
  }
}

object GoodsActivity {
  val cellMargin = 10

  val TextHeightKey = "com.tort.trade.mobile.TextHeight"

  val ShortcutsKey = "com.tort.trade.mobile.ShortcutFilters"
}

class GoodsTask(subname: String,
                filters: Set[String],
                goodsActivity: GoodsActivity,
                goodsGrid: GridLayout,
                transitionSession: TransitionSession,
                localDAO: SQLiteDAO) extends AsyncTask[AnyRef, Int, Seq[String]] {
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
    goodsGrid addView quantityView

    val selectedGoodView = quantityView.findViewById(R.id.goodSelectedView).asInstanceOf[TextView]
    addSelectedGoodView(good, selectedGoodView)

    val countLayout = quantityView.findViewById(R.id.countLayout).asInstanceOf[LinearLayout]
    redrawNumberLayout(
      countLayout,
      Seq(1, 3, 5),
      _.quantity,
      (c) => goodsActivity.activityState = goodsActivity.activityState.updateQuantity(c)
    )

    val priceLayout = quantityView.findViewById(R.id.priceLayout).asInstanceOf[LinearLayout]
    redrawNumberLayout(
      priceLayout,
      Seq(50, 100, 300, 500),
      _.price,
      (p) => goodsActivity.activityState = goodsActivity.activityState.updatePrice(p)
    )
  }

  private def redrawNumberLayout(countLayout: LinearLayout,
                                 numbers: Seq[Int],
                                 getter: (ActivityState) => Option[Int],
                                 setter: (Int) => Unit) {
    val numberView = countLayout.findViewById(R.id.numberSelectedView).asInstanceOf[TextView]
    getter(goodsActivity.activityState).getOrElse(0).toString |> numberView.setText

    val negative = countLayout.findViewById(R.id.negative).asInstanceOf[LinearLayout]
    numbers.reverse.foreach(n => addButton(negative, numberView, n * -1, getter, setter))

    val positive = countLayout.findViewById(R.id.positive).asInstanceOf[LinearLayout]
    numbers.foreach(n => addButton(positive, numberView, n, getter, setter))
  }

  private def addButton(parent: LinearLayout,
                        numberView: TextView,
                        number: Int,
                        value: ActivityState => Option[Int],
                        updateState: (Int) => Unit) {
    val button = new Button(goodsActivity)
    button.setText(number.toString)
    button.setOnClickListener(new OnClickListener {
      def onClick(v: View): Unit = {
        def current = value(goodsActivity.activityState).getOrElse(0)
        updateState(number)
        current.toString |> numberView.setText
      }
    })
    parent.addView(button)
  }

  private def addSelectedGoodView(goodName: String, view: TextView) {
    view.setText(goodName)
    view.setClickable(true)
    view.setOnLongClickListener(new OnLongClickListener {
      def onLongClick(v: View) = {
        lazy val good = localDAO.goodByName(goodName)
        for {
          from <- transitionSession.from
          fromId = from.id
          to <- transitionSession.to
          toId = to.id
          journal <- transitionSession.journal
          journalId = journal.id
          quantity <- goodsActivity.activityState.quantity
        } yield {
          localDAO.insertTransition(
            NoCGLibTransition(fromId, toId, NoCGLibTransition.quantity(quantity), new Date(), journalId, good.id)
          )
          goodsActivity.nextActivity()
        }
        true
      }
    })
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
        new GoodsTask(substr, filters, goodsActivity, goodsGrid, transitionSession, localDAO).execute()
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

  private def mats(subnameLength: Int, subname: String, prevSize: Int): List[String] = {
    localDAO.matsBy(subnameLength, subname, filters) match {
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

case class ActivityState(
                          shortcutFilters: Map[String, Boolean],
                          searchHistory: Seq[String],
                          quantity: Option[Int],
                          price: Option[Int]) {
  def cutHistory(position: Int) = copy(searchHistory = searchHistory.take(position), quantity = None, price = None)
  def disableFilter(filter: String) = copy(shortcutFilters = shortcutFilters.updated(filter, false), quantity = None, price = None)
  def enableFilter(filter: String) = copy(shortcutFilters = shortcutFilters.updated(filter, true), quantity = None, price = None)
  def addStage(stage: String) = copy(searchHistory = searchHistory :+ stage, quantity = None, price = None)
  def updateQuantity(q: Int) = copy(quantity = q.some |+| this.quantity)
  def updatePrice(p: Int) = copy(price = p.some |+| this.price)
}

