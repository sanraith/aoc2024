package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.solution.Day14._
import scala.collection.mutable

/** Solution for https://adventofcode.com/2024/day/14 */
class Day14 extends Solution:
  override val title: String = "Restroom Redoubt"

  var gridSize = Point(101, 103)
  val NORTH = Point(0, -1)

  override def part1(ctx: Context): Long =
    val guards = parseInput(ctx)
    val steps = 100
    val gridMiddle = gridSize / 2
    val gridHalf = gridMiddle + Point(1, 1)
    guards
      .map { case Guard(p, v) => Guard((p + v * steps) % gridSize, v) }
      .filterNot(g => g.p.x == gridMiddle.x || g.p.y == gridMiddle.y)
      .groupBy(_.p / gridHalf)
      .mapValues(_.length)
      .values
      .product

  override def part2(ctx: Context): Long =
    // The heuristic is looking for a long vertical line that represents the trunk of the tree
    val minTrunkSize = 20
    val guards = parseInput(ctx)

    Iterator
      .from(1)
      .map: step =>
        val guardsAtStep = guards.map { case Guard(p, v) => Guard((p + v * step) % gridSize, v) }
        val hasTrunk = guardsAtStep
          .sortBy(_.p.y)
          .groupMap(_.p.x)(_.p)
          .values
          .filter(_.length >= minTrunkSize)
          .exists: points =>
            val (trunks, trunk) = points.tail.foldLeft(Seq.empty[Int], Seq(points.head)):
              case ((trunks, trunk), p) if (p + NORTH == trunk.last) => (trunks, trunk :+ p)
              case ((trunks, trunk), p) => (trunks :+ trunk.length, Seq(p))
            (trunks :+ trunk.length).exists(_ >= minTrunkSize)
        // if (hasTrunk) render(guardsAtStep)
        (step, hasTrunk)
      .collectFirst { case (step, hasTrunk) if hasTrunk => step }
      .get

  def render(guards: Seq[Guard]) =
    val map = guards.groupBy(g => g.p).toMap
    val rendered = (0 until gridSize.y.toInt)
      .map: y =>
        (0 until gridSize.x.toInt)
          .map: x =>
            val guardCount = map.get(Point(x, y)).map(_.length).getOrElse(0)
            if (guardCount > 0) '#' else '.'
          .mkString
      .mkString("\n")
    println(rendered)

  def parseInput(ctx: Context): Seq[Guard] =
    val numRegex = """(-?\d+)""".r
    ctx.input.linesIterator.toSeq.map: line =>
      numRegex.findAllIn(line).toSeq.map { case numRegex(s) => s.toLong } match
        case Seq(px, py, vx, vy) => Guard(Point(px, py), Point(vx, vy))

object Day14:
  def modWrap(a: Long, b: Long): Long = ((a % b) + b) % b
  case class Guard(p: Point, v: Point)
  case class Point(x: Long, y: Long):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    def *(scalar: Long): Point = Point(this.x * scalar, this.y * scalar)
    def /(scalar: Long): Point = Point(this.x / scalar, this.y / scalar)
    def /(other: Point): Point = Point(this.x / other.x, this.y / other.y)
    def %(other: Point): Point = Point(modWrap(this.x, other.x), modWrap(this.y, other.y))
