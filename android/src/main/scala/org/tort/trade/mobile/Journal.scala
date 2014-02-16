package org.tort.trade.mobile

import android.os.Bundle
import android.widget._
import android.view._
import android.content.Intent
import android.view.View._
import android.graphics.Color
import android.graphics.drawable.{ColorDrawable, Drawable}
import scalaz._
import Scalaz._
import NoCGLibSale._
import GoodsActivity.TextHeightKey
import Journal._
import android.util.Log

class Journal extends TypedActivity {

  object Menu {
    val RefreshActionId = 1
    val EditRemoteAddressActionId = 2
  }

  import Menu._

  private var transitionSession = TransitionSession()
  private val Alpha = 100

  private val saleViewIds = Map(
    NoCGLibSale(saleId("2"), saleName("Покупатель")) -> R.id.customer,
    NoCGLibSale(saleId("1"), saleName("Поставщик")) -> R.id.supplier
  )

  val SelectionColor = "#cc33ff"

  override def onCreateOptionsMenu(menu: Menu) = {
    val syncMenuItem: MenuItem = menu.add(0, RefreshActionId, 0, "Sync")
    syncMenuItem.setIcon(R.drawable.navigation_refresh)
    syncMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

    val editRemoteAddressMenuItem = menu.add(0, EditRemoteAddressActionId, 1, "Edit remote address")
    editRemoteAddressMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER)

