import sbt._
import Keys._

object SalesTrack extends Build {
  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = buildSettings
  ) aggregate(model, lift, replicator)

  lazy val model = Project(
    id = "model",
    base = file("model"),
    settings = buildSettings
  )

  lazy val lift = Project(
    id = "lift",
    base = file("lift"),
    settings = buildSettings
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
