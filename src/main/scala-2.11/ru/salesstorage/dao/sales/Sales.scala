package ru.salesstorage.dao.sales

import java.util.Date

import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._
import ru.salesstorage.dao.Connector
import ru.salesstorage.entities.Sale

import scala.concurrent.Future

class Sales extends CassandraTable[SalesDao, Sale] {
  object shop_id extends IntColumn(this) with PartitionKey[Int]
  object sale_date extends DateColumn(this) with ClusteringOrder[Date] with Descending
  object sale_id extends IntColumn(this) with ClusteringOrder[Int]
  object product_id extends IntColumn(this)
  object product_count extends IntColumn(this)
  object price extends DoubleColumn(this)
  object category_id extends IntColumn(this)
  object vendor_id extends IntColumn(this)

  override def fromRow(row: Row): Sale = {
    Sale(
      shop_id = shop_id(row),
      sale_id = sale_id(row),
      sale_date = sale_date(row),
      product_id = product_id(row),
      product_count = product_count(row),
      price = price(row),
      category_id = category_id(row),
      vendor_id = vendor_id(row)
    )
  }
}

abstract class SalesDao extends Sales with Connector {
  def find(from: Date, to: Date): Future[List[Sale]] = {
    select
      .where(_.sale_date gte from)
      .and(_.sale_date lt to)
      .allowFiltering().fetch()
  }
  def find(shop_ids: List[Int], from: Date, to: Date): Future[List[Sale]] = {
    select
      .where(_.shop_id in shop_ids)
      .and(_.sale_date gte from)
      .and(_.sale_date lt to)
      .fetch()
  }
}
