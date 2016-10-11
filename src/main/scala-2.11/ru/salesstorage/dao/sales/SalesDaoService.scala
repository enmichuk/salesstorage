package ru.salesstorage.dao.sales

import com.websudos.phantom.connectors.KeySpaceDef
import com.websudos.phantom.dsl.Database
import ru.salesstorage.dao.CassandraConnection

object SalesDaoService extends SalesDaoService(CassandraConnection.manager)

abstract class SalesDaoService(val keyspace: KeySpaceDef) extends Database(keyspace) {
  object sales extends SalesDao with keyspace.Connector
}