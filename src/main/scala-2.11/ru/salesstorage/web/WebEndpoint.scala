package ru.salesstorage.web

import akka.stream.ActorMaterializer
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.http.scaladsl.Http
import akka.actor.ActorRef
import scala.concurrent.Future
import akka.http.scaladsl.Http.ServerBinding
import ru.salesstorage.web.sales.SalesRoute
import scala.concurrent.duration._

class WebEndpoint(
                   host: String,
                   port: Int,
                   val salesService: ActorRef 
                 )
                 (implicit actorSystem: ActorSystem) extends SalesRoute{

  implicit val timeout = Timeout(5 seconds)
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = actorSystem.dispatcher

  private val binding: Future[ServerBinding] =
    Http().bindAndHandle(salesRoute, host, port)

  def stopEndpoint: Future[Unit] = binding.flatMap(_.unbind())
}