package org.tort.trade.mobile

import android.os.Bundle
import android.widget.{Button, EditText}
import android.view.View.OnClickListener
import android.view.View
import android.content.Context
import scala.util.matching.Regex
import Settings._

class EditRemoteServerIpActivity extends TypedActivity {
  private val IpPatternStr = """(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})"""
  private val IpPattern = new Regex(IpPatternStr)
  
  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.ip_view)
    val ipEditBox = findViewById(R.id.ipInput).asInstanceOf[EditText]
    readSetting(RemoteServerIpKey).foreach(ipEditBox.setText)
    val pathEditBox = findViewById(R.id.pathInput).asInstanceOf[EditText]
    readSetting(RemoteServerPathKey).foreach(pathEditBox.setText)

    findViewById(R.id.ipSaveButton).asInstanceOf[Button].setOnClickListener(new OnClickListener {
      def onClick(v: View) = {
        findViewById(R.id.ipInput).asInstanceOf[EditText].getText match {
          case IpPattern(ip) =>
            saveIp(ip)
          case _ =>
        }

        Option(findViewById(R.id.pathInput).asInstanceOf[EditText].getText.toString) match {
          case None =>
          case Some("") =>
          case Some(path) => savePath(path)
        }

        for {
          ip <- readSetting(RemoteServerIpKey)
          path <- readSetting(RemoteServerPathKey)
        } yield finish()
      }
    })
  }


  private def readSetting(key: String) = {
    Option(getSharedPreferences(PreferencesFileName, Context.MODE_PRIVATE).getString(key, null))
  }

  private def saveIp(ip: String) {
    getSharedPreferences(PreferencesFileName, Context.MODE_PRIVATE).edit().putString(Settings.RemoteServerIpKey, ip).commit()
  }

  private def savePath(path: String) {
    getSharedPreferences(PreferencesFileName, Context.MODE_PRIVATE).edit().putString(Settings.RemoteServerPathKey, path).commit()
  }
}

object Settings {
  val RemoteServerIpKey = "org.tort.trade.mobile.main-base-ip"
  val RemoteServerPathKey = "org.tort.trade.mobile.main-base-path"
  val PreferencesFileName = "general-preferences"
}
