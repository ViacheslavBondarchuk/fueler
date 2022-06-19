ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.0"

lazy val akkaVersion = "2.6.19"
lazy val akkaConfigVersion = "1.4.2"
lazy val telegramCoreVersion = "5.4.2"
lazy val slf4SimpleVersion = "1.7.36"
lazy val slickVersion = "3.3.3"
lazy val postgresVersion = "42.4.0"

lazy val root = (project in file("."))
  .settings(
    name := "fueler",
    resolvers ++= Seq(Resolver.mavenLocal, Resolver.mavenCentral),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe" % "config" % akkaConfigVersion,
      "com.bot4s" %% "telegram-core" % telegramCoreVersion,
      "com.bot4s" %% "telegram-akka" % telegramCoreVersion,
      "org.slf4j" % "slf4j-simple" % slf4SimpleVersion,
      "com.typesafe.slick" %% "slick" % slickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
      "org.postgresql" % "postgresql" % postgresVersion
    )
  )
