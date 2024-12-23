import org.scalajs.linker.interface.ModuleSplitStyle
import org.scalajs.linker.interface.ESVersion

Global / lintUnusedKeysOnLoad := false
run / javaOptions += "-Dfile.encoding=UTF-8"
outputStrategy := Some(StdoutOutput)

/* --- Command aliases --- */
addCommandAlias("solve", "aoc2024JVM/run")
addCommandAlias("scaffold", "aoc2024JVM/run scaffold")
addCommandAlias("web", "~fastLinkJS")
(1 to 25).flatMap(day => // Test alias for each day: "test-1".."test-25"
  addCommandAlias(
    s"test-$day",
    s"testOnly hu.sanraith.aoc2024.solution.Day${"%02d".format(day)}Test"
  )
)

/* --- Project config --- */

lazy val root = project
  .in(file("."))
  .aggregate(aoc2024.js, aoc2024.jvm)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val aoc2024 = crossProject(JSPlatform, JVMPlatform)
  .in(file("."))
  .settings(
    name := "aoc2024",
    version := "0.1-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parallel-collections" % "1.1.0"
    )
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "com.github.pureconfig" % "pureconfig_2.13" % "0.17.7",
      "org.jsoup" % "jsoup" % "1.18.1",
      "org.scala-js" %% "scalajs-stubs" % "1.1.0" % "provided",
      "org.scalatest" %% "scalatest-funspec" % "3.2.19" % "test"
    ),
    scalaVersion := "3.5.1",
    mainClass := Some("hu.sanraith.aoc2024.cli.Main")
  )
  .jsSettings(
    scalaVersion := "3.5.1",

    // Tell Scala.js that this is an application with a main method
    scalaJSUseMainModuleInitializer := true,

    /* Configure Scala.js to emit modules in the optimal way to
     * connect to Vite's incremental reload.
     * - emit ECMAScript modules
     * - emit as many small modules as possible for classes in the "aoc2024" package
     * - emit as few (large) modules as possible for all other classes
     *   (in particular, for the standard library)
     */
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(
          ModuleSplitStyle.SmallModulesFor(List("hu.sanraith.aoc2024"))
        )
        .withESFeatures(_.withESVersion(ESVersion.ES2020))
    },

    /* Depend on the scalajs-dom library.
     * It provides static types for the browser DOM APIs.
     */
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0",
    libraryDependencies += "com.raquo" %%% "laminar" % "17.0.0"
  )
