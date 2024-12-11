package hu.sanraith.aoc2024.solution

import scala.collection.mutable

/** Solution for https://adventofcode.com/2024/day/10 */
class Day10 extends Solution:
  override val title: String = "Hoof It"

  val DIRECTIONS = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0))
  val START_HEIGHT = 0
  val TARGET_HEIGHT = 9

  override def part1(ctx: Context): Long =
    val grid = parseInput(ctx)
    val trailHeads = grid.tiles.collect { case (pos, height) if height == START_HEIGHT => pos }

    trailHeads.map { trailHead =>
      val visited = mutable.Set.empty[Point]
      val queue = mutable.Queue(trailHead)
      var pathCount = 0
      while (queue.nonEmpty)
        val pos = queue.dequeue()
        if (visited.add(pos))
          val height = grid.tiles(pos)
          if (height == TARGET_HEIGHT) pathCount += 1
          else
            DIRECTIONS
              .map(pos + _)
              .filter(p => grid.isInBounds(p) && grid.tiles(p) == height + 1)
              .foreach(p => queue.enqueue(p))
      pathCount
    }.sum

  override def part2(ctx: Context): Long =
    val grid = parseInput(ctx)
    val trailHeads = grid.tiles.collect { case (p, h) if h == START_HEIGHT => p }

    trailHeads.map { trailHead =>
      val queue = mutable.Queue(Seq(trailHead))
      var pathCount = 0
      while (queue.nonEmpty)
        val trail = queue.dequeue()
        val pos = trail.last
        val height = grid.tiles(pos)
        if (height == TARGET_HEIGHT) pathCount += 1
        else
          DIRECTIONS
            .map(pos + _)
            .filter(p => grid.isInBounds(p) && grid.tiles(p) == height + 1)
            .foreach(p => queue.enqueue(trail :+ p))
      pathCount
    }.sum

  def parseInput(ctx: Context) =
    val tiles = ctx.input.linesIterator.zipWithIndex.flatMap { case (line, y) =>
      line.zipWithIndex.collect:
        case (tile, x) if x.isValidInt => Point(x, y) -> tile.asDigit
        case (_, x)                    => Point(x, y) -> -1
    }.toMap
    val width = ctx.input.linesIterator.toSeq.headOption.map(_.length).getOrElse(0)
    val height = ctx.input.linesIterator.toSeq.length
    Grid(tiles, width, height)

  case class Grid(val tiles: Map[Point, Int], width: Int, height: Int):
    def isInBounds(p: Point): Boolean = p.x >= 0 && p.x < width && p.y >= 0 && p.y < height

  case class Point(x: Int, y: Int):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    def *(scalar: Int): Point = Point(this.x * scalar, this.y * scalar)
