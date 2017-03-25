package ru.salesstorage.dao.phantom.sales

import java.util.Date

import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._
import ru.salesstorage.dao.phantom.Connector
import ru.salesstorage.entities.Sale

import scala.concurrent.Future

class Sales extends CassandraTable[SalesDao, Sale] {
  object shop_id extends IntColumn(this) with PartitionKey[Int]
  object product_id extends IntColumn(this) with ClusteringOrder[Int]
  object sale_date extends DateColumn(this) with ClusteringOrder[Date] with Descending
  object price extends DoubleColumn(this) with ClusteringOrder[Double]
  object sale_id extends IntColumn(this) with ClusteringOrder[Int]
  object product_count extends IntColumn(this)
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

  def create(sales: List[Sale]) = {

    def insertFutureList() = {
      sales.map{
        s =>
          insert
            .value(_.shop_id, s.shop_id)
            .value(_.sale_id, s.sale_id)
            .value(_.sale_date, s.sale_date)
            .value(_.product_id, s.product_id)
            .value(_.product_count, s.product_count)
            .value(_.price, s.price)
            .value(_.category_id, s.category_id)
            .value(_.vendor_id, s.vendor_id)
          .consistencyLevel_=(ConsistencyLevel.ALL)
          .future()
      }
    }

    Future.sequence(insertFutureList())
  }

  def createTable() = {
    autocreate(space).future()
  }
}
