package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.mutable as mut

/** Solution for https://adventofcode.com/2024/day/23 */
class Day23 extends Solution:
  override val title: String = "LAN Party"

  override def part1(ctx: Context): Long =
    val computers = parseInput(ctx)
    val triples = computers.values
      .filter(_.name.startsWith("t"))
      .flatMap: startComputer =>
        startComputer.connections.toSeq
          .combinations(2)
          .filter { case Seq(a, b) => a.connections.contains(b) }
          .map(pair => (pair :+ startComputer).toSet)
      .toSet
    triples.size

  override def part2(ctx: Context): String =
    val computers = parseInput(ctx)
    val largestGroup = computers.values.foldLeft(Seq.empty[Computer]):
      case (largestGroup, computer) =>
        findLargestGroup(computer, minSize = largestGroup.size).getOrElse(largestGroup)
    largestGroup.map(_.name).sorted.mkString(",")

  def findLargestGroup(start: Computer, minSize: Int): Option[Seq[Computer]] =
    val connections = start.connections.toSeq
    val groupOption = Iterator
      .from(connections.size, -1)
      .takeWhile(_ >= minSize)
      .map: groupSize =>
        connections
          .combinations(groupSize)
          .find: group =>
            val pairs = group.combinations(2)
            pairs.forall { case Seq(a, b) => a.connections.contains(b) }
      .collectFirst { case Some(seq) => start +: seq }
    groupOption

  def parseInput(ctx: Context): mut.Map[String, Computer] =
    val computers = mut.Map.empty[String, Computer]
    ctx.input.linesIterator.foreach:
      case s"$a-$b" =>
        val Seq(ca, cb) = Seq(a, b).map(name => computers.getOrElseUpdate(name, Computer(name)))
        ca.connect(cb)
    computers

  case class Computer(name: String):
    val connections = mut.Set.empty[Computer]
    def connect(b: Computer): Unit =
      connections.add(b)
      b.connections.add(this)
