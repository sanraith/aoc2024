package hu.sanraith.aoc2024.web

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.*
// import hu.sanraith.aoc2024.solution.Day01
import hu.sanraith.aoc2024.solution.Context
import com.raquo.laminar.api.L.{*, given}
import scala.util.Random
import hu.sanraith.aoc2024.solution.SolutionMap
import hu.sanraith.aoc2024.solution.Solution
import scala.util.Try

class SimpleContext(override val input: String) extends Context

@main
def Main(): Unit =
  renderOnDomContentLoaded(dom.document.getElementById("app"), WebMain.mainPage())

object WebMain {
  def mainPage() = { //
    div(
      a(
        href := "https://vitejs.dev",
        target := "_blank",
        img(src := "/vite.svg", className := "logo", alt := "Vite logo")
      ),
      a(
        href := "https://developer.mozilla.org/en-US/docs/Web/JavaScript",
        target := "_blank",
        img(src := typescriptLogo, className := "logo vanilla", alt := "JavaScript logo")
      ),
      h1("Hello Laminar!"),
      dayElement(),
      p(className := "read-the-docs", "Click on the Vite logo to learn more")
    )
  }

  def dayElement() = {
    val dayInput = Var("sevenineeight29seven")
    val dayModel = dayInput.signal.map(x => DayModel(1, x))
    div(
      input(
        typ := "text",
        value <-- dayInput,
        onInput.mapToValue --> dayInput.writer
      ),
      "--> (",
      child.text <-- dayModel.map(_.part1),
      ", ",
      child.text <-- dayModel.map(_.part2),
      ")"
    )
  }
}

case class DayModel(day: Int, input: String) {
  val solution: Option[Solution] = SolutionMap.get(day).headOption.map(_.createInstance())
  val context = SimpleContext(input)

  lazy val part1: String =
    solution
      .map(s => Try { s.part1(context).toString() }.getOrElse("???"))
      .getOrElse("no solution available")
  lazy val part2: String =
    solution
      .map(s => Try { s.part2(context).toString() }.getOrElse("???"))
      .getOrElse("no solution available")
}
