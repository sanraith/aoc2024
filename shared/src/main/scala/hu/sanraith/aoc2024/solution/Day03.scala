package hu.sanraith.aoc2024.solution

import scala.util.matching.Regex

/** Solution for https://adventofcode.com/2024/day/3 */
class Day03 extends Solution:
  override val title: String = "Mull It Over"

  val mulRegex = Regex("""mul\((-?\d+),(-?\d+)\)""")

  override def part1(ctx: Context): Int =
    ctx.input.linesIterator
      .flatMap(mulRegex.findAllMatchIn(_).map { case mulRegex(a, b) => a.toInt * b.toInt })
      .sum

  override def part2(ctx: Context): Int =
    val doRegex = Regex("""do\(\)""")
    val dontRegex = Regex("""don't\(\)""")
    val (_, sum) = ctx.input.linesIterator.foldLeft((true, 0)):
      case ((enabled, sum), line) =>
        Seq(doRegex, dontRegex, mulRegex)
          .flatMap(_.findAllMatchIn(line))
          .sortBy(_.start)
          .foldLeft((enabled, sum)):
            case ((enabled, sum), m) =>
              m match {
                case doRegex()      => (true, sum)
                case dontRegex()    => (false, sum)
                case mulRegex(a, b) => (enabled, sum + (if (enabled) a.toInt * b.toInt else 0))
              }
    sum
