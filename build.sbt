name := "play-api"
organization := "com.github.windbird123"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    publishArtifact := false
  )

scalaVersion := "2.13.12"

val tapirVersion = "1.9.6"
val jacksonVersion = "2.16.0"

libraryDependencies ++= Seq(
  guice,
  caffeine,
  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-play-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-play" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test,
  "org.typelevel" %% "cats-core" % "2.10.0",
  "net.logstash.logback" % "logstash-logback-encoder" % "7.4",
  "com.klibisz.futil" %% "futil" % "0.1.2",
  // Enforce Jackson consistency
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-cbor" % jacksonVersion,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % jacksonVersion,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion,
  "com.fasterxml.jackson.module" % "jackson-module-parameter-names" % jacksonVersion,
  "com.fasterxml.jackson.module" % "jackson-module-paranamer" % jacksonVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion
)
