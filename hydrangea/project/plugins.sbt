logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.3")

// Cross Builds
addSbtPlugin("com.lucidchart" % "sbt-cross" % "3.2")