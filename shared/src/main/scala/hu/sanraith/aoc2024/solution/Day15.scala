package hu.sanraith.aoc2024.solution

import scala.collection.mutable
import hu.sanraith.aoc2024.util._

/** Solution for https://adventofcode.com/2024/day/15 */
class Day15 extends Solution:
  override val title: String = "Warehouse Woes"

  val isRendering = false

  val EAST = Point(1, 0)
  val EMPTY_TILE = '.'
  val ROBOT_TILE = '@'
  val WALL_TILE = '#'
  val BOX_TILE = 'O'
  val DIRECTIONS: Seq[Direction] = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0))
  val DIRECTION_MAP: Map[Char, Direction] = "^>v<".zip(DIRECTIONS).toMap

  override def part1(ctx: Context): Long =
    val (grid, path) = parseInput(ctx)
    var robot = grid.tiles.collectFirst { case (p, ROBOT_TILE) => p }.get
    val walls = grid.tiles.collect { case (p, WALL_TILE) => p }.toSet
    val boxes = mutable.Map.from:
      grid.tiles
        .collect { case (p, BOX_TILE) => Box(p, 1) }
        .flatMap(b => b.points.map(_ -> b))

    rearrangeBoxes(robot, path, walls, boxes)
    boxes.values.toSet.map { case Box(p, _) => p.x + p.y * 100 }.sum

  override def part2(ctx: Context): Long =
    val (grid, path) = parseInput(ctx)
    val scale = Point(2, 1)
    var robot = grid.tiles.collectFirst { case (p, ROBOT_TILE) => p * scale }.get
    val walls = grid.tiles
      .collect { case (p, WALL_TILE) => Seq(p * scale, p * scale + EAST) }
      .flatten
      .toSet
    val boxes = mutable.Map.from:
      grid.tiles
        .collect { case (p, BOX_TILE) => Box(p * scale, 2) }
        .flatMap(b => b.points.map(_ -> b))

    rearrangeBoxes(robot, path, walls, boxes)
    boxes.values.toSet.map { case Box(p, _) => p.x + p.y * 100 }.sum

  def rearrangeBoxes(
      start: Point,
      path: Seq[Direction],
      walls: Set[Point],
      boxes: mutable.Map[Point, Box]
  ) =
    var robot = start
    path.foreach: dir =>
      val next = robot + dir
      val boxStack = boxes.get(next).map(findBoxStack(_, dir, boxes)).getOrElse(Set.empty)
      val canMove = !walls.contains(next) &&
        boxStack.flatMap(_.points).forall(p => !walls.contains(p + dir))

      if (canMove)
        robot = next
        boxStack.flatMap(_.points).foreach(boxes.remove)
        boxStack
          .map(b => Box(b.start + dir, b.width))
          .foreach(b => b.points.foreach(boxes.addOne(_, b)))
      render(robot, boxes, walls)

  def findBoxStack(box: Box, dir: Direction, boxes: mutable.Map[Point, Box]): Set[Box] =
    val level = box.points.flatMap(p => boxes.get(p + dir)).filter(_ != box)
    level ++ level.flatMap(findBoxStack(_, dir, boxes)) + box

  def render(robot: Point, boxes: mutable.Map[Point, Box], walls: Set[Point]) =
    if (isRendering)
      val grid = Grid(walls.map(_ -> WALL_TILE).toMap)
      val rendered = (0 until grid.height.toInt)
        .map: y =>
          (0 until grid.width.toInt)
            .map: x =>
              val p = Point(x, y)
              if (p == robot) 'X'
              else if (walls.contains(p)) '#'
              else
                boxes
                  .get(p)
                  .map:
                    case box if (box.width == 1) => 'O'
                    case box                     => "[]" (box.points.toSeq.sortBy(_.x).indexOf(p))
                  .getOrElse(' ')
            .mkString
        .mkString("\n")
      println(rendered + "\n")

  def parseInput(ctx: Context) =
    val Seq(mapStr, pathStr) = ctx.input.split("""\R\R""").toSeq
    val tiles = mapStr.linesIterator.zipWithIndex.flatMap:
      case (line, y) =>
        line.zipWithIndex.collect { case (tile, x) if tile != EMPTY_TILE => Point(x, y) -> tile }
    val path = pathStr.flatMap(DIRECTION_MAP.get)

    (Grid(tiles.toMap), path)

  case class Box(start: Point, width: Int):
    val points: Set[Point] = (0 until width).map(start + EAST * _).toSet

  case class Grid(val tiles: Map[Point, Char]):
    val width = tiles.keys.map(_.x).max + 1
    val height = tiles.keys.map(_.y).max + 1

  type Direction = Point
  case class Point(x: Long, y: Long):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    def *(scalar: Long): Point = Point(this.x * scalar, this.y * scalar)
    def *(other: Point): Point = Point(this.x * other.x, this.y * other.y)
