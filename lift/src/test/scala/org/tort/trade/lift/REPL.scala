package org.tort.trade.lift

import tools.nsc.MainGenericRunner

object REPL {
  override def main(args: Array[String]) {
    val b = new Boot()

    b.boot

    MainGenericRunner.main(args)

    sys.exit(0)
  }
}
