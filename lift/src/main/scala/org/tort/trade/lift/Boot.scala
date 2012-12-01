package org.tort.trade.lift

class Boot {
  def boot {
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
  }
}
