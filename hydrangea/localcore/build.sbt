version := "1.0"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0"
    exclude("org.slf4j", "*")
    exclude("log4j", "log4j")
    exclude("org.json4s", "*")
    exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "org.apache.spark" %% "spark-streaming" % "2.2.0"
    exclude("org.slf4j", "*")
    exclude("log4j", "log4j")
    exclude("org.json4s", "*")
    exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.2.0"
    exclude("org.slf4j", "*")
    exclude("log4j", "log4j")
    exclude("org.json4s", "*")
    exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "org.codehaus.groovy" % "groovy" % "2.6.0-alpha-1",
  "com.typesafe" % "config" % "1.2.1" exclude("org.slf4j", "*"),
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.25",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.25",
  "org.json4s" %% "json4s-jackson" % "3.5.3"
    exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "org.json4s" %% "json4s-mongo" % "3.5.3",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0"
)


assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}