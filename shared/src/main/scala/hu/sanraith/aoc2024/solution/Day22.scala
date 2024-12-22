package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.mutable as mut

/** Solution for https://adventofcode.com/2024/day/22 */
class Day22 extends Solution:
  override val title: String = "Monkey Market"

  val PRUNE_MOD = 16777216L

  override def part1(ctx: Context): Long =
    val secrets = mut.ArraySeq.from(parseInput(ctx))
    val iterCount = 2000
    (1 to iterCount).iterator
      .tapEachWithIndex { case (_, i) => ctx.progress(i.toDouble / iterCount) }
      .foreach: _ =>
        for i <- 0 until secrets.length do secrets(i) = nextSecret(secrets(i))
    secrets.sum

  override def part2(ctx: Context): Long =
    ???

  def nextSecret(start: Long): Long =
    var secret = start
    secret = prune(mix(secret * 64L, secret))
    secret = prune(mix(secret / 32L, secret))
    secret = prune(mix(secret * 2048L, secret))
    secret

  def mix(value: Long, secret: Long) = value ^ secret

  def prune(secret: Long) = secret % PRUNE_MOD

  def parseInput(ctx: Context) = ctx.input.linesIterator.map(_.toLong).toSeq
