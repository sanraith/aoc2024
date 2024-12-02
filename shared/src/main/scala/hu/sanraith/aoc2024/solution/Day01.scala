package hu.sanraith.aoc2024.solution

import scala.util.matching.Regex

/** Solution for https://adventofcode.com/2024/day/1 */
class Day01 extends Solution:
  override val title: String = "Historian Hysteria"

  override def part1(ctx: Context): Int =
    val (seq1, seq2) = parseInput(ctx)
    seq1.sorted.zip(seq2.sorted).map { case (a, b) => Math.abs(a - b) }.sum

  override def part2(ctx: Context): Int =
    val (seq1, seq2) = parseInput(ctx)
    seq1.map(x => x * seq2.count(_ == x)).sum

  def parseInput(ctx: Context): (Seq[Int], Seq[Int]) =
    val numberPairRegex = Regex("""(\d+)\s+(\d+)""")
    ctx.input.linesIterator
      .collect { case numberPairRegex(a, b) => (a.toInt, b.toInt) }
      .toSeq
      .unzip
