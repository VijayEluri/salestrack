package org.tort.trade.mobile

import android.os.Bundle
import android.widget._
import android.view.{LayoutInflater, ViewGroup, View}
import android.content.Context
import android.app.Activity
import scalaz.@@
import org.tort.trade.mobile.NoCGLibSale.SaleId

class CheckJournalActivity extends TypedActivity {
  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.journal)

    renderSalesLabel
    renderTransitions
  }

  private def renderSalesLabel {
    findViewById(R.id.saleLabel).asInstanceOf[TextView].setText(getIntent().getStringExtra("sale"))
  }
  
  private def renderTransitions {
    val listView = findViewById(R.id.transitionsList).asInstanceOf[ListView]

    listView.setAdapter(new TransitionAdapter(context, this, NoCGLibSale.saleId("8")))
  }

  private val context = this
}

class TransitionAdapter(context: Context, activity: Activity, journalId: String @@ SaleId) extends BaseAdapter {
  private lazy val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]

  private val items = SQLiteDAO(activity).transitionsByJournal(journalId)

  def getCount = items.size

  def getItem(position: Int) = items(position)

  def getItemId(position: Int) = position

  def getView(position: Int, convertView: View, parent: ViewGroup) = {
    val view = Option(convertView).getOrElse(inflater.inflate(R.layout.transition, parent, false))
    bindData(view, position)
  }

  private def bindData(view: View, position: Int) = {
    def textView(id: Int) = view.findViewById(id).asInstanceOf[TextView]

    val item = getItem(position)

    textView(R.id.goodNameView).setText("ДЖ СИНИЕ")
    textView(R.id.salesNameView).setText("Валя")
    textView(R.id.transitionDirectionView).setText("<<<<<")
    textView(R.id.quantityView).setText("3")

    view
  }
}
