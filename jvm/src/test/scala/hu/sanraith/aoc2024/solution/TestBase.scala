package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.cli.Util
import hu.sanraith.aoc2024.solution._
import org.scalatest.funspec.AnyFunSpec

import scala.util.matching.Regex

abstract class SolutionTestSpec extends AnyFunSpec {
  private val LeadingNewLineRegex: Regex = """(?s)^\s*\R(.*)$""".r
  private val ClassDayRegex: Regex = """^.*?(\d+)$""".r

  /** Asserts that the given part returns the expected output for the given input. Uses puzzle input
    * from `input/Day<day>.txt` if no input provided.
    */
  def assertPart(
      part: (Context) => WholeNumberOrString,
      expected: WholeNumberOrString,
      input: String = null
  )(implicit solution: Solution) =
    solution.println = println
    val resolvedInput = trim(Option(input).getOrElse(Util.loadInputFromFile(solution)))
    try
      val actual = part(TestContext(resolvedInput))
      assertResult(trim(expected.toString))(actual.toString)
    catch case _: NotImplementedError => pending

  /** PENDING assert, remove "_" to make test active. */
  def _assertPart(
      part: (Context) => WholeNumberOrString,
      expected: WholeNumberOrString,
      input: String = null
  ) = pending

  /** Trim the starting newline from a string. Allows easier-to-read declaration with """...""" */
  private def trim(text: String): String =
    LeadingNewLineRegex.findFirstMatchIn(text) match
      case Some(m) => m.group(1)
      case None    => text
}

private final class TestContext(val input: String) extends Context
