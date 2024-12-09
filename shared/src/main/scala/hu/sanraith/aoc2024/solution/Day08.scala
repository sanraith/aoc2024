package hu.sanraith.aoc2024.solution

/** Solution for https://adventofcode.com/2024/day/8 */
class Day08 extends Solution:
  override val title: String = "Resonant Collinearity"

  val EMPTY_TILE = '.'

  override def part1(ctx: Context): Long =
    val grid = parseInput(ctx)
    val antennas = grid.tiles.groupBy { case (_, tile) => tile }.mapValues(_.keySet)
    val antiNodes = antennas.toSet.flatMap:
      case (_, antennaTypePositions) =>
        val antennaPairs = antennaTypePositions.toSeq.combinations(2)
        antennaPairs.flatMap:
          case Seq(a, b) =>
            val dir = b - a
            Seq(a - dir, b + dir).filter(grid.isInBounds)
    antiNodes.size

  override def part2(ctx: Context): Long =
    val grid = parseInput(ctx)
    val antennas = grid.tiles.groupBy { case (_, tile) => tile }.mapValues(_.keySet)
    val antiNodes = antennas.toSet.flatMap:
      case (_, antennaTypePositions) =>
        val antennaPairs = antennaTypePositions.toSeq.combinations(2)
        antennaPairs.flatMap:
          case Seq(a, b) =>
            val dir = b - a
            (0 until Int.MaxValue).iterator
              .map(dist => Seq(a - dir * dist, b + dir * dist).filter(grid.isInBounds))
              .takeWhile(_.nonEmpty)
              .flatten
    antiNodes.size

  def parseInput(ctx: Context) =
    val tiles = ctx.input.linesIterator.zipWithIndex.flatMap { case (line, y) =>
      line.zipWithIndex.collect { case (tile, x) if tile != EMPTY_TILE => Point(x, y) -> tile }
    }.toMap
    val width = ctx.input.linesIterator.toSeq.headOption.map(_.length).getOrElse(0)
    val height = ctx.input.linesIterator.toSeq.length
    Grid(tiles, width, height)

  case class Grid(val tiles: Map[Point, Char], width: Int, height: Int):
    def isInBounds(p: Point): Boolean = p.x >= 0 && p.x < width && p.y >= 0 && p.y < height

  case class Point(x: Int, y: Int):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    def *(scalar: Int): Point = Point(this.x * scalar, this.y * scalar)
