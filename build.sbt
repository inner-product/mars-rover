name := "essential-exercises"

Global / onChangedBuildSource := ReloadOnSourceChanges
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / useSuperShell := false

// ScalaFix configuration
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.5.0"

val catsVersion = "2.4.2"
val catsEffectVersion = "3.3.5"
val catsParseVersion = "0.3.3"
val doodleVersion = "0.10.1"
val munitVersion = "0.7.22"

val build = taskKey[Unit]("Format, compile, and test")

val sharedSettings = Seq(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    "org.typelevel" %% "cats-parse" % catsParseVersion,
    "org.creativescala" %% "doodle" % doodleVersion,
    "org.scalameta" %% "munit" % munitVersion % Test,
    "org.scalatestplus" %% "scalacheck-1-15" % "3.2.5.0" % Test,
    "io.chrisdavenport" %% "cats-scalacheck" % "0.3.0" % Test
  ),
  // Turn on some compiler flags that scalafix needs
  scalacOptions ++= Seq(
    "-Yrangepos",
    "-Ymacro-annotations",
    "-Wunused:imports"
  ),
  // Don't warn on dead code---some of the exercises need this
  scalacOptions -= ("-Wdead-code"),
  testFrameworks += new TestFramework("munit.Framework"),
  addCompilerPlugin(scalafixSemanticdb)
)

lazy val root = project
  .in(file("."))
  .settings(
    sharedSettings,
    build := {
      Def.sequential(scalafixAll.toTask(""), scalafmtAll, Test / test).value
    }
  )

