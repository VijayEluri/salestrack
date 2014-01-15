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
  var from = "undefined"
  val RefreshActionId = 1
  var transitionSession = TransitionSession()

  val textViews = Map(
    NoCGLibSale(saleId("2"), saleName("Покупатель")) -> R.id.customer,
    NoCGLibSale(saleId("1"), saleName("Поставщик")) -> R.id.supplier,
    NoCGLibSale(saleId("3"), saleName("Гена")) -> R.id.gena,
    NoCGLibSale(saleId("7"), saleName("Кума")) -> R.id.masha,
    NoCGLibSale(saleId("11"), saleName("Оля")) -> R.id.ola,
    NoCGLibSale(saleId("10"), saleName("Саша")) -> R.id.sasha,
    NoCGLibSale(saleId("9"), saleName("Таня")) -> R.id.tana,
    NoCGLibSale(saleId("8"), saleName("Валя")) -> R.id.vala
  ).map(x => x._1 -> findViewById(x._2).asInstanceOf[TextView])

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
    setContentView(R.layout.main)

    setSaleClickListeners
    updateAll
  }

  private def updateAll {
    updateCurrentJournal(transitionSession.journal)
  }

  private def updateCurrentJournal(sale: Option[NoCGLibSale]) {
    clearCurrentJournalSelection

    sale.foreach {
      case journal =>
        selectNewCurrentJournal(journal)
    }
  }

  private def selectNewCurrentJournal(sale: NoCGLibSale) {
    textViews(sale).setTextAppearance(context, R.style.current_journal)
  }

  private def clearCurrentJournalSelection {
  }

  def setSaleClickListeners {
    textViews.foreach {
      case (sale, view) =>
        setLongClickListener(view, sale)
        setClickListener(view, sale)
    }
  }

  private def setClickListener(view: View, sale: NoCGLibSale) {
    view.setOnClickListener(new OnClickListener {
      def onClick(v: View) = {
        val itemView: TextView = v.asInstanceOf[TextView]
        val intent: Intent = new Intent(context, classOf[CheckJournalActivity])
        intent.putExtra("sale", itemView.getText)
        startActivity(intent)
      }
    })
  }

  private def setLongClickListener(view: View, sale: NoCGLibSale) {
    view.setOnLongClickListener(new OnLongClickListener {
      def onLongClick(v: View) = {
        setCurrentJournal(sale)
        updateAll
        true
      }
    })
  }

  private def setCurrentJournal(sale: NoCGLibSale) {
    transitionSession = transitionSession.copy(journal = sale.some)
  }

  private def showFromAndTo(from: String, to: String) {
    val direction: String = "from " + from + " to " + to
    val intent: Intent = new Intent(context, classOf[GoodsActivity])
    intent.putExtra("direction", direction)
    startActivity(intent)
  }

  def toColor(drawable: Drawable): Int = {
    drawable.asInstanceOf[ColorDrawable].getColor
  }
}

class SalesDragListener(context: Context, backgroundColor: Int, action: (String, String) => Unit) extends OnDragListener {
  def onDrag(view: View, event: DragEvent) = event.getAction match {
    case DragEvent.ACTION_DRAG_STARTED =>
      true
    case DragEvent.ACTION_DRAG_ENTERED =>
      view.setBackgroundColor(Color.DKGRAY)
      view.invalidate()
      true
    case DragEvent.ACTION_DRAG_EXITED =>
      view.setBackgroundColor(backgroundColor)
      view.invalidate()
      true
    case DragEvent.ACTION_DROP =>
      view.setBackgroundColor(backgroundColor)
      view.invalidate()
      val textView = view.asInstanceOf[TextView]
      val to = textView.getText.toString
      val from: String = event.getClipData.getItemAt(0).getText.toString
      action(from, to)
      true
    case _ => true
  }
}

case class TransitionSession(journal: Option[NoCGLibSale] = None, from: Option[NoCGLibSale] = None, to: Option[NoCGLibSale] = None)
