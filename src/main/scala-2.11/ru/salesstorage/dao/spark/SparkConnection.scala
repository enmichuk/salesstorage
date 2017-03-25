package ru.salesstorage.dao.spark

import org.apache.spark.{SparkConf, SparkContext}
import ru.salesstorage.AppConfiguration

trait SparkConnection extends AppConfiguration{

  private val conf = new SparkConf(true)
    .set("spark.cassandra.connection.host", cassandra.hosts.mkString(","))
    .setMaster("local[2]")
  val sc = new SparkContext("local", "Test", conf)

}
