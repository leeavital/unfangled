import AssemblyKeys._

assemblySettings

name := "unfangled"

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.25.0",
  "org.scalatra.scalate" % "scalate-core_2.11" % "1.7.1",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "junit" % "junit" % "4.11" % "test"
)

scalaVersion := "2.11.6"

scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-language:implicitConversions",
    "-Xlint",
    "-Xfatal-warnings",
    "-Ywarn-dead-code",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfuture")

version := "0.1.0"
