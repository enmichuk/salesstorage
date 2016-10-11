package ru.salesstorage

import akka.actor.ActorSystem
import akka.actor.Props
import scala.io.StdIn
import scala.concurrent.Future
import ru.salesstorage.web.WebEndpoint
import ru.salesstorage.services.SalesService

object SalesStorage extends App{

  implicit val actorSystem = ActorSystem("SalesStorage")
  implicit val ec = actorSystem.dispatcher

  val salesService = actorSystem.actorOf(Props[SalesService], "salesService")

  val endpoint = new WebEndpoint("localhost", 8080, salesService)

  Future {
    println("Press enter to stop server")
    StdIn.readLine()
    endpoint.stopEndpoint.foreach(_ => actorSystem.terminate().foreach(println))
  }

}