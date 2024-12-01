# aoc2024

Solutions for Advent of Code 2024.

## Setup dev environment

- Install Scala build tool: <https://www.scala-lang.org/download/>
- Install Node.js: <https://nodejs.org/en/download/prebuilt-installer>
- `npm install`

## Console commands

Start `sbt` and enter one of the commands:

- Run solutions:  
`solve [ all | last | <days...> ]`
- Test solutions:  
`test` | `test-<day>`
- Scaffold solutions:  
 Create solution, test and input files by getting info from [adventofcode.com](https://adventofcode.com).  
`scaffold [reload] [ <days...> | input [<days...>] | inputs ]`

### Examples

- `solve` / `solve all` - Run all available solutions.
- `solve last` - Run the latest available solution.
- `solve 1 2` / `solve day 1 2 3` - Run solutions for the given days.
- `scaffold` - Scaffold the earliest missing solution.
- `scaffold 1 2` / `scaffold day 1 2 3` - Scaffold the given days.
- `scaffold inputs` - Download inputs for all implemented solutions.
- `scaffold input 1 2` - Download inputs for the given days.
- `scaffold reload input 1 2` - Re-download the input for the given days, invalidating the cache.
- `test` - Run all tests.
- `test-12` - Run tests for day 12.

## Run WEB project with live refresh

Run these commands in parallel:

- `sbt web` or `sbt ~fastLinkJS` - Rebuilds scala sources on changes
- `npm run dev` - Runs webserver and rebuilds web sources on changes

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

## Configuration options

The app generates `aoc2024.config.json` on first run:

```jsonc
{
    // Copy the last successful result to the clipboard
    "copyResultToClipboard": false,
    // The event year to use for puzzle info from adventofcode.com
    "eventYear" : 2024,
    // Open the scaffolded files using the editor defined in "pathToEditor"
    "openScaffoldedFiles": false,
    // The editor "openScaffoldedFiles" uses
    "pathToEditor": "C:\\Program Files\\Microsoft VS Code\\Code.exe",
    // Your Advent of Code session key used to get your puzzle input
    "sessionCookie": "YOUR_SESSION_COOKIE"
}
```

## Advent of Code Automation

This repository does follow the automation guidelines on the /r/adventofcode [community wiki](https://www.reddit.com/r/adventofcode/wiki/faqs/automation). Specifically:

- Outbound calls are only triggered manually, by `sbt> scaffold [...]` -> `Main.scaffold()`
- All successful web requests are cached locally in `.cache/` by `WebClient.requestCached()`
- If you suspect your input is corrupted, you can manually request a fresh copy by `sbt> scaffold reload input <day>`
- The User-Agent header in `WebClient.request()` is set to me since I maintain this repository.
