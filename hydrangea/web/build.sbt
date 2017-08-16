version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  guice,
  "org.apache.kafka" %% "kafka" % "0.11.0.0"
    exclude("org.slf4j", "*")
    exclude("log4j", "log4j")
    exclude("org.json4s", "*"),
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.slf4j" % "log4j-over-slf4j" % "1.7.25",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.25"
)

lazy val web = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator

fork := true
