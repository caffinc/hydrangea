version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  guice,
  "org.apache.kafka" %% "kafka" % "0.11.0.0"
)

lazy val web = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator

fork := true
