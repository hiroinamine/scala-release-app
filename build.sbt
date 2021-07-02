import java.io.PrintWriter

import Dependencies._
import sbtrelease.ReleaseStateTransformations._

ThisBuild / scalaVersion := "2.13.5"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, BuildInfoPlugin, DockerPlugin)
  .settings(
    name := "scala-release-app",
    buildInfoOptions += BuildInfoOption.ToJson,
    libraryDependencies += scalaTest % Test
  )

/** Docker configuration */
Docker / dockerRepository := Some("99b2b")
//val env = sys.env("env")
//env match {
//  case "develop" => Docker / version := "SNAPSHOT"
//  case _ => Docker / version := version.value
//}
Docker / version := {
  if (version.value.trim.endsWith("SNAPSHOT"))
    "SNAPSHOT"
  else
    version.value
}

//dockerBaseImage := "dockering/oracle-java8"
//dockerExposedPorts := Seq(9000)
//dockerExposedVolumes := Seq("/opt/docker/logs")
//dockerCommands += ExecCmd("CMD", "-Dconfig.resource=production.conf")

/** Release configuration */
val writeReleaseFile = taskKey[Unit]("Write release version task.")
writeReleaseFile := {
  val s = new PrintWriter("release-version")
  s.print(version.value)
  s.close()
}
//releaseUseGlobalVersion := false
//releaseIgnoreUntrackedFiles := true
//releaseVersion := { ver =>
//  Version(ver).map(_.withoutQualifier.string).getOrElse(versionFormatError(ver))
//}
//releaseNextVersion := { ver =>
//  Version(ver).map(_.bumpNano.string).getOrElse(versionFormatError(ver))
//}
releaseTagName := {
  s"${version.value}"
}
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies, // : ReleaseStep
  inquireVersions, // : ReleaseStep
  //  runClean,                               // : ReleaseStep
  //  runTest,                                // : ReleaseStep
  setReleaseVersion, // : ReleaseStep
  commitReleaseVersion, // : ReleaseStep, performs the initial git checks
  tagRelease, // : ReleaseStep
  ReleaseStep(releaseStepTask(This / writeReleaseFile)),
  ReleaseStep(releaseStepTask(Docker / publish)),
  //  publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion, // : ReleaseStep
  commitNextVersion // : ReleaseStep
  //  pushChanges                             // : ReleaseStep, also checks that an upstream branch is properly configured
)
