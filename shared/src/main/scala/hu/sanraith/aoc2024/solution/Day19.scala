package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._

import scala.collection.immutable.WrappedString
import scala.collection.{mutable => mut}

/** Solution for https://adventofcode.com/2024/day/19 */
class Day19 extends Solution:
  override val title: String = "Linen Layout"

  override def part1(ctx: Context): Long =
    val (towels, designs) = parseInput(ctx)
    val towelsByInitial = towels.groupBy(t => t.head).mapValues(_.sortBy(-_.length)).toMap
    designs.count(design => countArrangements(design.toSeq, towelsByInitial) > 0)

  override def part2(ctx: Context): Long =
    val (towels, designs) = parseInput(ctx)
    val towelsByInitial = towels.groupBy(t => t.head).mapValues(_.sortBy(-_.length)).toMap
    designs.map(design => countArrangements(design.toSeq, towelsByInitial)).sum

  def countArrangements(
      design: WrappedString,
      towelsByInitial: Map[Char, Seq[String]],
      cache: mut.Map[WrappedString, Long] = mut.Map("".toSeq -> 1)
  ): Long = cache.get(design) match
    case Some(count) => count
    case None =>
      val towelsToCheck = towelsByInitial.getOrElse(design.head, Seq.empty)
      cache(design) = towelsToCheck.collect {
        case towel if design.startsWith(towel) =>
          countArrangements(design.slice(towel.length, design.length), towelsByInitial, cache)
      }.sum
      cache(design)

  def parseInput(ctx: Context) =
    val wordRegex = raw"\w+".r
    val Seq(towelsStr, designsStr) = ctx.input.split(raw"\R\R").toSeq
    val towels = wordRegex.findAllIn(towelsStr).toSeq
    val designs = wordRegex.findAllIn(designsStr).toSeq
    (towels, designs)
