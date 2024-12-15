package hu.sanraith.aoc2024.solution

import scala.collection.mutable
import hu.sanraith.aoc2024.util._

/** Solution for https://adventofcode.com/2024/day/15 */
class Day15 extends Solution:
  override val title: String = "Warehouse Woes"

  val isRendering = false

  val EMPTY_TILE = '.'
  val ROBOT_TILE = '@'
  val WALL_TILE = '#'
  val BOX_TILE = 'O'
  val DIRECTION_TILES = "^>v<"
  val DIRECTIONS: Seq[Direction] = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0))
  val DIRECTION_MAP: Map[Char, Direction] = DIRECTION_TILES.zip(DIRECTIONS).toMap

  override def part1(ctx: Context): Long =
    val (grid, path) = parseInput(ctx)
    val walls = grid.tiles.collect { case (p, WALL_TILE) => p }.toSet
    val boxes = mutable.Set.from(grid.tiles.collect { case (p, BOX_TILE) => p })
    var robot = grid.tiles.collectFirst { case (p, ROBOT_TILE) => p }.get
    render(grid, robot, boxes.toSet, walls)

    path.iterator
      .tapEachWithIndex { case (_, idx) => ctx.progress(idx.toDouble / path.length) }
      .foreach { case dir =>
        val rd = robot * dir
        val boxesInDir = boxes
          .filter { case bp =>
            val bd = bp * dir
            (rd.x < bd.x && dir.y == 0 && robot.y == bp.y) || (rd.y < bd.y && dir.x == 0 && robot.x == bp.x)
          }
          .toSeq
          .sortBy(robot.manhattan)
          .foldLeft(Seq(robot)) {
            case (seg, current) if seg.last.manhattan(current) == 1 => seg :+ current
            case (seg, current)                                     => seg
          }
          .tail

        val nextRobotPos = robot + dir
        boxesInDir match
          case _ if walls.contains(nextRobotPos)               => ()
          case s if s.isEmpty && !walls.contains(nextRobotPos) => robot = nextRobotPos
          case s if s.nonEmpty && !walls.contains(s.last + dir) =>
            robot = nextRobotPos
            s.foreach(boxes.remove)
            s.map(_ + dir).foreach(boxes.add)
          case _ => ()

        render(grid, robot, boxes.toSet, walls)
      }

    boxes.map(p => p.x + p.y * 100).sum

  override def part2(ctx: Context): Long =
    ???

  def render(grid: Grid, robot: Point, boxes: Set[Point], walls: Set[Point]) = if (isRendering)
    val rendered = (0 until grid.height.toInt)
      .map: y =>
        (0 until grid.width.toInt)
          .map: x =>
            val p = Point(x, y)
            if (p == robot) 'X'
            else if (boxes.contains(p)) 'O'
            else if (walls.contains(p)) '#'
            else ' '
          .mkString
      .mkString("\n")
    println(rendered)

  def parseInput(ctx: Context) =
    val Seq(mapStr, pathStr) = ctx.input.split("""\R\R""").toSeq
    val tiles = mapStr.linesIterator.zipWithIndex.flatMap:
      case (line, y) =>
        line.zipWithIndex.collect { case (tile, x) if tile != EMPTY_TILE => Point(x, y) -> tile }
    val path = pathStr.collect { case c if DIRECTION_TILES.contains(c) => DIRECTION_MAP(c) }

    (Grid(tiles.toMap), path)

  case class Grid(val tiles: Map[Point, Char]):
    val width = tiles.keys.map(_.x).max + 1
    val height = tiles.keys.map(_.y).max + 1
    def isInBounds(p: Point): Boolean = p.x >= 1 && p.x < width - 1 && p.y >= 1 && p.y < height - 1

  def modWrap(a: Long, b: Long): Long = ((a % b) + b) % b
  type Direction = Point
  case class Point(x: Long, y: Long):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)

    def *(scalar: Long): Point = Point(this.x * scalar, this.y * scalar)
    def *(other: Point): Point = Point(this.x * other.x, this.y * other.y)

    def /(scalar: Long): Point = Point(this.x / scalar, this.y / scalar)
    def /(other: Point): Point = Point(this.x / other.x, this.y / other.y)

    def %(other: Point): Point = Point(modWrap(this.x, other.x), modWrap(this.y, other.y))

    def manhattan(other: Point): Long =
      val d = other - this
      Math.abs(d.x) + Math.abs(d.y)
