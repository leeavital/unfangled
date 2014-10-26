libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.2.0",
  "org.fusesource.scalate" %% "scalate-core" % "1.6.1",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "junit" % "junit" % "4.11" % "test"
)

scalaVersion := "2.10.4"

scalacOptions ++= Seq("-feature", "-deprecation", "-language:implicitConversions")
