package hu.sanraith.aoc2024.solution

/** Solution for https://adventofcode.com/2024/day/4 */
class Day04 extends Solution:
  override val title: String = "Ceres Search"

  val allDirections = for
    x <- -1 to 1
    y <- -1 to 1
    if !(x == 0 && y == 0)
  yield Point(x, y)

  override def part1(ctx: Context): Int =
    hitsAtCells("XMAS", allDirections, ctx).sum

  override def part2(ctx: Context): Int =
    val directions = allDirections.filter(p => p.x != 0 && p.y != 0)
    hitsAtCells("MAS", directions, ctx).count(_ >= 2)

  def hitsAtCells(word: String, directions: Seq[Point], ctx: Context): Seq[Int] =
    val lines = ctx.input.linesIterator.toSeq
    val width = lines.map(_.length).max
    val height = lines.length
    val delta = word.length / 2 // Put point to the middle of the word
    for
      x <- 0 until width
      y <- 0 until height
      start = Point(x, y)
    yield directions.count: direction =>
      (0 until word.length).forall: index =>
        val Point(x, y) = start + direction * (index - delta)
        lines.lift(y).flatMap(_.lift(x)).contains(word(index))

  case class Point(x: Int, y: Int):
    def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
    def *(scalar: Int): Point = Point(this.x * scalar, this.y * scalar)
