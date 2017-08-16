version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  guice,
  "org.apache.kafka" %% "kafka" % "0.11.0.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

lazy val web = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator

fork := true
