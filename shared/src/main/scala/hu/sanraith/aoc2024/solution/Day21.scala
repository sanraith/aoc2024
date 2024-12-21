package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.mutable as mut
import scala.collection.MapView
import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.immutable.ParSeq

/** Solution for https://adventofcode.com/2024/day/21 */
class Day21 extends Solution:
  override val title: String = "Keypad Conundrum"

  val TILE_A = 'A'
  val TILE_EMPTY = ' '
  val KEYPAD: Grid = toGrid(Seq("789", "456", "123", " 0A"))
  val DIRPAD: Grid = toGrid(Seq(" ^A", "<v>"))
  val DIRECTIONS: Seq[Direction] = Seq(Point(0, -1), Point(1, 0), Point(0, 1), Point(-1, 0))
  val KEY_DIR_MAP: Map[Char, Direction] = "^>v<".zip(DIRECTIONS).toMap
  val DIR_KEY_MAP: Map[Direction, Char] = KEY_DIR_MAP.map { case (k, v) => v -> k }

  override def part1(ctx: Context): Long = findComplexitySum(dirPadRobotCount = 2, ctx)
  override def part2(ctx: Context): Long = findComplexitySum(dirPadRobotCount = 25, ctx)

  def findComplexitySum(dirPadRobotCount: Int, ctx: Context): Long =
    val moveMap = getMoveMap()
    val codes = ctx.input.linesIterator.toSeq
    codes.zipWithIndex.map { case (code, codeIdx) =>
      findCodeComplexity(codes, codeIdx, moveMap, dirPadRobotCount, ctx)
    }.sum

  def findCodeComplexity(
      codes: Seq[String],
      codeIdx: Int,
      moveMap: Map[String, Seq[Conversion]],
      dirPadRobotCount: Int,
      ctx: Context
  ) =
    val code = codes(codeIdx)
    val codePaths = findPaths(KEYPAD, code)
    val pathCombinations = combinations(codePaths).toSet
    var dirPadSequences = filterBestSequences(convertToButtonSequence(pathCombinations))

    val bestSequenceLength = dirPadSequences.zipWithIndex.map { case (dirPadSequence, startIdx) =>
      val progress = codeIdx.toDouble / codes.length +
        (1.0 / codes.length * startIdx.toDouble / dirPadSequences.length)
      ctx.progress(progress)

      var pairCountMaps = ParSeq(toPairCountMap(dirPadSequence))
      (1 to dirPadRobotCount).foreach { _ =>
        val pairCountGroups = pairCountMaps.par
          .flatMap { pairCounts =>
            val conversions =
              combinations(pairCounts.toSeq.map { case (from, _) => moveMap(from) })
                .map(_.map(x => x.from -> x.to).toMap)

            conversions.map { conversions =>
              pairCounts.toSeq
                .flatMap { case (from, fromCount) =>
                  val result = conversions(from)
                  toPairCountMap(result).map { case (to, toCount) =>
                    to -> (fromCount * toCount)
                  }
                }
                .groupMap { case (k, v) => k } { case (k, v) => v }
                .mapValues(_.sum)
            }
          }
          .groupBy(_.values.sum)
        val min = pairCountGroups.keySet.min
        pairCountMaps = pairCountGroups(min)
      }
      pairCountMaps.head.values.sum
    }.min

    val numCodePart = raw"[1-9]\d*".r.findFirstIn(code).getOrElse("0").toLong
    numCodePart * bestSequenceLength

  def toPairCountMap(s: String): MapView[String, Long] =
    (TILE_A + s).sliding(2).toSeq.groupBy(identity).mapValues(_.length.toLong)

  def combinations[T](seq: Seq[Seq[T]]): Seq[Seq[T]] = seq match
    case Seq() => Seq(Seq())
    case head +: tail =>
      for
        h <- head
        t <- combinations(tail)
      yield h +: t

  def getMoveMap(): Map[String, Seq[Conversion]] =
    val positions = DIRPAD.tiles.toMap
    val paths = for
      (from, fromTile) <- positions
      (to, toTile) <- positions
    yield
      var pos = from
      val targetVec = to - pos
      val targetDir = Point(math.signum(targetVec.x), math.signum(targetVec.y))
      val stepX = Point(targetVec.x, 0)
      val stepY = Point(0, targetVec.y)
      var pathsBetweenPoints = Set(Seq(stepX, stepY), Seq(stepY, stepX))
        .filter { case Seq(a, b) =>
          val p1 = pos + a
          val p2 = p1 + b
          pos
            .range(p1)
            .forall(DIRPAD.tiles.contains) && p1.range(p2).forall(DIRPAD.tiles.contains)
        }
        .map(_.filter(_ != Point(0, 0)))
      if pathsBetweenPoints.forall(_.isEmpty) then pathsBetweenPoints = Set(Seq(Point(0, 0)))
      pos = to
      s"$fromTile$toTile" -> convertToButtonSequence(pathsBetweenPoints.map(Seq(_)))

    paths.map { case (k, v) => k -> v.map(Conversion(k, _)) }.toMap

  def filterBestSequences(lines: Seq[String]): Seq[String] =
    val shortestSequences = lines.groupBy(_.length).toSeq.sortBy(_._1).head._2
    val groups = shortestSequences
      .groupBy { line =>
        val l = line + "X"
        val (groupCount, _) = l.tail.foldLeft((0, l.head)) {
          case ((groups, last), c) if c == last => (groups, c)
          case ((groups, last), c)              => (groups + 1, c)
        }
        groupCount
      }
    val (_, bestLines) = groups.toSeq.sortBy { case (groupCount, _) => groupCount }.head
    bestLines

  def convertToButtonSequence(combinations: Iterable[Seq[Step]]): Seq[String] =
    val keyCombinations = combinations.map { combination =>
      combination.flatMap { case steps =>
        steps.flatMap { vec =>
          val (dir, count) = vec.toDirectionCount
          DIR_KEY_MAP.get(dir).map(_.toString.repeat(count)).getOrElse("")
        } :+ TILE_A
      }.mkString
    }
    keyCombinations.toSeq

  def findPaths(onGrid: Grid, targetCode: String): Seq[Seq[Step]] =
    var pos = onGrid.start
    targetCode.map: c =>
      val targetPos = onGrid.keyMap(c)
      val targetVec = targetPos - pos
      val targetDir = Point(math.signum(targetVec.x), math.signum(targetVec.y))

      val stepX = Point(targetVec.x, 0)
      val stepY = Point(0, targetVec.y)
      var pathsBetweenPoints = Seq(Seq(stepX, stepY), Seq(stepY, stepX))
        .filter { case Seq(a, b) =>
          val p1 = pos + a
          val p2 = p1 + b
          pos.range(p1).forall(onGrid.tiles.contains) && p1.range(p2).forall(onGrid.tiles.contains)
        }
        .map(_.filter(_ != Point(0, 0)))
      if pathsBetweenPoints.forall(_.isEmpty) then pathsBetweenPoints = Seq(Seq(Point(0, 0)))

      pos = targetPos
      pathsBetweenPoints

  def toGrid(lines: Seq[String]): Grid =
    val tiles = lines.zipWithIndex.flatMap { case (l, y) =>
      l.zipWithIndex.collect { case (c, x) if c != TILE_EMPTY => Point(x, y) -> c }
    }.toMap
    Grid(tiles)

  case class Conversion(from: String, to: String)

  case class Grid(val tiles: Map[Point, Char]):
    val keyMap = tiles.map { case (p, t) => t -> p }
    val start = tiles.collectFirst { case (p, t) if t == TILE_A => p }.get
    val width = tiles.keys.map(_.x).max + 1
    val height = tiles.keys.map(_.y).max + 1

  type Step = Seq[Direction]
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
    def toDirectionCount: (Direction, Int) =
      (Point(math.signum(x), math.signum(y)), math.max(math.abs(x.toInt), math.abs(y.toInt)))
