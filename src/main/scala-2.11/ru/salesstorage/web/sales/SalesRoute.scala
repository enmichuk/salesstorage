package ru.salesstorage.web.sales

import ru.salesstorage.web.SalesStorageRoute
import ru.salesstorage.services.SalesService._
import akka.actor.ActorRef
import akka.pattern.ask
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import SalesJsonProtocol._
import akka.http.scaladsl.server.StandardRoute
import spray.json._

import scala.util.{Failure, Success, Try}

trait SalesRoute extends SalesStorageRoute {

  def salesService: ActorRef

  val processResponse: PartialFunction[Try[Any], StandardRoute] = {
    case Success(GetSalesResponse(sales)) =>
      complete(sales)
    case Success(GetSalesErrorResponse(error)) =>
      complete(error.getMessage)
    case Failure(error)                   =>
      complete(StatusCodes.InternalServerError)
  }

  val salesRoute =
    pathPrefix("test") {
      path("get-sales-by-period") {
        post {
          entity(as[GetSalesByPeriodRequest]) { request =>
            onComplete(salesService ? request) (processResponse)
          }
        }
      } ~ 
      path("get-sales-by-shop") {
        post {
          entity(as[GetSalesByShopRequest]) { request =>
            onComplete(salesService ? request) (processResponse)
          }
        }
      } ~ 
      path("get-sales-by-shop-product") {
        post {
          entity(as[GetSalesByShopProductRequest]) { request =>
            onComplete(salesService ? request) (processResponse)
          }
        }
      } ~ 
      path("get-sales-by-shop-price") {
        post {
          entity(as[GetSalesByShopPriceRequest]) { request =>
            onComplete(salesService ? request) (processResponse)
          }
        }
      }
    }

}