package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._

/** Solution for https://adventofcode.com/2024/day/7 */
class Day07 extends Solution:
  override val title: String = "Bridge Repair"

  override def part1(ctx: Context): Long =
    val ops = Seq('+', '*')
    val equations = parseInput(ctx)
    equations.collect { case (res, parts) if isEquationValid(res, parts, ops) => res }.sum

  override def part2(ctx: Context): Long =
    val ops = Seq('+', '*', '|')
    val equations = parseInput(ctx)
    equations.iterator
      .reportProgress(equations.length, ctx)
      .collect { case (res, parts) if isEquationValid(res, parts, ops) => res }
      .sum

  def isEquationValid(
      expected: Long,
      parts: Seq[Long],
      ops: Seq[Char],
      partialResult: Option[Long] = None
  ): Boolean = partialResult match
    case None => isEquationValid(expected, parts.tail, ops, Some(parts.head))
    case Some(result) if parts.length == 0 => result == expected
    case Some(partialResult) =>
      ops
        .map:
          case '+' => partialResult + parts.head
          case '*' => partialResult * parts.head
          case '|' => s"$partialResult${parts.head}".toLong
        .exists(result => isEquationValid(expected, parts.tail, ops, Some(result)))

  def parseInput(ctx: Context): Seq[(Long, Seq[Long])] =
    ctx.input.linesIterator.toSeq.map: line =>
      val Array(resultStr, partsStr) = line.split(':');
      val parts = partsStr.trim().split(' ').map(_.toLong).toSeq
      (resultStr.toLong, parts)
