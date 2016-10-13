package ru.salesstorage.dao.spark.sales

import java.util.Date

import com.datastax.spark.connector.toSparkContextFunctions
import ru.salesstorage.AppConfiguration
import ru.salesstorage.dao.spark.SparkConnection
import ru.salesstorage.entities.Sale

object SalesSparkDaoService extends SparkConnection with AppConfiguration{

  private val sales = sc.cassandraTable[Sale](cassandra.keyspace, cassandra.tables.sales)

  def find(shop: List[Int], products: List[Int], from: Date, to: Date) = {
    sales.where(
      "shop_id in ? and sale_date >= ? and sale_date <= ?",
      shop, from, to
    ).collect.toList
  }

  def find(shop: List[Int], from: Date, to: Date) = {
    sales.where(
      "shop_id in ? and sale_date >= ? and sale_date <= ?",
      shop, from, to
    ).collect.toList
  }

  def find(shop: List[Int], price_from: Double, price_to: Double, from: Date, to: Date) = {
    sales.where(
      "shop_id in ? and sale_date >= ? and sale_date <= ? and price >= ? and price <= ?",
      shop, from, to, price_from, price_to
    ).collect.toList
  }

}
