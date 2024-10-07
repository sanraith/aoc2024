# aoc2024

Solutions for Advent of Code 2024.

## Setup dev environment

- Install Scala build tool: <https://www.scala-lang.org/download/>
- Install Node.js: <https://nodejs.org/en/download/prebuilt-installer>
- `npm install`

## Run WEB project with live refresh

Run these commands in parallel:

- `sbt web` or `sbt ~fastLinkJS` - Rebuilds scala sources on changes
- `npm run dev` - Runs webserver and rebuilds web sources on changes

## Run CONSOLE project

- `sbt solve` or `sbt aoc2024JVM/run` - Runs the solutions in the console

## Project structure

Based on <https://www.scala-js.org/doc/tutorial/scalajs-vite.html> and <https://www.scala-js.org/doc/project/cross-build.html>.

- `js`: Scala sources (WEB)
- `jvm`: Scala sources (CONSOLE)
- `project`: Scala project configuration
  - `build.properties`: Scala build properties
  - `plugins.sbt`: Scala plugins
- `public`: Static resources for web
- `shared`: Scala sources (SHARED)
- `src`: Typescript sources for web
- `.scalafmt.conf`: Scala formatting config
- `build.sbt`: Scala build config
- `index.html`: Loader template for web
- `package.json`: Node packages and config
- `tsconfig.json`: Typescript configuration
- `vite.config.json`: Vite build config
