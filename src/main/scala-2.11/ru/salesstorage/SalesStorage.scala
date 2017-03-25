package ru.salesstorage

import akka.actor.{ActorSystem, Props}
import ru.salesstorage.services.SalesService
import ru.salesstorage.web.WebEndpoint

import scala.concurrent.Future
import scala.io.StdIn

object SalesStorage extends App{

  implicit val actorSystem = ActorSystem("SalesStorage")
  implicit val ec = actorSystem.dispatcher

  val salesService = actorSystem.actorOf(Props[SalesService], "salesService")

  val endpoint = new WebEndpoint("localhost", 8080, salesService)

  Future {
    StdIn.readLine()
    endpoint.stopEndpoint.foreach(_ => actorSystem.terminate().foreach(println))
  }

}