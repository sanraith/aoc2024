package hu.sanraith.aoc2024.solution

import scala.collection.mutable
import hu.sanraith.aoc2024.util._

/** Solution for https://adventofcode.com/2024/day/6 */
class Day06 extends Solution:
  override val title: String = "Guard Gallivant"

  val DIRECTIONS = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0)) // N, E, S, W
  val START_TILE = '^'
  val EMPTY_TILE = '.'
  val OBSTACLE_TILE = '#'

  override def part1(ctx: Context): Long =
    val grid = parseInput(ctx)
    val (route, _) = findRoute(grid)
    route.map { case (pos, dir) => pos }.size

  override def part2(ctx: Context): Long =
    val grid = parseInput(ctx)
    val (route, _) = findRoute(grid)
    val routeSteps = route.map { case (pos, dir) => pos } - grid.start
    routeSteps.iterator
      .reportProgress(routeSteps.size, ctx)
      .count: step =>
        val (_, isLoop) = findRoute(grid, Some(grid.tiles + (step -> OBSTACLE_TILE)))
        isLoop

  def findRoute(grid: Grid, tilesOverride: Option[Map[Point, Char]] = None) //
      : (mutable.Set[(Point, Point)], Boolean) =
    val tiles = tilesOverride.getOrElse(grid.tiles)
    val route = mutable.Set.empty[(Point, Point)]
    var pos = grid.start
    var dirIdx = 0
    var isLoop = false

    while (!isLoop && grid.isInBounds(pos))
      isLoop = !route.add(pos, DIRECTIONS(dirIdx))
      val nextPos = pos + DIRECTIONS(dirIdx)
      tiles.get(nextPos) match
        case Some(OBSTACLE_TILE) => dirIdx = (dirIdx + 1) % DIRECTIONS.size
        case _                   => pos = nextPos

    (route, isLoop)

  def parseInput(ctx: Context): Grid =
    val tiles = ctx.input.linesIterator.zipWithIndex.flatMap { case (line, y) =>
      line.zipWithIndex.collect { case (tile, x) => Point(x, y) -> tile }
    }.toMap
    Grid(tiles)

  case class Grid(val tiles: Map[Point, Char]):
    val start = tiles.collectFirst { case (p, START_TILE) => p }.get
    val width = tiles.keys.map(_.x).max + 1
    val height = tiles.keys.map(_.y).max + 1
    def isInBounds(p: Point): Boolean = p.x >= 0 && p.x < width && p.y >= 0 && p.y < height

  case class Point(x: Int, y: Int):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def *(scalar: Int): Point = Point(this.x * scalar, this.y * scalar)
