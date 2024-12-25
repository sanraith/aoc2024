package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.mutable as mut
import scala.collection.parallel.CollectionConverters._
import scala.util.Try
import scala.util.Success
import scala.util.Failure

/** Solution for https://adventofcode.com/2024/day/24 */
class Day24 extends Solution:
  override val title: String = "Crossed Wires"

  override def part1(ctx: Context): Long =
    var gates = parseInput(ctx)
    getNumber("z", gates)

  override def part2(ctx: Context): String =
    val gates = parseInput(ctx).values
      .map: // Set input gates to zero
        case v: ValueGate => ValueGate(v.name, 0)
        case g            => g
      .map(g => g.name -> g)
      // Convert from (gate) to (_gate -> resultGate) for easier swaps
      .map { case (k, v) => "_" + k -> v }
      .flatMap { //
        case (_n, g: ValueGate) => Seq(ValueGate(_n, g.value), ResultGate(g.name, _n))
        case (_n, g: AndGate)   => Seq(AndGate(_n, g.a, g.b), ResultGate(g.name, _n))
        case (_n, g: OrGate)    => Seq(OrGate(_n, g.a, g.b), ResultGate(g.name, _n))
        case (_n, g: XorGate)   => Seq(XorGate(_n, g.a, g.b), ResultGate(g.name, _n))
      }
      .map(g => g.name -> g)
      .toMap
    val swaps = backtrackSwaps(gates, ctx)
    swaps.flatMap { case (a, b) => Seq(a, b) }.toSeq.sorted.mkString(",")

  def getBinNumber(namePrefix: String, gates: Map[String, Gate]): Seq[Int] =
    val cache = mut.Map.empty[String, Bit]
    gates.values
      .filter(_.name.startsWith(namePrefix))
      .toSeq
      .sortBy(_.name)
      .reverse
      .map(_.calc(gates, cache).get)

  def getNumber(namePrefix: String, gates: Map[String, Gate]): Long =
    val binary = getBinNumber(namePrefix, gates).mkString
    java.lang.Long.parseLong(binary, 2)

  def backtrackSwaps(
      gates: Map[String, Gate],
      ctx: Context,
      swapHistory: Seq[(String, String)] = Seq.empty
  ): Seq[(String, String)] =
    ctx.progress(swapHistory.length.toDouble / 4)
    val invalidOption = bits("z", gates).collectFirst:
      case g: ResultGate if !getValidities(gates, Some(g.id.get)).next()._2 => g

    (invalidOption, swapHistory.length) match
      case (None, _)                        => swapHistory // solution found
      case (Some(_), count) if count >= 4   => Seq.empty // not a solution, too many swaps
      case (Some(invalid), switchHistCount) =>
        // println((swapHistory.map(_.toString) :+ invalid.name).mkString(", "))
        val invalidId = invalid.id.get
        val resultGates = gates.values.collect { case r: ResultGate => r }.toSet
        val deps = getDependencies(invalid.name, gates).collect { case r: ResultGate => r }.toSet
        val safeDeps =
          if invalidId == 0 then Set.empty
          else
            val prevName = s"z${padded(invalidId - 1)}"
            getDependencies(prevName, gates).collect { case r: ResultGate => r }.toSet

        val aDeps = deps -- safeDeps
        val bDeps = resultGates -- safeDeps
        val swaps = for
          a <- aDeps.iterator.filterNot(_.isSwapped)
          b <- bDeps.filterNot(_.isSwapped)
          if a != b
          altered = switch(gates, a, b)
          (_, isValid) = getValidities(altered, Some(invalidId)).next()
          if isValid
        yield (a, b, altered)

        swaps
          .map { case (a, b, altered) =>
            backtrackSwaps(altered, ctx, swapHistory :+ (a.name, b.name))
          }
          .find(_.nonEmpty)
          .getOrElse(Seq.empty)

  def switch(gates: Map[String, Gate], a: ResultGate, b: ResultGate): Map[String, Gate] =
    gates.values
      .map:
        case ResultGate(name, source) if source == a.source => ResultGate(name, b.source)
        case ResultGate(name, source) if source == b.source => ResultGate(name, a.source)
        case g                                              => g
      .map { g => g.name -> g }
      .toMap

  def getValidities(gates: Map[String, Gate], targetIdx: Option[Int] = None) =
    val xBits = bits("_x", gates)
    val yBits = bits("_y", gates)
    val dropCount = targetIdx.getOrElse(0)
    val takeCount = targetIdx.map(_ => 1).getOrElse(xBits.size)
    (xBits.head +: xBits :+ ValueGate(s"x${xBits.length}", 0))
      .zip(yBits.head +: yBits :+ ValueGate(s"y${xBits.length}", 0))
      .collect { case (a: ValueGate, b: ValueGate) => (a, b) }
      .sliding(2)
      .drop(dropCount)
      .take(takeCount)
      .map { case Seq((gx1, gy1), (gx2, gy2)) =>
        val isFirst = gx2.id.get == 0
        val isLast = gx2.id.get == xBits.length
        val idStr = gx2.idStr.get
        val truthTableResults = for
          x1 <- (0 to (if isFirst then 0 else 1)).iterator
          y1 <- 0 to (if isFirst then 0 else 1)
          x2 <- 0 to (if isLast then 0 else 1)
          y2 <- 0 to (if isLast then 0 else 1)
          z2e = (x2 + y2 + (x1 + y1) / 2) % 2
        yield
          val gates1 = gates
            + (gx1.name -> gx1.copy(value = x1))
            + (gy1.name -> gy1.copy(value = y1))
            + (gx2.name -> gx2.copy(value = x2))
            + (gy2.name -> gy2.copy(value = y2))
          val z2g = gates1(s"z$idStr")
          val cache = mut.Map.empty[String, Bit]
          val z2a = z2g.calc(gates1, cache).getOrElse(-1)
          z2a == z2e
        val isValid = truthTableResults.forall(identity)
        gates(s"z$idStr") -> isValid
      }

  def padded(x: Int): String = x.toString.reverse.padTo(2, '0').reverse

  def getDependencies(name: String, gates: Map[String, Gate]): Set[Gate] =
    val gate = gates(name)
    gate match
      case v: ValueGate  => Set[Gate](v)
      case g: DualGate   => Set(g) ++ getDependencies(g.a, gates) ++ getDependencies(g.b, gates)
      case g: ResultGate => Set(g) ++ getDependencies(g.source, gates)

  def bits(prefix: String, gates: Map[String, Gate]): Seq[Gate] =
    gates.values.filter(_.name.startsWith(prefix)).toSeq.sortBy(_.name)

  def parseInput(ctx: Context) =
    val Seq(valuesStr, gatesStr) = ctx.input.split("""\R\R""").toSeq
    val gates = mut.Map.empty[String, Gate]
    valuesStr.linesIterator.toSeq.foreach:
      case s"$name: $valueStr" => gates(name) = ValueGate(name, valueStr.toInt)
    gatesStr.linesIterator.toSeq.foreach:
      case s"$a AND $b -> $name" => gates(name) = AndGate(name, a, b)
      case s"$a OR $b -> $name"  => gates(name) = OrGate(name, a, b)
      case s"$a XOR $b -> $name" => gates(name) = XorGate(name, a, b)
    gates.toMap

  val idxRegex = raw"\d\d".r
  type Bit = Int
  trait Gate:
    val name: String
    val idStr: Option[String] = idxRegex.findFirstIn(name)
    val id: Option[Int] = idStr.map(_.toInt)
    def calc(
        gates: Map[String, Gate],
        cache: mut.Map[String, Bit],
        visited: Set[String] = Set()
    ): Try[Bit]

  trait DualGate extends Gate:
    val a: String
    val b: String
    def op(a: Bit, b: Bit): Bit
    def calc(
        gates: Map[String, Gate],
        cache: mut.Map[String, Bit],
        visited: Set[String] = Set()
    ): Try[Bit] =
      if visited.contains(name) then
        Failure(new RuntimeException(s"Circular reference detected at gate $name"))
      else
        for
          aResult <- gates(a).calc(gates, cache, visited + name)
          bResult <- gates(b).calc(gates, cache, visited + name)
        yield cache.getOrElseUpdate(name, op(aResult, bResult))

  case class ValueGate(name: String, value: Bit) extends Gate:
    def calc(g: Map[String, Gate], cache: mut.Map[String, Bit], v: Set[String] = Set()): Try[Bit] =
      Success(cache.getOrElseUpdate(name, value))

  case class ResultGate(name: String, source: String) extends Gate:
    val isSwapped = "_" + name != source
    def calc(gates: Map[String, Gate], c: mut.Map[String, Bit], v: Set[String] = Set()): Try[Bit] =
      gates(source).calc(gates, c, v)

  case class AndGate(name: String, a: String, b: String) extends DualGate:
    def op(a: Bit, b: Bit): Bit = a & b

  case class OrGate(name: String, a: String, b: String) extends DualGate:
    def op(a: Bit, b: Bit): Bit = a | b

  case class XorGate(name: String, a: String, b: String) extends DualGate:
    def op(a: Bit, b: Bit): Bit = a ^ b
