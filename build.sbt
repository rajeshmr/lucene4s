import sbtcrossproject.CrossPlugin.autoImport.crossProject

name in ThisBuild := "lucene4s"
organization in ThisBuild := "com.outr"
version in ThisBuild := "1.9.0-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.8"
crossScalaVersions in ThisBuild := List("2.12.8", "2.11.12")
parallelExecution in Test in ThisBuild := false
scalacOptions ++= Seq("-unchecked", "-deprecation")

publishTo in ThisBuild := sonatypePublishTo.value
sonatypeProfileName in ThisBuild := "com.outr"
publishMavenStyle in ThisBuild := true
licenses in ThisBuild := Seq("MIT" -> url("https://github.com/outr/lucene4s/blob/master/LICENSE"))
sonatypeProjectHosting in ThisBuild := Some(xerial.sbt.Sonatype.GitHubHosting("outr", "lucene4s", "matt@outr.com"))
homepage in ThisBuild := Some(url("https://github.com/outr/lucene4s"))
scmInfo in ThisBuild := Some(
  ScmInfo(
    url("https://github.com/outr/lucene4s"),
    "scm:git@github.com:outr/lucene4s.git"
  )
)
developers in ThisBuild := List(
  Developer(id="darkfrog", name="Matt Hicks", email="matt@matthicks.com", url=url("http://matthicks.com"))
)
testOptions in Test in ThisBuild += Tests.Argument("-oD")

val luceneVersion = "7.7.1"
val powerScalaVersion = "2.0.5"
val squantsVersion = "1.3.0"

val scalaTestVersion = "3.0.5"
val scalacticVersion = "3.0.5"

lazy val root = project.in(file("."))
  .aggregate(coreJS, coreJVM, implementation)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .in(file("core"))
  .settings(
    name := "lucene4s-core"
  )

lazy val coreJS = core.js
lazy val coreJVM = core.jvm

lazy val implementation = project
  .in(file("implementation"))
  .settings(
    name := "lucene4s",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "org.apache.lucene" % "lucene-core" % luceneVersion,
      "org.apache.lucene" % "lucene-analyzers-common" % luceneVersion,
      "org.apache.lucene" % "lucene-queryparser" % luceneVersion,
      "org.apache.lucene" % "lucene-facet" % luceneVersion,
      "org.apache.lucene" % "lucene-highlighter" % luceneVersion,
      "org.powerscala" %% "powerscala-io" % powerScalaVersion,
      "org.typelevel" %% "squants" % squantsVersion,
      "org.scalactic" %% "scalactic" % scalaTestVersion % "test",
      "org.scalatest" %% "scalatest" % scalacticVersion % "test"
    ),
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oF")
  )
  .dependsOn(coreJVM)

