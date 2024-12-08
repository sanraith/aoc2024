package hu.sanraith.aoc2024.solution

import scala.collection.mutable

/** Solution for https://adventofcode.com/2024/day/8 */
class Day08 extends Solution:
  override val title: String = "Resonant Collinearity"

  val EMPTY_TILE = '.'

  override def part1(ctx: Context): Long =
    val grid = parseInput(ctx)
    val antennas =
      grid.tiles.groupBy { case (_, tile) => tile }.mapValues(_.map { case (p, _) => p })
    val antiNodes = antennas.flatMap { case (tile, positions) =>
      positions.toSeq
        .combinations(2)
        .flatMap { case Seq(a, b) =>
          val dir = b - a
          Seq(a - dir, b + dir).filter(grid.isInBounds)
        }
    }.toSet
    antiNodes.size

  override def part2(ctx: Context): Long =
    val grid = parseInput(ctx)
    val antennas =
      grid.tiles.groupBy { case (_, tile) => tile }.mapValues(_.map { case (p, _) => p })
    val antiNodes = antennas.flatMap { case (tile, positions) =>
      positions.toSeq
        .combinations(2)
        .flatMap { case Seq(a, b) =>
          val dir = b - a
          var dist = dir
          var isInBounds = true
          val groupedNodes = mutable.Set.empty[Point]
          while (isInBounds)
            val newNodes = Set(a + dist, b - dist).filter(grid.isInBounds)
            groupedNodes.addAll(newNodes)
            isInBounds = newNodes.nonEmpty
            dist += dir

          groupedNodes
        }
    }.toSet

    antiNodes.size

  def parseInput(ctx: Context) =
    val tiles = ctx.input.linesIterator.zipWithIndex.flatMap { case (line, y) =>
      line.zipWithIndex.collect { case (tile, x) if tile != EMPTY_TILE => Point(x, y) -> tile }
    }.toMap
    Grid(tiles, ctx.input.linesIterator.toSeq.head.length, ctx.input.linesIterator.toSeq.length)

  case class Grid(val tiles: Map[Point, Char], width: Int, height: Int):
    def isInBounds(p: Point): Boolean = p.x >= 0 && p.x < width && p.y >= 0 && p.y < height

  case class Point(x: Int, y: Int):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
    def *(scalar: Int): Point = Point(this.x * scalar, this.y * scalar)
