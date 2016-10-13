package ru.salesstorage.services

import java.text.SimpleDateFormat

import ru.salesstorage.entities.Sale
import java.util.Date

import akka.actor.{Actor, ActorLogging, ActorRef}
import ru.salesstorage.dao.phantom.sales.SalesPhantomDaoService
import ru.salesstorage.dao.spark.sales.SalesSparkDaoService

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object SalesService {

  object CreateSales

  case class GetSalesByPeriodRequest(from: Date, to: Date)
  
  case class GetSalesByShopRequest(shop: List[Int], from: Date, to: Date)
  
  case class GetSalesByShopProductRequest(shop: List[Int], products: List[Int], from: Date, to: Date)
  
  case class GetSalesByShopPriceRequest(shop: List[Int], price_from: Double, price_to: Double, from: Date, to: Date)
  
  case class GetSalesResponse(data: Seq[Sale])

  case class GetSalesErrorResponse(error: Throwable)
  
}

class SalesService extends Actor with ActorLogging{

  import SalesService._

  implicit val ec = context.dispatcher

  override def preStart() = {
    SalesPhantomDaoService.sales.createTable().onComplete {
      case Success(_) => self ! CreateSales
      case Failure(error) =>
      log.error(error, "Error auto creating sales table in cassandra")
    }
  }

  override def receive = {
    case GetSalesByPeriodRequest(from, to) =>
      val replyTo = sender()
      SalesPhantomDaoService.sales.find(from, to)
        .onComplete(processFind(replyTo, _))

    case GetSalesByShopRequest(shop, from, to) =>
      val replyTo = sender()
      Future(SalesSparkDaoService.find(shop, from, to))
        .onComplete(processFind(replyTo, _))

    case GetSalesByShopProductRequest(shop, products, from, to) =>
      val replyTo = sender()
      Future(SalesSparkDaoService.find(shop, products, from, to))
        .onComplete(processFind(replyTo, _))

    case GetSalesByShopPriceRequest(shop, price_from, price_to, from, to) =>
      val replyTo = sender()
      Future(SalesSparkDaoService.find(shop, price_from, price_to, from, to))
        .onComplete(processFind(replyTo, _))

    case CreateSales =>
      val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
      val sales = (for(i <- 1 to 9) yield  {
        Sale(
          shop_id = i, sale_id = i, sale_date = simpleDateFormat.parse("2016-01-0" + i),
          product_id = i, product_count = i, price = i, category_id = i, vendor_id = i
        )
      }).toList
      SalesPhantomDaoService.sales.create(sales)
  }

  def processFind(replyTo: ActorRef, find: Try[Seq[Sale]]) = {
    find match {
      case Success(sales) => replyTo ! GetSalesResponse(sales)
      case Failure(error) =>
        log.error(error, "Error while getting sales from cassandra")
        replyTo ! GetSalesErrorResponse(error)
    }
  }

}