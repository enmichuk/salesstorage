package ru.salesstorage.web.sales

import spray.json._
import ru.salesstorage.entities.Sale
import ru.salesstorage.services.SalesService._
import java.text.SimpleDateFormat
import spray.json.JsString
import spray.json.JsValue
import scala.util.Try
import spray.json.JsonFormat
import java.util.Date

object SalesJsonProtocol extends DefaultJsonProtocol{
  
  implicit object DateFormat extends JsonFormat[Date] {
    def write(date: Date) = JsString(dateToIsoString(date))
    def read(json: JsValue) = json match {
      case JsString(rawDate) =>
        parseIsoDateString(rawDate)
          .fold(deserializationError(s"Expected date format, got $rawDate"))(identity)
      case error => deserializationError(s"Expected string, got $error")
    }
  }
  private val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {
    override def initialValue() = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
  }
  private def dateToIsoString(date: Date) =
    localIsoDateFormatter.get().format(date)
  private def parseIsoDateString(date: String): Option[Date] =
    Try{ localIsoDateFormatter.get().parse(date) }.toOption

  implicit val saleFormat = jsonFormat8(Sale)
  implicit val getSalesByPeriodRequestFormat = jsonFormat2(GetSalesByPeriodRequest)
  implicit val getSalesByShopRequestFormat = jsonFormat3(GetSalesByShopRequest)
  implicit val getSalesByShopProductRequestFormat = jsonFormat4(GetSalesByShopProductRequest)
  implicit val getSalesByShopPriceRequestFormat = jsonFormat5(GetSalesByShopPriceRequest)
  implicit val getSalesResponseFormat = jsonFormat1(GetSalesResponse)

}