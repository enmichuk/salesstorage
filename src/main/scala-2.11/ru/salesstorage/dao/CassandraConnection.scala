package ru.salesstorage.dao

import java.net.InetAddress
import java.security.SecureRandom
import javax.net.ssl.{SSLContext, TrustManager}

import com.datastax.driver.core.Cluster.Builder
import com.datastax.driver.core.{Cluster, PlainTextAuthProvider, SSLOptions, Session}
import com.typesafe.config.ConfigFactory
import com.websudos.phantom.connectors.{KeySpaceDef, SessionProvider}
import com.websudos.phantom.dsl._

import scala.collection.JavaConverters._

trait Connector {
  implicit def space: KeySpace
  implicit def session: Session
}

trait AppConfiguration {
  val config = ConfigFactory.load()
}

object CassandraConnection extends AppConfiguration {

  val keyspace = config.getString("cassandra.keyspace")
  val hosts = config.getStringList("cassandra.hosts").asScala

  val manager = ContactPoints(hosts).keySpace(keyspace)

}
