package org.tort.trade.mobile

import scalaz._

class NoCGLibGood(val id: String @@ NoCGLibGood.Id, val name: String)

object NoCGLibGood {

  trait Id

  def id(goodId: String): String @@ Id = Tag(goodId)

  def goodsEquality: Equal[NoCGLibGood] = Equal.equal((left, right) => left.id == right.id)

  def unapply(good: NoCGLibGood): Some[Tuple2[String, String]] = Some((good.id, good.name))

  def tupled(tuple: (String, String)) = new NoCGLibGood(id(tuple._1), tuple._2)
}

