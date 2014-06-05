import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin._

object Build extends sbt.Build {

  object Dependencies {
    lazy val specs2 = "org.specs2" %% "specs2" % "2.3.12"
  }

  lazy val root = Project(
    id = "dependency-utils",
    base = file("."),
    settings = Project.defaultSettings ++ releaseSettings ++ Seq(
      name := "dependency-utils",
      organization := "com.ahum",
      scalaVersion := "2.10.3",
      libraryDependencies ++= Seq(Dependencies.specs2 % "test"),
      publishMavenStyle := true,
      publishTo <<= version {
        (v: String) =>
          def isSnapshot = v.trim.contains("-")
          val finalPath = (if (isSnapshot) "/snapshots" else "/releases")
          Some(
            Resolver.sftp(
              "Ed Eustace",
              "edeustace.com",
              "/home/edeustace/edeustace.com/public/repository" + finalPath))
      }
    )
  )
}