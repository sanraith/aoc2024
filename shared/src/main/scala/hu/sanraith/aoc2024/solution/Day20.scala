package hu.sanraith.aoc2024.solution

import scala.collection.{mutable => mut}
import hu.sanraith.aoc2024.util._

/** Solution for https://adventofcode.com/2024/day/20 */
class Day20 extends Solution:
  override val title: String = "Race Condition"

  var saveThreshold = 100
  val isRenderEnabled = true

  val WALL_TILE = '#'
  val EMPTY_TILE = '.'
  val START_TILE = 'S'
  val END_TILE = 'E'
  val DIRECTIONS: Seq[Direction] = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0))

  override def part1(ctx: Context): Long =
    val grid = parseInput(ctx)
    val path = findPath(grid)
    val pathMap = path.zipWithIndex.toMap
    val cheats = createCheatMap(grid, path)
    val forwardCheats = cheats
      .collect:
        case (start, ends) if ends.exists(e => pathMap(start) < pathMap(e)) =>
          start -> ends.filter(e => pathMap(start) < pathMap(e))
      .toSeq
      .flatMap { case (k, vs) => vs.map(k -> _) }
      .toSet
    forwardCheats.count { case (a, b) => pathMap(b) - pathMap(a) - 2 >= saveThreshold }

  override def part2(ctx: Context): Long =
    val cheatLength = 20
    val grid = parseInput(ctx)
    val path = findPath(grid)
    val pathMap = path.zipWithIndex.toMap
    path.zipWithIndex.iterator
      .tapEach { case (_, i) => ctx.progress(i.toDouble / path.length) }
      .map { case (a, aIdx) =>
        pathMap.count { case (b, bIdx) =>
          val dist = a.manhattan(b)
          bIdx > aIdx && bIdx - aIdx - dist >= saveThreshold && dist <= cheatLength
        }
      }
      .sum

  def findPath(grid: Grid) =
    val start = grid.start
    val target = grid.end
    val visited = mut.Set.empty[Point]
    val queue = mut.Queue((start, 0, Seq(start)))
    var finalPath = Seq.empty[Point]
    while finalPath.isEmpty && queue.nonEmpty do
      val (pos, dist, path) = queue.dequeue()
      if (pos == target) finalPath = path
      else if visited.add(pos) then
        DIRECTIONS
          .map(pos + _)
          .filter(p => grid.isInBounds(p) && grid.tiles.contains(p))
          .foreach(p => queue.enqueue((p, dist + 1, path :+ p)))
    finalPath

  def createCheatMap(grid: Grid, path: Seq[Point]): Map[Point, Seq[Point]] =
    val cheatDirections = DIRECTIONS.map(_ * 2)
    val tiles = grid.tiles
    path
      .flatMap: start =>
        cheatDirections
          .map(start + _)
          .filter: p =>
            grid.isInBounds(p) && tiles.contains(p) && !tiles.contains(start.range(p).toSeq(1))
          .flatMap(end => Seq(start -> end, end -> start))
      .groupMap { case (a, b) => a } { case (a, b) => b }
      .toMap

  def render(grid: Grid, path: Set[Point], cheats: Map[Point, Seq[Point]] = Map.empty) =
    if isRenderEnabled then
      val rendered = (0 until grid.height.toInt + 1)
        .map: y =>
          (0 until grid.width.toInt + 1)
            .map: x =>
              val p = Point(x, y)
              if cheats.contains(p) then "X"
              else
                grid.tiles.get(p) match
                  case None                        => WALL_TILE
                  case Some(_) if path.contains(p) => 'O'
                  case _                           => ' '
            .mkString
        .mkString("\n")
      println(rendered)

  def parseInput(ctx: Context): Grid =
    val tiles = ctx.input.linesIterator.zipWithIndex.flatMap:
      case (line, y) =>
        line.zipWithIndex.collect { case (tile, x) if tile != WALL_TILE => Point(x, y) -> tile }
    Grid(tiles.toMap)

  case class Grid(val tiles: Map[Point, Char]):
    val start = tiles.collectFirst { case (p, START_TILE) => p }.get
    val end = tiles.collectFirst { case (p, END_TILE) => p }.get
    val width = tiles.keys.map(_.x).max + 1
    val height = tiles.keys.map(_.y).max + 1
    def isInBounds(p: Point): Boolean = p.x >= 0 && p.x < width && p.y >= 0 && p.y < height

  type Direction = Point
  case class Point(x: Long, y: Long):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    def *(scalar: Long): Point = Point(this.x * scalar, this.y * scalar)
    def *(other: Point): Point = Point(this.x * other.x, this.y * other.y)
    def manhattan(other: Point): Long = Math.abs(this.x - other.x) + Math.abs(this.y - other.y)
    def range(other: Point): Iterable[Point] =
      val (a, b) = (
        Point(Math.min(this.x, other.x), Math.min(this.y, other.y)),
        Point(Math.max(this.x, other.x), Math.max(this.y, other.y))
      )
      for x <- a.x to b.x; y <- a.y to b.y yield Point(x, y)
