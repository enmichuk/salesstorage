package ru.salesstorage.dao.phantom

import com.datastax.driver.core.Session
import com.typesafe.config.ConfigFactory
import com.websudos.phantom.dsl._
import ru.salesstorage.AppConfiguration

import scala.collection.JavaConverters._

trait Connector {
  implicit def space: KeySpace
  implicit def session: Session
}

object CassandraConnection extends AppConfiguration {

  val manager = ContactPoint.local.keySpace(cassandra.keyspace)

}
