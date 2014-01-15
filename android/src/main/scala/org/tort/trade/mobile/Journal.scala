package org.tort.trade.mobile

import android.os.Bundle
import android.widget._
import android.view._
import android.content.{Intent, Context}
import android.view.View._
import android.graphics.Color
import android.graphics.drawable.{ColorDrawable, Drawable}
import scalaz._
import Scalaz._
import NoCGLibSale._

class Journal extends TypedActivity {
  private val RefreshActionId = 1
  private var transitionSession = TransitionSession()
  private val Alpha = 100

  val textViews = Map(
    NoCGLibSale(saleId("2"), saleName("Покупатель")) -> R.id.customer,
    NoCGLibSale(saleId("1"), saleName("Поставщик")) -> R.id.supplier,
    NoCGLibSale(saleId("3"), saleName("Гена")) -> R.id.gena,
    NoCGLibSale(saleId("7"), saleName("Кума")) -> R.id.masha,
    NoCGLibSale(saleId("11"), saleName("Оля")) -> R.id.ola,
    NoCGLibSale(saleId("10"), saleName("Саша")) -> R.id.sasha,
    NoCGLibSale(saleId("9"), saleName("Таня")) -> R.id.tana,
    NoCGLibSale(saleId("8"), saleName("Валя")) -> R.id.vala
  )

  val SelectionColor = "#cc33ff"

  override def onCreateOptionsMenu(menu: Menu) = {
    val syncMenuItem: MenuItem = menu.add(0, RefreshActionId, 0, "Sync")
    syncMenuItem.setIcon(R.drawable.navigation_refresh)
    syncMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

    true
  }


  override def onMenuItemSelected(featureId: Int, item: MenuItem) = {
    item.getItemId match {
      case RefreshActionId =>
        new SyncTask(this).execute()
        true
      case _ => super.onMenuItemSelected(featureId, item)
    }
  }

  val context = this

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    transitionSession = Option(bundle)
      .flatMap(b => Option(b.getSerializable(TransitionSessionKey)))
      .map(_.asInstanceOf[TransitionSession])
      .getOrElse(TransitionSession())

    setContentView(R.layout.main)

    setSaleClickListeners()
    updateAll()
  }

  private val TransitionSessionKey = "com.tort.trade.mobile.TransitionSession"

  override def onSaveInstanceState(outState: Bundle) = {
    outState.putSerializable(TransitionSessionKey, transitionSession)
    super.onSaveInstanceState(outState)
  }


  override def onRestoreInstanceState(savedInstanceState: Bundle) = {
    super.onRestoreInstanceState(savedInstanceState)
    transitionSession = savedInstanceState.getSerializable(TransitionSessionKey).asInstanceOf[TransitionSession]
  }

  private def updateAll() {
    updateDefaultColors()
    
    updateCurrentJournal(transitionSession.journal)
    updateSale(transitionSession.from)
    updateSale(transitionSession.to)
  }


  private def updateDefaultColors() {
    textViews.foreach { case (sale, viewId) => setDefaultColor(sale, viewId) }
  }

  private def setDefaultColor(sale: NoCGLibSale, viewId: Int) {
    val color = sale.id match {
      case id if id === saleId("1") => "#fffb7c"
      case id if id === saleId("2") => "#fffb7c"
      case _ => "#55d8aa"
    }

    val view = findViewById(viewId)
    view.setBackgroundColor(Color.parseColor(color))
    view.setAlpha(255)
  }

  private def updateSale(sale: Option[NoCGLibSale]) {
    sale.foreach(selectFrom)
  }

  private def selectFrom(sale: NoCGLibSale) {
    (textViews(sale) |> findViewById).getBackground.setAlpha(Alpha)
  }

  private def updateCurrentJournal(sale: Option[NoCGLibSale]) {
    clearCurrentJournalSelection()

    sale.foreach(selectNewCurrentJournal)
  }

  private def selectNewCurrentJournal(sale: NoCGLibSale) {
    val view = (textViews(sale) |> findViewById).asInstanceOf[TextView]
    view.setBackgroundColor(Color.parseColor(SelectionColor))
  }

  private def clearCurrentJournalSelection() {
  }

  def setSaleClickListeners() {
    textViews.foreach {
      case (sale, viewId) =>
        setLongClickListener(viewId, sale)
        setClickListener(viewId, sale)
    }
  }

  private def setClickListener(viewId: Int, sale: NoCGLibSale) {
    val view = findViewById(viewId).asInstanceOf[TextView]
    view.setOnClickListener(new OnClickListener {
      def onClick(v: View) = {
        setFromOrTo(sale)
      }
    })
  }

  private def setFromOrTo(sale: NoCGLibSale) {
    transitionSession match {
      case TransitionSession(Some(journal), None, None) =>
        transitionSession = transitionSession.copy(from = sale.some)
      case TransitionSession(Some(journal), Some(from), None) =>
        sale match {
          case s if s === from =>
            transitionSession = transitionSession.copy(from = none)
          case _ =>
            transitionSession = transitionSession.copy(to = sale.some)
            startGoodActivity
        }
      case _ =>
    }
    updateAll()
  }


  private def startGoodActivity {
    val intent: Intent = new Intent(context, classOf[GoodsActivity])
    intent.putExtra(TransitionSessionKey, transitionSession)
    startActivity(intent)
  }

  private def setLongClickListener(viewId: Int, sale: NoCGLibSale) {
    val view = findViewById(viewId).asInstanceOf[TextView]
    view.setOnLongClickListener(new OnLongClickListener {
      def onLongClick(v: View) = {
        setCurrentJournal(sale)
        updateAll()
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

case class TransitionSession(journal: Option[NoCGLibSale] = None, from: Option[NoCGLibSale] = None, to: Option[NoCGLibSale] = None)
