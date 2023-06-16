name := "play-api"
organization := "com.github.windbird123"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    publishArtifact := false
  )

publishTo := Some(Resolver.file("Unused transient repository", file("target/unusedrepo")))

scalaVersion := "2.13.11"

val tapirVersion = "1.5.5"

libraryDependencies ++= Seq(
  guice,
  "org.typelevel" %% "cats-core" % "2.9.0",
  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-play-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-play" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
  "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.4.0",
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui" % tapirVersion,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
  "com.softwaremill.sttp.shared" %% "akka" % "1.3.15"
)
