package org.tort.trade.mobile

import scalaz._
import Scalaz._

class NoCGLibGood(val id: String @@ NoCGLibGood.Id, val name: String)

object NoCGLibGood {

  trait Id

  def id(goodId: String): String @@ Id = Tag(goodId)

  def goodsEquality: Equal[NoCGLibGood] = Equal.equal((left, right) => left.id == right.id)
}

