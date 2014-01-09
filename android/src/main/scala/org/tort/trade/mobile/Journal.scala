package org.tort.trade.mobile

import android.os.Bundle
import android.widget._
import android.view._
import android.content.{Intent, ClipData, Context}
import android.view.View._
import android.graphics.Color
import android.graphics.drawable.{ColorDrawable, Drawable}

class Journal extends TypedActivity {
  var from = "undefined"
  val RefreshActionId = 1

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

    val textViews = Seq(
      R.id.customer,
      R.id.supplier,
      R.id.gena,
      R.id.masha,
      R.id.ola,
      R.id.sasha,
      R.id.tana,
      R.id.vala
    ).map(findViewById)

    textViews.foreach { 
      case view => 
      setLongClickListener(view)
      setClickListener(view)
    }

    textViews.foreach(view => view.setOnDragListener(new SalesDragListener(context, toColor(view.getBackground), showFromAndTo)))
  }

  private def setClickListener(view: View) {
    view.setOnClickListener(new OnClickListener {
      def onClick(v: View) = {
        val itemView: TextView = v.asInstanceOf[TextView]
        val intent: Intent = new Intent(context, classOf[CheckJournalActivity])
        intent.putExtra("sale", itemView.getText)
        startActivity(intent)
      }
    })
  }

  private def setLongClickListener(view: View) {
    view.setOnLongClickListener(new OnLongClickListener {
      def onLongClick(v: View) = {
        val itemView: TextView = v.asInstanceOf[TextView]
        val dragData = ClipData.newPlainText("label", itemView.getText)
        val shadowBuilder = new DragShadowBuilder(v)
        itemView.startDrag(dragData, shadowBuilder, null, 0)
        true
      }
    })
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
