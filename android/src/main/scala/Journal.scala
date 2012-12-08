package org.tort.trade.mobile

import android.app.{AlertDialog, Activity}
import android.os.Bundle
import android.widget._
import android.widget.AdapterView.{OnItemLongClickListener, OnItemClickListener}
import android.view.{DragEvent, ViewGroup, View}
import android.content.{ClipData, Context}
import android.view.View.{DragShadowBuilder, OnDragListener}
import android.graphics.Color
import android.content.res.Resources
import android.util.DisplayMetrics

class Journal extends Activity with TypedActivity {
  var from = "undefined"
  val context = this

  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    setContentView(R.layout.main)

    val gridAdapter = new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, Array("sales1", "sales2", "sales3")){
      override def getView(position: Int, convertView: View, parent: ViewGroup) = {
        val v: TextView = super.getView(position, convertView, parent).asInstanceOf[TextView]
        v.setOnDragListener(dragListener)
        def r: Resources  = getContext().getResources()
        def metrics: DisplayMetrics  = r.getDisplayMetrics();
        v.setCompoundDrawablePadding((6 * metrics.density + 0.5f).toInt)
        v.setCompoundDrawablesWithIntrinsicBounds(r.getDrawable(R.drawable.star),
          null, null, null);
        v
      }
    }
    val salesView: GridView = findView(TR.salesView)
    salesView.setAdapter(new DragAndDropGridAdapter(context))
    salesView.setOnItemClickListener(new OnItemClickListener {
      def onItemClick(p1: AdapterView[_], view: View, p3: Int, p4: Long) {
        val itemView: TextView = view.asInstanceOf[TextView]
        showAlert(itemView.getText.toString)
      }
    })
    salesView.setOnItemLongClickListener(new OnItemLongClickListener {
      def onItemLongClick(parent: AdapterView[_], view: View, position: Int, id: Long) = {
        val itemView: TextView = view.asInstanceOf[TextView]
        val dragData = ClipData.newPlainText("label", itemView.getText)
        val shadowBuilder = new DragShadowBuilder(view)
        itemView.startDrag(dragData, shadowBuilder, null, 0)
        true
      }
    })

    val adapter = new ArrayAdapter[String](this, android.R.layout.simple_list_item_1, Array("good1", "good2", "good3"))
    findView(TR.goodsView).setAdapter(adapter)
    findView(TR.salesView).setAdapter(gridAdapter)
  }

  val dragListener = new OnDragListener {
      def onDrag(view: View, event: DragEvent) = event.getAction match {
        case DragEvent.ACTION_DRAG_STARTED =>
          true
        case DragEvent.ACTION_DRAG_ENTERED =>
          view.setBackgroundColor(Color.DKGRAY)
          view.invalidate
          true
        case DragEvent.ACTION_DRAG_EXITED =>
          view.setBackgroundColor(Color.BLACK)
          view.invalidate
          true
        case DragEvent.ACTION_DROP =>
          val textView = view.asInstanceOf[TextView]
          val to = textView.getText.toString
          val from: String = event.getClipData.getItemAt(0).getText.toString
          showFromAndTo(to, from)
          true
        case _ => true
      }
    }

  private def showAlert(text: String) {
    val alert = new AlertDialog.Builder(context).create()
    alert.setMessage(text)
    alert.show()
  }

  private def showFromAndTo(from: String, to: String) {
    showAlert("from %s to %s".format(from, to))
  }
}

class DragAndDropGridAdapter(context: Context) extends BaseAdapter {
  def getCount = 0

  def getItem(p1: Int) = null

  def getItemId(p1: Int) = 0L

  def getView(p1: Int, p2: View, p3: ViewGroup) = new TextView(context)
}
