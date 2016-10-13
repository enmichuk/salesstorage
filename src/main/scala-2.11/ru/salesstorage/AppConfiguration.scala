package ru.salesstorage

import com.typesafe.config.ConfigFactory
import ru.salesstorage.dao.phantom.CassandraConnection._

trait AppConfiguration {

  private val config = ConfigFactory.load()
  private val cassandraConfig = config.getConfig("cassandra")
  object cassandra {
    val keyspace = cassandraConfig.getString("keyspace")
    val host = cassandraConfig.getString("host")
    object tables {
      private val tables = cassandraConfig.getConfig("tables")
      val sales = tables.getString("sales")
    }
  }

}
