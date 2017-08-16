version := "1.0"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
//  "org.apache.kafka" %% "kafka" % "0.11.0.0",
  "org.apache.spark" %% "spark-core" % "2.2.0", // % "provided",
  "org.apache.spark" %% "spark-streaming" % "2.2.0",  // % "provided",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.2.0", // % "provided",
  "com.typesafe" % "config" % "1.2.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)
