package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.mutable as mut

/** Solution for https://adventofcode.com/2024/day/25 */
class Day25 extends Solution:
  override val title: String = "Code Chronicle"

  override def part1(ctx: Context): Long =
    val schematics = parseInput(ctx)
    val keys = schematics.collect { case k: Key => k }
    val locks = schematics.collect { case l: Lock => l }
    keys.iterator.map(k => locks.count(tryKey(k, _))).sum

  override def part2(ctx: Context): String = "*"

  def tryKey(key: Key, lock: Lock): Boolean =
    key.pins.iterator.zip(lock.pins).forall { case (a, b) => a + b < lock.height }

  def parseInput(ctx: Context): Seq[Schematic] =
    val schematicStrings = ctx.input.split(raw"\R\R")
    schematicStrings.map: schematicString =>
      val lines = schematicString.linesIterator.toSeq
      val isLock = lines.head.forall(_ == '#')
      val pins = lines.transpose.map(_.count(_ == '#') - 1)
      if isLock then Lock(pins, lines.length - 1)
      else Key(pins)

  trait Schematic:
    val pins: Seq[Int]
  case class Lock(pins: Seq[Int], height: Int) extends Schematic
  case class Key(pins: Seq[Int]) extends Schematic
