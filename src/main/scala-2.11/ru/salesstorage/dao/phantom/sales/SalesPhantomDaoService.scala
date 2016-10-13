package ru.salesstorage.dao.phantom.sales

import com.websudos.phantom.connectors.KeySpaceDef
import com.websudos.phantom.dsl.Database
import ru.salesstorage.dao.phantom.CassandraConnection

object SalesPhantomDaoService extends SalesPhantomDaoService(CassandraConnection.manager)

abstract class SalesPhantomDaoService(val keyspace: KeySpaceDef) extends Database(keyspace) {
  object sales extends SalesDao with keyspace.Connector
}