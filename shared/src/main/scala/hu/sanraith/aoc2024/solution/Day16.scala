package hu.sanraith.aoc2024.solution

import scala.collection.mutable

/** Solution for https://adventofcode.com/2024/day/16 */
class Day16 extends Solution:
  override val title: String = "Reindeer Maze"

  val WALL_TILE = '#'
  val EMPTY_TILE = '.'
  val START_TILE = 'S'
  val END_TILE = 'E'
  val EAST = Point(1, 0)
  val DIRECTIONS: Seq[Direction] = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0))

  override def part1(ctx: Context): Long =
    val grid = parseInput(ctx)
    val stepCost = 1L
    val turnCost = 1000L

    val visited = mutable.Set.empty[(Point, Int)]
    val queue = mutable.PriorityQueue(State(grid.start, 1, 0, Seq(grid.start)))(StatePriority)
    var goal: Option[State] = None

    while (goal.isEmpty && queue.nonEmpty)
      // println(queue.clone().dequeueAll.map(_.points).mkString(", "))
      val State(pos, dirIdx, points, path) = queue.dequeue()
      if (pos == grid.end) goal = Some(State(pos, dirIdx, points, path))
      else if (visited.add(pos, dirIdx))
        Seq((dirIdx, 1, stepCost), (dirIdx - 1, 0, turnCost), (dirIdx + 1, 0, turnCost))
          .map { case (di, step, cost) =>
            val next = pos + DIRECTIONS(modWrap(di, 4)) * step
            State(next, di, points + cost, path /* :+ next*/ )
          }
          .collect { case state if grid.tiles.contains(state.pos) => queue.enqueue(state) }

    // goal.map(g => render(grid, g.path.toSet))
    goal.map(_.points).getOrElse(-1)

  override def part2(ctx: Context): Long =
    ???

  def render(grid: Grid, path: Set[Point]) =
    val rendered = (0 until grid.height.toInt + 1)
      .map: y =>
        (0 until grid.width.toInt + 1)
          .map: x =>
            val p = Point(x, y)
            grid.tiles.get(p) match
              case None                        => WALL_TILE
              case Some(_) if path.contains(p) => '.'
              case _                           => ' '
          .mkString
      .mkString("\n")
    println(rendered)

  case class State(pos: Point, dirIdx: Int, points: Long, path: Seq[Point])
  object StatePriority extends Ordering[State]:
    override def compare(a: State, b: State): Int = b.points.compare(a.points)

  def parseInput(ctx: Context) =
    val tiles = ctx.input.linesIterator.zipWithIndex.flatMap:
      case (line, y) =>
        line.zipWithIndex.collect { case (tile, x) if tile != WALL_TILE => Point(x, y) -> tile }
    Grid(tiles.toMap)

  def modWrap(a: Int, b: Int): Int = ((a % b) + b) % b

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
