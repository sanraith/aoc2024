package hu.sanraith.aoc2024.solution

import scala.collection.{mutable => mut}
import hu.sanraith.aoc2024.solution.Day18._

/** Solution for https://adventofcode.com/2024/day/18 */
class Day18 extends Solution:
  override val title: String = "RAM Run"

  var grid = Grid(71, 71)
  var part1ByteCount = 1024

  val DIRECTIONS: Seq[Direction] = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0))

  override def part1(ctx: Context): Long =
    val bytes = parseInput(ctx).take(part1ByteCount).toSet
    findPath(bytes).size

  override def part2(ctx: Context): String =
    val bytes = parseInput(ctx)
    var byteCount = part1ByteCount
    var path = bytes.toSet
    while path.nonEmpty && byteCount < bytes.length do
      ctx.progress(byteCount.toDouble / bytes.length)
      byteCount += 1
      if path.contains(bytes(byteCount - 1)) then //
        path = findPath(bytes.take(byteCount).toSet)

    val cutoffByte = bytes(byteCount - 1)
    s"${cutoffByte.x},${cutoffByte.y}"

  def findPath(bytes: Set[Point]) =
    val start = Point(0, 0)
    val target = Point(grid.width - 1, grid.height - 1)
    val visited = mut.Set.empty[Point]
    val queue = mut.Queue((start, 0, Set.empty[Point]))
    var finalPath = Set.empty[Point]
    while finalPath.isEmpty && queue.nonEmpty do
      val (pos, dist, path) = queue.dequeue()
      if (pos == target) finalPath = path
      else if visited.add(pos) then
        DIRECTIONS
          .map(pos + _)
          .filter(p => grid.isInBounds(p) && !bytes.contains(p))
          .foreach(p => queue.enqueue((p, dist + 1, path + p)))
    finalPath

  def parseInput(ctx: Context) = ctx.input.linesIterator
    .map(_.split(',').map(_.toLong).toSeq)
    .map { case Seq(a, b) => Point(a, b) }
    .toSeq

object Day18:
  case class Grid(val width: Int, val height: Int):
    def isInBounds(p: Point): Boolean = p.x >= 0 && p.x < width && p.y >= 0 && p.y < height

  type Direction = Point
  case class Point(x: Long, y: Long):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    def *(scalar: Long): Point = Point(this.x * scalar, this.y * scalar)
    def *(other: Point): Point = Point(this.x * other.x, this.y * other.y)
    def manhattan(other: Point): Long = Math.abs(this.x - other.x) + Math.abs(this.y - other.y)
