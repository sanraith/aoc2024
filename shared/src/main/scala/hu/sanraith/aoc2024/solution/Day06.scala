package hu.sanraith.aoc2024.solution

import scala.collection.mutable

/** Solution for https://adventofcode.com/2024/day/6 */
class Day06 extends Solution:
  override val title: String = "Guard Gallivant"

  val directions = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0)) // N, E, S, W
  val StartTile = '^';
  val EmptyTile = '.';
  val ObstructionTile = '#';

  override def part1(ctx: Context): Long =
    val grid = parseInput(ctx)
    val visited = getVisitedTiles(grid)
    visited.size

  override def part2(ctx: Context): Long =
    val grid = parseInput(ctx)
    val tiles = grid.tiles
    val start = tiles.find { case (_, tile) => tile == StartTile }.map { case (p, _) => p }.get

    val possibleObstaclePositions = getVisitedTiles(grid) - start
    possibleObstaclePositions.toSeq.zipWithIndex.count { case (extraObstaclePos, idx) =>
      ctx.progress(idx.toDouble / possibleObstaclePositions.size)
      val tiles2 = tiles + (extraObstaclePos -> ObstructionTile)

      var pos = start
      var dir = directions(0)
      val visited = mutable.Set.empty[(Point, Point)]
      var isLoop = false

      while (!isLoop && grid.isInside(pos)) {
        isLoop = !visited.add(pos, dir)
        val next = pos + dir
        tiles2.get(next) match {
          case tile if tile == Some(ObstructionTile) =>
            dir = directions((directions.indexOf(dir) + 1) % directions.length)
          case _ => pos = next
        }
      }
      isLoop
    }

  def getVisitedTiles(grid: Grid): Set[Point] =
    val tiles = grid.tiles
    val start = tiles.find { case (_, tile) => tile == StartTile }.map { case (p, _) => p }.get

    var pos = start
    var dir = directions(0)
    val visited = mutable.Set.empty[Point]
    while (grid.isInside(pos)) {
      visited.add(pos)
      val next = pos + dir
      tiles.get(next) match {
        case Some(ObstructionTile) =>
          dir = directions((directions.indexOf(dir) + 1) % directions.length)
        case _ => pos = next
      }
    }
    visited.toSet

  def parseInput(ctx: Context): Grid =
    val tiles = ctx.input.linesIterator.zipWithIndex.flatMap { case (line, y) =>
      line.zipWithIndex.map { case (tile, x) => Point(x, y) -> tile }
    }.toMap
    Grid(tiles)

  case class Grid(val tiles: Map[Point, Char]):
    val width = tiles.keys.map(_.x).max + 1
    val height = tiles.keys.map(_.y).max + 1
    def isInside(p: Point): Boolean = p.x >= 0 && p.x < width && p.y >= 0 && p.y < height

  case class Point(x: Int, y: Int):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def *(scalar: Int): Point = Point(this.x * scalar, this.y * scalar)
