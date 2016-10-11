package ru.salesstorage.web

import akka.actor.ActorRef
import akka.util.Timeout

trait SalesStorageRoute {
  
  implicit def timeout: Timeout

}