    true
  }


  override def onMenuItemSelected(featureId: Int, item: MenuItem) = {
    item.getItemId match {
      case RefreshActionId =>
        new SyncTask(this).execute()
        true
      case EditRemoteAddressActionId =>
        new Intent(this, classOf[EditRemoteServerIpActivity]) |> this.startActivity
        true
      case _ => super.onMenuItemSelected(featureId, item)
    }
  }

  val context = this

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    restoreTransitionSession(bundle)

    setContentView(R.layout.main)

    createAndUpdateAllViews
  }


  private def createAndUpdateAllViews {
    val textViews = saleTextViews()
    reCreateSaleViews(textViews)
    updateAll(textViews.toMap)
  }

  private def reCreateSaleViews(textViews: Set[(NoCGLibSale, TextView)]) {
    val otherSalesView = findViewById(R.id.otherSales).asInstanceOf[LinearLayout]
    otherSalesView.removeAllViews()
    addSaleViewsToContainer(textViews, otherSalesView)
    setSaleClickListeners(textViews.toMap)
  }

  private def addSaleViewsToContainer(textViews: Set[(NoCGLibSale, TextView)], otherSalesView: LinearLayout) {
    textViews foreach {
      case (s, v) if saleViewIds.keySet(s) =>
      case (s, v) => otherSalesView addView v
    }
  }

  private def saleTextViews(): Set[(NoCGLibSale, TextView)] = {
    new SQLiteDAO(this).allSales collect {
      case sale if saleViewIds.keySet.contains(sale) =>
        sale -> (saleViewIds(sale) |> findViewById).asInstanceOf[TextView]
      case sale =>
        sale -> newSaleView(sale.name)
    }
  }

  private def restoreTransitionSession(bundle: Bundle) {
    transitionSession = Option(bundle)
      .flatMap(b => Option(b.getSerializable(TransitionSessionKey)))
      .map(_.asInstanceOf[TransitionSession])
      .getOrElse(TransitionSession())
  }

  private def newSaleView(saleName: String): TextView = {
    import LinearLayout._
    val newTextView = new TextView(this)
    newTextView.setBackgroundColor(Color.parseColor("#55d8aa"))
    newTextView.setText(saleName)
    newTextView.setPadding(10, 10, 10, 10)
    newTextView.setTextColor(Color.BLACK)
    val marginParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    marginParams.setMargins(0, 2, 0, 2)
    newTextView.setLayoutParams(marginParams)
    newTextView
  }

  override def onSaveInstanceState(outState: Bundle) = {
    outState.putSerializable(TransitionSessionKey, transitionSession)
    super.onSaveInstanceState(outState)
  }

  override def onRestart(): Unit = {
    super.onRestart()
    transitionSession = transitionSession.copy(to = None)

    createAndUpdateAllViews
  }

  override def onRestoreInstanceState(savedInstanceState: Bundle) = {
    super.onRestoreInstanceState(savedInstanceState)
    transitionSession = savedInstanceState.getSerializable(TransitionSessionKey).asInstanceOf[TransitionSession]
    Log.e("TEST", "onRestoreInstance")
  }

  private def updateAll(textViews: Map[NoCGLibSale, TextView]) {
    updateDefaultColors(textViews)
    transitionSession.journal map textViews map updateCurrentJournal
    transitionSession.from map textViews map updateSaleView
    transitionSession.to map textViews map updateSaleView
  }

  private def updateSaleView(textView: TextView) = {
    updateSaleBackground(textView)
  }

  private def updateDefaultColors(saleViews: Map[NoCGLibSale, TextView]) {
    saleViews.foreach {
      case (sale, view) =>
        setDefaultColor(sale, view)
    }
  }

  private def setDefaultColor(sale: NoCGLibSale, view: TextView) {
    val color = sale.id match {
      case id if id === saleId("1") => "#fffb7c"
      case id if id === saleId("2") => "#fffb7c"
      case _ => "#55d8aa"
    }

    view.setBackgroundColor(Color.parseColor(color))
    view.setAlpha(255)
  }

  private def updateSaleBackground(textView: TextView) {
    textView.getBackground.setAlpha(Alpha)
  }

  private def updateCurrentJournal(textView: TextView) {
    clearCurrentJournalSelection()

    selectNewCurrentJournal(textView)
  }

  private def selectNewCurrentJournal(view: TextView) {
    view.setBackgroundColor(Color.parseColor(SelectionColor))
  }

  private def clearCurrentJournalSelection() {
  }

  private def setSaleClickListeners(saleViews: Map[NoCGLibSale, TextView]) {
    saleViews.foreach {
      case (sale, view) =>
        setLongClickListener(saleViews, sale)
        setClickListener(saleViews, sale)
    }
  }

  private def setClickListener(views: Map[NoCGLibSale, TextView], sale: NoCGLibSale) {
    views(sale).setOnClickListener(new OnClickListener {
      def onClick(v: View) = {
        setFromOrTo(views, sale)
      }
    })
  }

  private def setFromOrTo(saleViews: Map[NoCGLibSale, TextView], sale: NoCGLibSale) {
    transitionSession match {
      case TransitionSession(Some(journal), None, None) =>
        transitionSession = transitionSession.copy(from = sale.some)
      case TransitionSession(Some(journal), Some(from), None) =>
        sale match {
          case s if s === from =>
            transitionSession = transitionSession.copy(from = none)
          case _ =>
            transitionSession = transitionSession.copy(to = sale.some)
            startGoodActivity()
        }
      case _ =>
    }
    updateAll(saleViews)
  }

  private def startGoodActivity() {
    val intent: Intent = new Intent(context, classOf[GoodsActivity])
    intent.putExtra(TransitionSessionKey, transitionSession)
    intent.putExtra(TextHeightKey, findViewById(R.id.customer).getMeasuredHeight)
    startActivity(intent)
  }

  private def setLongClickListener(saleViews: Map[NoCGLibSale, TextView], sale: NoCGLibSale) {
    saleViews(sale).setOnLongClickListener(new OnLongClickListener {
      def onLongClick(v: View) = {
        setCurrentJournal(sale)
        updateAll(saleViews)
        true
      }
    })
  }

  private def setCurrentJournal(sale: NoCGLibSale) {
    transitionSession = transitionSession.copy(journal = sale.some)
  }

  def toColor(drawable: Drawable): Int = {
    drawable.asInstanceOf[ColorDrawable].getColor
  }
}

object Journal {
  val TransitionSessionKey = "com.tort.trade.mobile.TransitionSession"
}

case class TransitionSession(journal: Option[NoCGLibSale] = None, from: Option[NoCGLibSale] = None, to: Option[NoCGLibSale] = None) {
  def clearTo = this.copy(to = None)
}
