name := "hydrangea"

version := "1.0"

scalaVersion := "2.12.3"

lazy val common = project.cross

lazy val common_2_12 = common("2.12.3")

lazy val common_2_11 = common("2.11.11")

lazy val web = project
  .dependsOn(common_2_12)

lazy val core = project
  .dependsOn(common_2_11)

lazy val localcore = project
  .dependsOn(core)

lazy val root = project.in(file(".")).aggregate(common_2_12, common_2_11, web, core, localcore)