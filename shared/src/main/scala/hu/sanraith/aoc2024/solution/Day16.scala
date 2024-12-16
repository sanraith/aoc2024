package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.{mutable => mut}

/** Solution for https://adventofcode.com/2024/day/16 */
class Day16 extends Solution:
  override val title: String = "Reindeer Maze"

  val isRenderEnabled = false

  val WALL_TILE = '#'
  val EMPTY_TILE = '.'
  val START_TILE = 'S'
  val END_TILE = 'E'
  val STEP_COST = 1
  val TURN_COST = 1000
  val DIRECTIONS: Seq[Direction] = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0))
  val DIR_COUNT = DIRECTIONS.length

  override def part1(ctx: Context): Long =
    val grid = parseInput(ctx)
    val reindeer = findBestPaths(grid).head
    render(grid, reindeer.path.toSet)
    reindeer.score

  override def part2(ctx: Context): Long =
    val grid = parseInput(ctx)
    val bestPaths = findBestPaths(grid)
    val points = bestPaths.flatMap(_.path.sliding(2).flatMap { case Seq(a, b) => a.range(b) }).toSet
    render(grid, points)
    points.size

  def findBestPaths(grid: Grid): Seq[Reindeer] =
    val junctionMap = createJunctionMap(grid)
    val queue = mut.PriorityQueue(Reindeer(grid.start, 1, 0, Seq(grid.start)))(ScorePriority)
    val visitedScores = mut.Map.empty[(Point, Int), Long]
    val bestPaths = mut.Queue.empty[Reindeer]
    var foundBestPaths = false

    while !foundBestPaths && queue.nonEmpty do
      val reindeer @ Reindeer(pos, dirIdx, score, path) = queue.dequeue()
      if pos == grid.end then
        if bestPaths.headOption.exists(_.score < score) then foundBestPaths = true
        else bestPaths.enqueue(reindeer)
      else if visitedScores.get(pos, dirIdx).forall(score <= _) then
        visitedScores((pos, dirIdx)) = score
        (dirIdx - 1 to dirIdx + 1)
          .map(_ %% DIR_COUNT)
          .flatMap: nextDirIdx =>
            junctionMap(pos)
              .get(DIRECTIONS(nextDirIdx))
              .map: nextPos =>
                val turnCost = if nextDirIdx == dirIdx then 0 else TURN_COST
                val stepCost = pos.manhattan(nextPos) * STEP_COST
                val nextScore = score + turnCost + stepCost
                Reindeer(nextPos, nextDirIdx, nextScore, path :+ nextPos)
          .map(queue.enqueue(_))
    bestPaths.toSeq

  def createJunctionMap(grid: Grid): Map[Point, Map[Direction, Point]] =
    val junctions = grid.tiles.keySet.filter: p =>
      val vNeighbors = Seq(0, 2).count(di => grid.tiles.contains(p + DIRECTIONS(di)))
      val hNeighbors = Seq(1, 3).count(di => grid.tiles.contains(p + DIRECTIONS(di)))
      vNeighbors + hNeighbors != 2 || vNeighbors == hNeighbors // not | or -
    junctions.map { junction =>
      val edges = DIRECTIONS
        .map: dir =>
          var pos = junction + dir
          while (grid.tiles.contains(pos) && !junctions.contains(pos))
            pos += dir
          dir -> pos
        .filter { case (_, pos) => junctions.contains(pos) }
        .toMap
      junction -> edges
    }.toMap

  def render(grid: Grid, path: Set[Point]) = if (isRenderEnabled)
    val rendered = (0 until grid.height.toInt + 1)
      .map: y =>
        (0 until grid.width.toInt + 1)
          .map: x =>
            val p = Point(x, y)
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

  case class Reindeer(pos: Point, dirIdx: Int, score: Long, path: Seq[Point])
  object ScorePriority extends Ordering[Reindeer]:
    override def compare(a: Reindeer, b: Reindeer): Int = b.score.compare(a.score)

  case class Grid(val tiles: Map[Point, Char]):
    val start = tiles.collectFirst { case (p, START_TILE) => p }.get
    val end = tiles.collectFirst { case (p, END_TILE) => p }.get
    val width = tiles.keys.map(_.x).max + 1
    val height = tiles.keys.map(_.y).max + 1

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
