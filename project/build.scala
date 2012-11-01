import sbt._
import Keys._

object SalesTrack extends Build {
  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = buildSettings
  ) aggregate(model, webapp, replicator)

  lazy val model = Project(
    id = "model",
    base = file("model"),
    settings = buildSettings
  )

  lazy val webapp = Project(
    id = "webapp",
    base = file("webapp"),
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
