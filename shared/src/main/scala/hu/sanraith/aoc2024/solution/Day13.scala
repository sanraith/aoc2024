package hu.sanraith.aoc2024.solution

import scala.collection.mutable
import scala.math.Integral.Implicits.*

/** Solution for https://adventofcode.com/2024/day/13 */
class Day13 extends Solution:
  override val title: String = "Claw Contraption"

  override def part1(ctx: Context): Long =
    val machines = parseInput(ctx)
    countTokens(machines, ctx)

  override def part2(ctx: Context): Long =
    val delta = Point(10000000000000L, 10000000000000L)
    val machines = parseInput(ctx)
    countTokens(machines, ctx, delta)

  def countTokens(machines: Seq[ClawMachine], ctx: Context, delta: Point = Point(0, 0)) = machines
    .map { case ClawMachine(a, b, p) => ClawMachine(a, b, p + delta) }
    .map { case ClawMachine(a, b, p) =>
      // px = ax * n + bx * m
      // py = ay * n + by * m
      // solve for single root n, m
      // n = (by * px - nx * py) / (ax * by - ay * bx)
      // m = (ay * px - ax * py) / (ay * bx - ax * by)
      val (Point(ax, ay), Point(bx, by), Point(px, py)) = (a, b, p)
      val (n, nr) = (by * px - bx * py) /% (ax * by - ay * bx)
      val (m, mr) = (ay * px - ax * py) /% (ay * bx - ax * by)
      val hasSolution = n >= 0 && m >= 0 && nr == 0 && mr == 0

      // My inputs only had a single root, so this produces the correct result even without caring about the token weights
      if (hasSolution) n * 3 + m else 0
    }
    .sum

  def parseInput(ctx: Context): Seq[ClawMachine] =
    val posRgx = """(\d+).*?(\d+)""".r
    ctx.input
      .split("""\R\R""")
      .map(posRgx.findAllMatchIn(_).toSeq.map { case posRgx(x, y) => Point(x.toLong, y.toLong) })
      .collect { case Seq(a, b, p) => ClawMachine(a, b, p) }

  case class ClawMachine(a: Point, b: Point, prize: Point)

  case class Point(x: Long, y: Long):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    def *(scalar: Long): Point = Point(this.x * scalar, this.y * scalar)
