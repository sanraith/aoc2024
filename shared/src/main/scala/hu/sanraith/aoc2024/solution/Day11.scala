package hu.sanraith.aoc2024.solution

import scala.collection.mutable
import hu.sanraith.aoc2024.util._

/** Solution for https://adventofcode.com/2024/day/11 */
class Day11 extends Solution:
  override val title: String = "Plutonian Pebbles"

  override def part1(ctx: Context): Long =
    val numbers = parseInput(ctx)
    blink(25, numbers, ctx)

  override def part2(ctx: Context): Long =
    val numbers = parseInput(ctx)
    blink(75, numbers, ctx)

  def blink(blinkCount: Int, numbers: Seq[Long], ctx: Context) =
    var numberCounts = numbers.groupBy(identity).mapValues(_.length.toLong).toMap
    (1 to blinkCount)
      .tapEachWithIndex { case (_, idx) => ctx.progress(idx.toDouble / blinkCount) }
      .foreach: _ =>
        numberCounts = numberCounts.toSeq
          .flatMap { case (num, count) =>
            val newNumbers = num match
              case 0 => Seq(1L)
              case x if math.log10(x).toInt % 2 == 1 =>
                val (a, b) = x.toString.splitAt(x.toString.length / 2)
                Seq(a.toLong, b.toLong)
              case x => Seq(x * 2024L)
            newNumbers.map(_ -> count)
          }
          .groupBy { case (num, _) => num }
          .mapValues(_.map { case (_, count) => count }.sum)
          .toMap
    numberCounts.map { case (_, count) => count }.sum

  def parseInput(ctx: Context) = ctx.input.trim.split(' ').map(_.toLong).toSeq
