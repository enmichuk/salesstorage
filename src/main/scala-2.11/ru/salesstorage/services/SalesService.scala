package ru.salesstorage.services

import ru.salesstorage.entities.Sale
import java.util.Date

import akka.actor.Actor
import ru.salesstorage.dao.sales.SalesDaoService

import scala.util.{Failure, Success}

object SalesService {

  case class GetSalesByPeriodRequest(from: Date, to: Date)
  
  case class GetSalesByShopRequest(shop: List[Int], from: Date, to: Date)
  
  case class GetSalesByShopProductRequest(shop: List[Int], products: List[Int], from: Date, to: Date)
  
  case class GetSalesByShopPriceRequest(shop: List[Int], price_from: Double, price_to: Double, from: Date, to: Date)
  
  case class GetSalesResponse(data: List[Sale])

  case class GetSalesErrorResponse(error: Throwable)
  
}

class SalesService extends Actor{

  import SalesService._

  implicit val ec = context.dispatcher

  override def receive = {

    case GetSalesByPeriodRequest(from, to) =>
      val replyTo = sender()
      SalesDaoService.sales.find(from, to).onComplete{
        case Success(sales) => replyTo ! GetSalesResponse(sales)
        case Failure(error) => replyTo ! GetSalesErrorResponse(error)
      }

    case GetSalesByShopRequest(shop, from, to) =>
      val replyTo = sender()
      SalesDaoService.sales.find(shop, from, to).onComplete{
        case Success(sales) => replyTo ! GetSalesResponse(sales)
        case Failure(error) => replyTo ! GetSalesErrorResponse(error)
      }

    case GetSalesByShopProductRequest(shop, products, from, to) => sender ! GetSalesResponse(Nil)

    case GetSalesByShopPriceRequest(shop, price_from, price_to, from, to) => sender ! GetSalesResponse(Nil)

  }
}