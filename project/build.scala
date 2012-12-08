import sbt._
import Keys._
import AndroidKeys._

object SalesTrack extends Build {
  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = buildSettings
  ) aggregate(model, android, replicator)

  lazy val model = Project(
    id = "model",
    base = file("model"),
    settings = buildSettings
  )

  lazy val android = Project(
    id = "android",
    base = file("android"),
    settings = buildSettings ++ General.fullAndroidSettings
  ) dependsOn (model)

  lazy val replicator = Project(
    id = "dbreplicator",
    base = file("dbreplicator"),
    settings = buildSettings
  ) dependsOn (model)

  val buildSettings = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.9.2"
  )
}

object General {
  val settings = Defaults.defaultSettings ++ Seq (
    name := "android",
    version := "0.1",
    versionCode := 0,
    scalaVersion := "2.9.2",
    platformName in Android := "android-16"
  )

  val proguardSettings = Seq (
    useProguard in Android := true
  )

  lazy val fullAndroidSettings =
    General.settings ++
      AndroidProject.androidSettings ++
      TypedResources.settings ++
      proguardSettings ++
      AndroidManifestGenerator.settings ++
      AndroidMarketPublish.settings ++ Seq (
      keyalias in Android := "change-me",
      libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test"
    )
}

