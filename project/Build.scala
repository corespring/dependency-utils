import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin._

object Build extends sbt.Build {

  object Dependencies {
    val specs2 = "org.specs2" %% "specs2" % "2.3.12"
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
  }

  import Dependencies._

  lazy val root = Project(
    id = "dependency-utils",
    base = file("."),
    settings = Project.defaultSettings ++ releaseSettings ++ Seq(
      name := "dependency-utils",
      organization := "org.corespring",
      scalaVersion := "2.10.3",
      libraryDependencies ++= Seq(specs2 % "test", scalaLogging),
      credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
      publishTo <<= version {
        (v: String) =>
          def isSnapshot = v.trim.contains("-")
          val base = "http://repository.corespring.org/artifactory"
          val repoType = if (isSnapshot) "snapshot" else "release"
          val finalPath = base + "/ivy-" + repoType + "s"
          Some("Artifactory Realm" at finalPath)
      }
    )
  )
}