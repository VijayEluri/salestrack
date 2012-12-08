package org.tort.trade.mobile

import android.app.Activity
import android.os.Bundle

class Journal extends Activity with TypedActivity {
  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    setContentView(R.layout.main)

    findView(TR.textview).setText("hello, android!")
  }
}
