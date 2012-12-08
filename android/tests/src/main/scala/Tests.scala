package org.tort.trade.mobile.tests

import org.tort.trade.mobile._
import junit.framework.Assert._
import _root_.android.test.AndroidTestCase
import _root_.android.test.ActivityInstrumentationTestCase2

class AndroidTests extends AndroidTestCase {
  def testPackageIsCorrect() {
    assertEquals("org.tort.trade.mobile", getContext.getPackageName)
  }
}

class ActivityTests extends ActivityInstrumentationTestCase2(classOf[Journal]) {
   def testHelloWorldIsShown() {
      val activity = getActivity
      val textview = activity.findView(TR.GoodsView)
      assertEquals(textview.getText, "hello, world!")
    }
}
