package org.tort.trade.mobile

import android.app.{AlertDialog, Activity}
import android.os.Bundle
import android.widget._
import android.view.{DragEvent, ViewGroup, View}
import android.content.{ClipData, Context}
import android.view.View._
import android.graphics.Color
import android.graphics.drawable.{ColorDrawable, Drawable}

class Journal extends Activity with TypedActivity {
  var from = "undefined"
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

    textViews.foreach(view => view.setOnLongClickListener(new OnLongClickListener {
      def onLongClick(v: View) = {
        val itemView: TextView = v.asInstanceOf[TextView]
        val dragData = ClipData.newPlainText("label", itemView.getText)
        val shadowBuilder = new DragShadowBuilder(v)
        itemView.startDrag(dragData, shadowBuilder, null, 0)
        true
      }
    }))

    textViews.foreach(view => view.setOnDragListener(new SalesDragListener(context, toColor(view.getBackground))))
  }

  def toColor(drawable: Drawable): Int = {
    drawable.asInstanceOf[ColorDrawable].getColor
  }

  class DragAndDropGridAdapter(context: Context) extends BaseAdapter {
    def getCount = 0

    def getItem(p1: Int) = null

    def getItemId(p1: Int) = 0L

    def getView(p1: Int, p2: View, p3: ViewGroup) = new TextView(context)
  }

}

class SalesDragListener(context: Context, backgroundColor: Int) extends OnDragListener {
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
      showFromAndTo(from, to)
      true
    case _ => true
  }

  private def showFromAndTo(from: String, to: String) {
    showAlert("from %s to %s".format(from, to))
  }

  private def showAlert(text: String) {
    val alert = new AlertDialog.Builder(context).create()
    alert.setMessage(text)
    alert.show()
  }
}
