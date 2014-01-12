package org.tort.trade.mobile

import android.os.Bundle
import android.widget.{Button, EditText}
import android.view.View.OnClickListener
import android.view.View
import android.content.Context
import scala.util.matching.Regex

class EditRemoteServerIpActivity extends TypedActivity {
  private val IpPatternStr = """(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})"""
  private val IpPattern = new Regex(IpPatternStr)

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.ip_view)

    findViewById(R.id.ipSaveButton).asInstanceOf[Button].setOnClickListener(new OnClickListener {
      def onClick(v: View) = {
        findViewById(R.id.ipInput).asInstanceOf[EditText].getText match {
          case IpPattern(ip) =>
            saveIp(ip)
            finish()
          case _ =>
        }
      }
    })
  }

  private def saveIp(ip: String) {
    getPreferences(Context.MODE_PRIVATE).edit().putString(Settings.RemoteServerIpKey, ip).commit()
  }
}

object Settings {
  val RemoteServerIpKey = "org.tort.trade.mobile.main-base-ip"
}
