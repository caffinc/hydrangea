version := "1.0"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  guice
)

lazy val web = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator

fork := true
