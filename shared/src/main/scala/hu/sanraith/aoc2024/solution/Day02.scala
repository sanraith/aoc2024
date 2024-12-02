package hu.sanraith.aoc2024.solution

/** Solution for https://adventofcode.com/2024/day/2 */
class Day02 extends Solution:
  override val title: String = "Red-Nosed Reports"

  override def part1(ctx: Context): Int =
    val reports = parseInput(ctx)
    reports.count(isSafe(_))

  override def part2(ctx: Context): Int =
    val reports = parseInput(ctx)
    reports.count(isSafe(_, withTolerance = true))

  def isSafe(report: Seq[Int], withTolerance: Boolean = false): Boolean =
    Seq(report, report.reverse).exists { r =>
      val patched = if (withTolerance) (0 to r.length).map(r.patch(_, Seq.empty, 1)) else Seq.empty
      (r +: patched).exists(_.sliding(2).forall { case Seq(a, b) => a < b && b - a <= 3 })
    }

  def parseInput(ctx: Context): Seq[Array[Int]] =
    ctx.input.linesIterator.map(_.split(" ").map(_.toInt)).toSeq
