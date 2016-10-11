name := "SalesStorage"

version := "1.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  Resolver.bintrayRepo("websudos", "oss-releases"),
  "Typesafe repository releases"     at "http://repo.typesafe.com/typesafe/releases/",
  "Websudos"                         at "https://dl.bintray.com/websudos/oss-releases/"
)

val phantomV = "1.29.3"
val akkaV = "2.4.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-jackson-experimental" % akkaV,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
  "com.websudos" %% "phantom-dsl" % phantomV,
  "com.websudos" %% "phantom-connectors" % phantomV,
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.1",
  "com.typesafe" % "config" % "1.3.1"
)
    