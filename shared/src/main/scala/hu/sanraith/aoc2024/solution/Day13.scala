package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.mutable

/** Solution for https://adventofcode.com/2024/day/13 */
class Day13 extends Solution:
  override val title: String = "Claw Contraption"

  override def part1(ctx: Context): Long =
    val machines = parseInput(ctx)
    getWithDelta(machines, ctx)
    // val maxPressCount = 100
    // val machines = parseInput(ctx)
    // machines.map { machine =>
    //   val costs = for
    //     timesB <- 0 to maxPressCount
    //     timesA <- 0 to maxPressCount
    //     if machine.a * timesA + machine.b * timesB == machine.prize
    //   yield timesA * 3 + timesB
    //   costs.minOption.getOrElse(0)
    // }.sum

  override def part2(ctx: Context): Long =
    val delta = Point(10000000000000L, 10000000000000L)
    val machines = parseInput(ctx)
    getWithDelta(machines, ctx, delta)

    // 91204896407640 too high

  def getWithDelta(machines: Seq[ClawMachine], ctx: Context, delta: Point = Point(0, 0)) =
    machines
      .map { case ClawMachine(a, b, p) => ClawMachine(a, b, p + delta) }
      .iterator
      .tapEachWithIndex { case (_, idx) => ctx.progress(idx.toDouble / machines.length) }
      .map { case ClawMachine(a, b, p) =>
        val visited = mutable.Set.empty[Point]
        var n = Math.min(p.x / b.x, p.y / b.y)
        var found: Option[Long] = None
        var shouldContinue = true
        while (found.isEmpty && shouldContinue && n >= 0) {
          val rem = (p - b * n) % a
          if (rem.x == 0 && rem.y == 0) found = Some(n)
          if (!visited.add(rem)) shouldContinue = false
          n -= 1
        }

        found match
          case Some(nb) =>
            val na = (p - b * nb).x / a.x
            // println(s"B times $nb, A times $na");
            na * 3 + nb
          case None => 0
      }
      .sum

  def parseInput(ctx: Context): Seq[ClawMachine] =
    val numPairRegex = """(\d+).*?(\d+)""".r
    ctx.input.split("""\R\R""").toSeq.map { case m =>
      numPairRegex
        .findAllMatchIn(m)
        .map { case numPairRegex(x, y) => Point(x.toLong, y.toLong) }
        .toSeq match
        case Seq(a, b, p) => ClawMachine(a, b, p)
    }

  def lcm(a: Long, b: Long): Long = a * b / gcd(a, b)
  def gcd(a: Long, b: Long): Long = if b == 0 then a else gcd(b, a % b)

  case class ClawMachine(a: Point, b: Point, prize: Point)

  case class Point(x: Long, y: Long):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    def *(scalar: Long): Point = Point(this.x * scalar, this.y * scalar)
    def %(other: Point): Point = Point(this.x % other.x, this.y % other.y)
