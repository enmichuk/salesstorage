package ru.salesstorage

import java.text.SimpleDateFormat

import com.datastax.driver.core.{Cluster, Session}
import grizzled.slf4j.Logging
import org.scalatest._
import org.scalatest.concurrent.Eventually
import ru.salesstorage.dao.phantom.sales.SalesPhantomDaoService
import ru.salesstorage.entities.Sale

import scala.concurrent.Await
import scala.concurrent.duration._

class SalesStorageIntegrationTest extends FlatSpec with Eventually with Matchers with Logging
  with BeforeAndAfterAllConfigMap with AppConfiguration {

  private var cluster: Cluster = _
  private var connection: Session = _

  override def beforeAll(configMap: ConfigMap): Unit = {
    debug("Start initialize environment")

    cluster = Cluster.builder().addContactPoints(cassandra.hosts: _*).withPort(cassandra.port).build()
    connection = cluster.connect()

    createTable()
    createSales()

    debug("Environment is initialized")
  }

  override def afterAll(configMap: ConfigMap): Unit = {
    debug("Start release resources")

    dropKeyspace()

    connection.close()
    cluster.close()

    debug("Resources released")
  }

  "SalesStorageIntegrationTest" should "work correctly" in {

  }

  private def createTable(): Unit = {
    Await.ready(SalesPhantomDaoService.sales.createTable(), 5 seconds)
  }

  private def createSales(): Unit = {
    val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    val sales = (for (i <- 1 to 9) yield {
      Sale(
        shop_id = i, sale_id = i, sale_date = simpleDateFormat.parse("2016-01-0" + i),
        product_id = i, product_count = i, price = i, category_id = i, vendor_id = i
      )
    }).toList
    SalesPhantomDaoService.sales.create(sales)
  }

  private def dropKeyspace(): Unit = {
    connection.execute(s"DROP KEYSPACE ${cassandra.keyspace}")
  }

}