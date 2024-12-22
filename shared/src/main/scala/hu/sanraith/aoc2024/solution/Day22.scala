package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.mutable as mut

/** Solution for https://adventofcode.com/2024/day/22 */
class Day22 extends Solution:
  override val title: String = "Monkey Market"

  val PRUNE_MOD = 16777216L
  val PRICE_CHANGE_COUNT = 2000

  override def part1(ctx: Context): Long =
    val secrets = mut.ArraySeq.from(parseInput(ctx))
    (1 to PRICE_CHANGE_COUNT).foreach: _ =>
      for i <- 0 until secrets.length do secrets(i) = getNextSecret(secrets(i))
    secrets.sum

  override def part2(ctx: Context): Long =
    val secrets = parseInput(ctx)
    val (diffs, bananas) = secrets.iterator
      .reportProgress(secrets.length, ctx)
      .flatMap(createDiffMap(_, PRICE_CHANGE_COUNT).toSeq)
      .toSeq
      .groupMap({ case (k, v) => k }) { case (k, v) => v }
      .mapValues(_.sum)
      .toSeq
      .sortBy({ case (k, v) => -v })
      .head

    bananas

  type DiffSeq = Seq[Byte]
  def createDiffMap(secretStart: Long, iterCount: Int): mut.Map[DiffSeq, Long] =
    val PAST_WINDOW = 4
    var secret = secretStart
    val diffQueue = mut.Queue.empty[Byte]
    val diffMap = mut.Map.empty[DiffSeq, Long]
    for it <- 1 to iterCount do
      val nextSecret = getNextSecret(secret)
      val nextSecretDigit = (nextSecret % 10)
      val diff = (nextSecretDigit - (secret % 10)).toByte
      secret = nextSecret

      diffQueue.enqueue(diff)
      if diffQueue.length == PAST_WINDOW then
        val seq = diffQueue.toSeq
        if !diffMap.contains(seq) then diffMap(seq) = nextSecretDigit
        diffQueue.dequeue()
    diffMap.filter { case (k, v) => v > 0 }

  def mix(value: Long, secret: Long) = value ^ secret
  def prune(secret: Long) = secret % PRUNE_MOD
  def getNextSecret(secretStart: Long): Long =
    var secret = secretStart
    secret = prune(mix(secret * 64L, secret))
    secret = prune(mix(secret / 32L, secret))
    secret = prune(mix(secret * 2048L, secret))
    secret

  def parseInput(ctx: Context) = ctx.input.linesIterator.map(_.toLong).toSeq
