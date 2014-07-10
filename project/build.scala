import sbt._
import Keys._
import android.Keys._
import xerial.sbt.Pack._

object SalesTrack extends Build {
  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = buildSettings ++ packSettings ++ Seq(
      packMain := Map("dbreplicator" -> "com.tort.trade.replicator.Runner")
    )
  ) aggregate(model, android, replicator)

  lazy val model = Project(
    id = "model",
    base = file("model"),
    settings = buildSettings ++ dbCommons
  )

  lazy val android = Project(
    id = "android",
    base = file("android"),
    settings = buildSettings ++ AndroidSettings.full
  ) dependsOn (model)

  lazy val replicator = Project(
    id = "dbreplicator",
    base = file("dbreplicator"),
    settings = buildSettings ++ Seq(
      libraryDependencies += "com.typesafe.slick" %% "slick-extensions" % "2.0.2",
      libraryDependencies += "com.h2database" % "h2" % "1.3.168",
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"
    )
  ) dependsOn (model)

  val buildSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.4",
    libraryDependencies += "org.scalaz" % "scalaz-core_2.10" % "7.0.5"
  )

  val dbCommons = Seq(
      libraryDependencies += "com.typesafe.slick" %% "slick" % "2.0.2",
      libraryDependencies += "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.3"
    )
}

object AndroidSettings {
  val settings = Defaults.defaultSettings ++ Seq(
    name := "android",
    version := "0.1"
  )

  val proguardSettings = Seq(
    proguardOptions in Android := Seq("-keep class scala.collection.SeqLike {public protected *;}")
  )

  lazy val full =
    AndroidSettings.settings ++
      proguardSettings ++
      Seq(
      libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
      libraryDependencies += "com.typesafe.slick" %% "slick-extensions" % "2.0.2"
    ) ++ android.Plugin.androidBuild ++ Seq(platformTarget in Android := "android-16")
}

