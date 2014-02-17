package org.tort.trade.mobile

import android.os.Bundle
import android.widget.{EditText, Button, GridLayout, ToggleButton}
import android.view.View
import android.view.View.{OnLongClickListener, OnClickListener}
import android.app.AlertDialog
import android.content.DialogInterface
import Settings._
import scalaz._
import Scalaz._

class EditShortcutActivity extends TypedActivity {
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.edit_shortcuts_view)

    redrawShortcuts()
    redrawAddShortcutButton()
  }

  private def redrawAddShortcutButton() {
    val newShortcutButton = findViewById(R.id.addShortcutButton).asInstanceOf[Button]
    newShortcutButton.setOnClickListener(new OnClickListener {
      def onClick(v: View): Unit = showNewShortcutDialog()
    })
  }

  private def redrawShortcuts() {
    val gridView = findViewById(R.id.editShortcutsGridLayout).asInstanceOf[GridLayout]
    gridView.removeAllViews()
    shortcuts map shortcutButton foreach addShortcutButton(gridView)
  }

  private implicit val activity = this

  private def context = this

  private def showNewShortcutDialog() {
    val builder = new AlertDialog.Builder(activity)
    val shortcutTextEdit = new EditText(context)
    builder.setView(shortcutTextEdit)
    builder.setPositiveButton("Ок", new DialogInterface.OnClickListener {
      def onClick(p1: DialogInterface, p2: Int): Unit = {
        saveNewShortcut(shortcutTextEdit.getText.toString)
        redrawShortcuts()
      }
    })
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener {
      def onClick(p1: DialogInterface, p2: Int): Unit = {}
    })
    builder.create().show()
  }

  private def saveNewShortcut(newShortcut: String) {
    saveShortcuts(shortcuts :+ newShortcut)
  }

  private def saveShortcuts(updateShortcuts: Seq[String]) {
    sharedPreferences(activity).edit().putString(GoodsActivity.ShortcutsKey, updateShortcuts.mkString(SplitSymbol)).commit()
  }

  private def addShortcutButton(gridView: GridLayout)(button: View) = gridView addView button

  private def shortcutButton(name: String): ToggleButton = {
    val shortcutButtonView = new ToggleButton(this)
    shortcutButtonView.setClickable(false)
    shortcutButtonView.setText(name)
    shortcutButtonView.setOnLongClickListener(new OnLongClickListener {
      def onLongClick(v: View): Boolean = {
        saveShortcuts(shortcuts.filterNot(_ === name))
        redrawShortcuts()
        true
      }
    })
    shortcutButtonView
  }
}
