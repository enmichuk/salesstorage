package ru.salesstorage.web

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.util.Timeout
import ru.salesstorage.web.sales.SalesRoute

import scala.concurrent.Future
import scala.concurrent.duration._

class WebEndpoint(
                   host: String,
                   port: Int,
                   val salesService: ActorRef
                 )(implicit actorSystem: ActorSystem) extends SalesRoute{

  implicit val timeout = Timeout(10 seconds)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher
  def logger = actorSystem.log

  private val binding: Future[ServerBinding] = Http().bindAndHandle(salesRoute, host, port)

  def stopEndpoint: Future[Unit] = binding.flatMap(_.unbind())

}