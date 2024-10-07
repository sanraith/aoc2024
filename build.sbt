import org.scalajs.linker.interface.ModuleSplitStyle

Global / lintUnusedKeysOnLoad := false
run / javaOptions += "-Dfile.encoding=UTF-8"
outputStrategy := Some(StdoutOutput)

addCommandAlias("solve", "aoc2024JVM/run")
addCommandAlias("web", "~fastLinkJS")

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
    version := "0.1-SNAPSHOT"
  )
  .jvmSettings(
    libraryDependencies += "org.scala-js" %% "scalajs-stubs" % "1.1.0" % "provided",
    scalaVersion := "3.5.1",
    mainClass := Some("hu.sanraith.aoc2024.shell.Main")
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
          ModuleSplitStyle.SmallModulesFor(List("hu.sanraith.aoc2024.web"))
        )
    },

    /* Depend on the scalajs-dom library.
     * It provides static types for the browser DOM APIs.
     */
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0"
  )
