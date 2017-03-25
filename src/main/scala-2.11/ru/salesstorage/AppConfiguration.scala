package ru.salesstorage

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._

trait AppConfiguration {

  private val config = ConfigFactory.load()
  private val cassandraConfig = config.getConfig("cassandra")
  object cassandra {
    val hosts = cassandraConfig.getStringList("hosts").asScala
    val port = cassandraConfig.getInt("port")
    val keyspace = cassandraConfig.getString("keyspace")
    object tables {
      private val tables = cassandraConfig.getConfig("tables")
      val sales = tables.getString("sales")
    }
  }

}
