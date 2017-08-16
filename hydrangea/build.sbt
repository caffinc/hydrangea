name := "hydrangea"

version := "1.0"

scalaVersion := "2.12.3"

lazy val common = project

lazy val web = project
  .dependsOn(common)

lazy val core = project
  .dependsOn(common)
