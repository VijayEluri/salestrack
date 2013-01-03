package org.tort.trade.mobile

import android.os.Bundle
import android.widget.TextView

class GoodsActivity extends TypedActivity {
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.goods)
    val direction = getIntent.getStringExtra("direction")
    val directionView = findViewById(R.id.transitionDirection).asInstanceOf[TextView]
    directionView.setText(direction)
  }
}
