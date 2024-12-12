package hu.sanraith.aoc2024.solution

import scala.collection.mutable

/** Solution for https://adventofcode.com/2024/day/12 */
class Day12 extends Solution:
  override val title: String = "Garden Groups"

  val DIRECTIONS: Seq[Direction] = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0))

  override def part1(ctx: Context): Long =
    val grid = parseInput(ctx)
    val regions = getRegions(grid)
    regions.map(r => r.area * r.sides.map(_.length).sum).sum

  override def part2(ctx: Context): Long =
    val grid = parseInput(ctx)
    val regions = getRegions(grid)
    regions.map(r => r.area * r.sides.length).sum

  def getRegions(grid: Grid) =
    val tiles = grid.tiles
    val visited = mutable.Set.empty[Point]
    tiles.keySet.iterator
      .filterNot(visited.contains)
      .map: start =>
        val perimeter = mutable.Queue.empty[(Point, Direction)]
        val queue = mutable.Queue(start)
        val tile = tiles(start)
        var area = 0
        while (queue.nonEmpty)
          val pos = queue.dequeue()
          if (visited.add(pos))
            area += 1
            DIRECTIONS.map(pos + _).foreach {
              case p if !tiles.get(p).contains(tile) => perimeter.enqueue((p, p - pos))
              case p                                 => queue.enqueue(p)
            }
        Region(tile, area, getSides(perimeter.toSeq))

  def getSides(perimeter: Seq[(Point, Direction)]) =
    // Point groups where each group corresponds to 1 or more sides
    val sidePointGroups: Iterable[Seq[Point]] = perimeter
      .groupMap { case (_, dir) => dir } { case (pos, _) => pos }
      .flatMap { case (dir, points) =>
        points.toSeq
          .groupBy(p => if (dir.x == 0) p.y else p.x)
          .map { case (_, seq) => seq.sortBy(p => if (dir.x == 0) p.x else p.y) }
      }

    // Collect continuous segments in groups to be the sides
    sidePointGroups.toSeq.flatMap: group =>
      val (sides, lastSide) = group.tail.foldLeft(Seq.empty[Seq[Point]], Seq(group.head)):
        case ((sides, side), p) if (p.isNeighbor(side.last)) => (sides, (side :+ p))
        case ((sides, side), p)                              => (sides :+ side, Seq(p))
      sides :+ lastSide

  def parseInput(ctx: Context) =
    val tiles = ctx.input.linesIterator.zipWithIndex.flatMap:
      case (line, y) =>
        line.zipWithIndex.collect { case (tile, x) => Point(x, y) -> tile }
    Grid(tiles.toMap)

  case class Region(tile: Char, area: Int, sides: Seq[Seq[Point]])

  case class Grid(val tiles: Map[Point, Char]):
    val width = tiles.keys.map(_.x).max + 1
    val height = tiles.keys.map(_.y).max + 1
    def isInBounds(p: Point): Boolean = p.x >= 0 && p.x < width && p.y >= 0 && p.y < height

  type Direction = Point
  case class Point(x: Int, y: Int):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    def *(scalar: Int): Point = Point(this.x * scalar, this.y * scalar)
    def isNeighbor(other: Point): Boolean =
      val Point(dx, dy) = other - this
      (Math.abs(dx) + Math.abs(dy)) == 1
