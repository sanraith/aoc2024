package hu.sanraith.aoc2024.solution

/** Solution for https://adventofcode.com/2024/day/5 */
class Day05 extends Solution:
  override val title: String = "Print Queue"

  override def part1(ctx: Context): Long =
    val (pairs, updates) = parseInput(ctx);
    updates.map { update =>
      val sorted = update.sortWith { case (a, b) => pairs.contains(a, b) }
      if (sorted.sameElements(update)) update(update.length / 2) else 0
    }.sum

  override def part2(ctx: Context): Long =
    val (pairs, updates) = parseInput(ctx);
    updates.map { update =>
      val sorted = update.sortWith { case (a, b) => pairs.contains(a, b) }
      if (sorted.sameElements(update)) 0 else sorted(sorted.length / 2)
    }.sum

  def parseInput(ctx: Context): (Seq[(Int, Int)], Seq[Seq[Int]]) =
    val Seq(pairsStr, updatesStr) = ctx.input.split("""\R\R""").toSeq
    val pairs = pairsStr.linesIterator
      .map(_.split('|').map(_.toInt) match { case Array(a, b) => a -> b })
    val updates = updatesStr.linesIterator.map(_.split(",").map(_.toInt).toSeq)
    (pairs.toSeq, updates.toSeq)
