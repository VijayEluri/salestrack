package org.tort.trade.lift

import java.text.SimpleDateFormat
import java.util.Date

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.{Bootable, InternalServerErrorResponse, LiftRules}
import net.liftweb.json.Extraction
import net.liftweb.json.JsonAST._
import org.tort.trade.mobile.{NoCGLibGood, NoCGLibSale, NoCGLibTransition}
import org.tort.trade.service.{DBHelper, Service}

import scala.util.{Failure, Success, Try}
import scalaz.Scalaz._

class Boot extends Bootable {
  def boot {
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
    LiftRules.statelessDispatch.append(JournalMapping)
  }
}

object JournalMapping extends RestHelper with DBHelper {

  initDB (db => initHandling(new Service(db)))

  def initHandling(service: Service) {
    serve {
      case JsonPost("sync-transitions" :: Nil, json -> _) => {
        Try( deserializeTransitions(json) ) match {
          case Success(trs) =>
            trs.map(syncTransition(service)) |> Extraction.decompose |> fixStatuses
          case Failure(ex) => {
            println(ex)
            InternalServerErrorResponse()
          }
        }
      }
      case JsonGet("goods" :: Nil, req) =>
        req.params.get("substr").map {
          case List(substr) =>
            val list = substr.split(" ").toList
            service.goodsBy(list).map(good => GoodDTO(good.id, good.name)) |> Extraction.decompose
        }
      case JsonGet("compareJournals" :: Nil, req) =>
        service.matchJournals.map(x => SuspiciousTransitionDTO(x.from, x.to, x.date.getTime, x.good, x.quant.toString)) |> Extraction.decompose
      case JsonReq("test1" :: Nil, req) => {
        req.toString
        println(req._1.method)
        println(req._2.json_?)
        println(req._2.json.isDefined)
        JString(req._1.method)
      }
    }
  }

  val SellOnePattern = """(\d+)""".r
  val SellPattern = """(\d+)\*(\d+)""".r
  val IncomePattern = """!(\d+)\*(\d+)""".r
  val GiveTransferPattern = """([a-zA-Zа-яА-Я])\*(\d+)?""".r
  val ReceiveTransferPattern = """!([a-zA-Zа-яА-Я])\*(\d+)?""".r

  object Status {
    val Error = "Error"
    var Synchronized = "Synchronized"
  }

  val saleByAlias = Map("H" -> 5, "Е" -> 6, "К" -> 7, "В" -> 8, "Т" -> 9, "С" -> 10)

  def syncTransition(service:Service)(dto: TransitionDTO): TransitionDTO = {
    val transition = dto.formula match {
      case SellOnePattern(price) =>
        Some(sellTransition(price.toInt, 1, dto))
      case SellPattern(number, price) =>
        Some(sellTransition(price.toInt, number.toInt, dto))
      case IncomePattern(price, number) =>
        Some(incomeTransition(price.toInt, number.toInt, dto))
      case ReceiveTransferPattern(from, number) =>
        val me = dto.me.salesId |> NoCGLibSale.saleId
        Some(NoCGLibTransition(
          saleByAlias(from).toString |> NoCGLibSale.saleId,
          me,
          Option(number.toLong).getOrElse(1L) |> NoCGLibTransition.quantity,
          None,
          new Date(dto.date),
          me,
          dto.good.id |> NoCGLibGood.id
        ))
      case GiveTransferPattern(to, number) =>
        val me = dto.me.salesId |> NoCGLibSale.saleId
        Some(NoCGLibTransition(
          me,
          saleByAlias(to).toString |> NoCGLibSale.saleId,
          Option(number.toLong).getOrElse(1L) |> NoCGLibTransition.quantity,
          None,
          new Date(dto.date),
          me,
          dto.good.id |> NoCGLibGood.id
        ))
      case _ => None
    }

    transition.flatMap(sync(service)) match {
      case Some(t) =>
        println(dto.date)
        println(new Date(dto.date))
        dto.copy(status = Status.Synchronized)
      case None =>
        dto.copy(status = Status.Error)
    }
  }

  def sync(service: Service)(t: NoCGLibTransition) = {
    Try(service.addTransition(t)) match {
      case Success(_) => Some(t)
      case Failure(ex) =>
        println(ex)
        None
    }
  }

  def incomeTransition(price: Long, quant: Int, t: TransitionDTO) = {
    val me = NoCGLibSale.saleId(t.me.salesId)
    NoCGLibTransition(
      NoCGLibSale.SupplierSaleId,
      me,
      NoCGLibTransition.quantity(quant),
      NoCGLibTransition.price(Some(price)),
      new Date(t.date),
      me,
      NoCGLibGood.id(t.good.id)
    )
  }

  def sellTransition(price: Long, quant: Int, t: TransitionDTO): NoCGLibTransition = {
    val me = NoCGLibSale.saleId(t.me.salesId)
    NoCGLibTransition(
      me,
      NoCGLibSale.CustomerSaleId,
      NoCGLibTransition.quantity(quant),
      NoCGLibTransition.price(Some(price)),
      new Date(t.date),
      me,
      NoCGLibGood.id(t.good.id)
    )
  }

  def persistOperation(t: NoCGLibTransition) {

  }
  
  def deserializeTransitions(json: JValue): Seq[TransitionDTO] = {
    json.map {
      case JField("status", JObject(List(JField("instance", JString("ReadyToSync"))))) => JField("status", JString("ReadyToSync"))
      case x => x
    }.extract[Array[TransitionDTO]]
  }

  def fixStatuses(json: JValue): JValue = {
    json.map {
      case JField("status", JString(status)) => JField("status", JObject(List(JField("instance", JString(status)))))
      case x =>
        println(x)
        x
    }
  }

  def filterOuter(json: JValue): JValue = json.remove {
    case JField("driver", _) => true
    case _ => false
  }
}

case class TransitionDTO(lid: String, good: GoodDTO, me: SalesDTO, formula: String, date: Long, status: String)
case class SalesDTO(salesId: String, salesName: String)
case class GoodDTO(instance: String, id: String, name: String)
case class SuspiciousTransitionDTO(instance: String, from: String, to: String, date: Long, renderedDate: String, good: String, quant: String)
object GoodDTO {
  def apply(id: String, name: String): GoodDTO = GoodDTO("Good", id, name)
}
object SuspiciousTransitionDTO {
  def apply(from: String, to: String, date: Long, good: String, quant: String): SuspiciousTransitionDTO = SuspiciousTransitionDTO(
    "Transition",
    from = from,
    to = to,
    date = date,
    renderedDate = new SimpleDateFormat("dd-MM-yyyy").format(date),
    good = good,
    quant = quant
  )
}
