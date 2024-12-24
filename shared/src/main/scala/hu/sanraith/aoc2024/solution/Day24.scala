package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.mutable as mut

/** Solution for https://adventofcode.com/2024/day/24 */
class Day24 extends Solution:
  override val title: String = "Crossed Wires"

  override def part1(ctx: Context): Long =
    ???

  override def part2(ctx: Context): Long =
    val gates = parseInput(ctx)
    val xBits = bits("x", gates)
    val yBits = bits("y", gates)

    val truthTable = for
      x1 <- 0 to 1
      y1 <- 0 to 1
      x2 <- 0 to 1
      y2 <- 0 to 1
      z1e = (x1 + y1) % 2
      z2e = (x2 + y2 + (x1 + y1) / 2) % 2
    yield (x1, y1, x2, y2, z1e, z2e)

    val validity = xBits
      .zip(yBits)
      .collect { case (a: ValueGate, b: ValueGate) => (a, b) }
      .sliding(2)
      .map { case Seq((gx1, gy1), (gx2, gy2)) =>
        // println(gx1, gy1, gx2, gy2)
        var isValid = true
        for
          x1 <- 0 to 1
          y1 <- 0 to 1
          x2 <- 0 to 1
          y2 <- 0 to 1
          z1e = (x1 + y1) % 2
          z2e = (x2 + y2 + (x1 + y1) / 2) % 2
        do
          val gates1 = gates
            + (gx1.name -> gx1.copy(value = x1))
            + (gy1.name -> gy1.copy(value = y1))
            + (gx2.name -> gx2.copy(value = x2))
            + (gy2.name -> gy2.copy(value = y2))
          val z2g = gates1(s"z${gx2.idStr.get}")
          val cache = mut.Map.empty[String, Bit]
          val z2a = z2g.calc(gates1, cache)

          // if z2a == z2e then () // println(s"${z2g.name} pass for ${(x1, y1, x2, y2)}")
          // else println(s"${z2g.name} FAILED for ${(x1, y1, x2, y2)}")
          isValid = isValid && (z2a == z2e)
        println(gx2.idStr, isValid)
        gates(s"z${gx2.idStr.get}") -> isValid
      }
      .toMap

    // sus out bad connections, Zs should appear as the 2nd connection
    // gates.keySet
    //   .map(k =>
    //     k -> gates.values
    //       .collect { case g: DualGate => g }
    //       .filter(g => g.a == k || g.b == k)
    //       .map(_.name)
    //       .toSeq
    //       .sorted
    //   )
    //   .filter { case (k, v) => v.size > 1 && !gates(k).isInstanceOf[ValueGate] }
    //   .toSeq
    //   .sortBy(_._2.last)
    //   .foreach(println)

    // TODO backtrack
    // - find first wrong
    // - rewire until it is right (at most 1 switch)
    // continue with remaining rewires, keep this group out of the rewiring target
    rewire_start(gates)
    ???

  def rewire_start(gates: mut.Map[String, Gate]) =
    val validity = getValidityMap(gates).toSeq
    var firstInvalidOption = validity.collectFirst { case (g, isValid) if !isValid => g }
    val safeDependencies =
      validity.takeWhile(_._2).flatMap { case (g, _) => getDependencies(g.name, gates) }
    val unsafeNodes = gates.values.toSet -- safeDependencies

    while (firstInvalidOption.isDefined) {
      val firstInvalid = firstInvalidOption.get
      val unsafeDeps = getDependencies(firstInvalid.name, gates) -- safeDependencies
      println(safeDependencies.map(_.name).mkString(","))
      println(firstInvalid.name)
      println(getDependencies(firstInvalid.name, gates).map(_.name).mkString(","))
      println(unsafeDeps.map(_.name).mkString(","))

      val switches = for
        a <- unsafeDeps
        b <- unsafeNodes
        if a != b
      yield (a, b)

      switches.map { case (a, b) =>
        val altered = switch(gates, a, b)
        if hasCircle(altered) then () // println(s"${a.name}->${b.name} has circle")
        else
          // TODO only check one Z instead the whole validity map
          // TODO maybe need to check how value changes if the new dependencies change?
          // TODO or prefer the shorter dependencies? check if there is no n+1 param?
          val alteredValidity = getValidityMap(altered)
          val isValidNow = alteredValidity.collectFirst {
            case (g, v) if g.name == firstInvalid.name => v
          }.get
          if isValidNow then
            val invalidParams = getDependencies(firstInvalid.name, altered)
              .collect { case v: ValueGate if v.id.get > firstInvalid.id.get => v }
            if invalidParams.isEmpty then
              println(s"${firstInvalid.name} is valid after switch ${a.name}, ${b.name}")
            else
              println(
                s"${firstInvalid.name} ${a.name}-${b.name} valid, but depends on: ${invalidParams.map(_.name).mkString(",")}"
              )
          else () // println(s"${firstInvalid.name} is not valid after switch ${a.name}, ${b.name}")
      }

      firstInvalidOption = None
    }

    ???

    // TODO cannot switch z output this way..
  def switch(gates: mut.Map[String, Gate], a: Gate, b: Gate) =
    mut.Map.from(
      gates.values
        .map {
          case g: DualGate if g.a == a.name => g.copy_(b.name, g.b)
          case g: DualGate if g.b == a.name => g.copy_(g.a, b.name)
          case g: DualGate if g.a == b.name => g.copy_(a.name, g.b)
          case g: DualGate if g.b == b.name => g.copy_(g.a, a.name)
          case g                            => g
        }
        .map(x => x.name -> x)
    )

  def hasCircle(gates: mut.Map[String, Gate]) =
    val visitedOut = mut.Set.empty[String]
    gates.keySet.iterator.filterNot(visitedOut.contains).exists { start =>
      val queue = mut.Queue((start, Set.empty[String]))
      var isCircle = false
      while (queue.nonEmpty && !isCircle) {
        val (name, visited) = queue.dequeue()
        val g = gates(name)
        if visited.contains(g.name) then isCircle = true
        else
          visitedOut.add(g.name)
          g match {
            case next: DualGate =>
              val nextVisited = visited + name
              queue.enqueue((next.a, nextVisited))
              queue.enqueue((next.b, nextVisited))
            case _ => ()
          }
      }
      isCircle
    }

  def getValidityMap(gates: mut.Map[String, Gate]) =
    val xBits = bits("x", gates)
    val yBits = bits("y", gates)
    val validity = xBits
      .zip(yBits)
      .collect { case (a: ValueGate, b: ValueGate) => (a, b) }
      .sliding(2)
      .map { case Seq((gx1, gy1), (gx2, gy2)) =>
        var isValid = true
        for
          x1 <- 0 to 1
          y1 <- 0 to 1
          x2 <- 0 to 1
          y2 <- 0 to 1
          z1e = (x1 + y1) % 2
          z2e = (x2 + y2 + (x1 + y1) / 2) % 2
        do
          val gates1 = gates
            + (gx1.name -> gx1.copy(value = x1))
            + (gy1.name -> gy1.copy(value = y1))
            + (gx2.name -> gx2.copy(value = x2))
            + (gy2.name -> gy2.copy(value = y2))
          val z2g = gates1(s"z${gx2.idStr.get}")
          val cache = mut.Map.empty[String, Bit]
          val z2a = z2g.calc(gates1, cache)
          isValid = isValid && (z2a == z2e)
        gates(s"z${gx2.idStr.get}") -> isValid
      }
    validity

  def padded(x: Int) = x.toString.reverse.padTo(2, '0').reverse

  def distance(a: String, b: String, gates: mut.Map[String, Gate]): Int =
    if a == b then 0
    else
      gates(a) match
        case g: DualGate =>
          val d1 = distance(g.a, b, gates)
          val d2 = distance(g.b, b, gates)
          (d1, d2) match
            case (-1, -1) => -1
            case (a, -1)  => a + 1
            case (-1, b)  => b + 1
            case (a, b)   => math.min(a, b) + 1
        case _ => -1

  def getDependencies(name: String, gates: mut.Map[String, Gate]): Set[Gate] =
    val gate = gates(name)
    gate match
      case v: ValueGate => Set[Gate](v)
      case g: DualGate  => Set(g) ++ getDependencies(g.a, gates) ++ getDependencies(g.b, gates)

  def bits(prefix: String, gates: mut.Map[String, Gate]) =
    gates.values.filter(_.name.startsWith(prefix)).toSeq.sortBy(_.name)

  def calc(gates: mut.Map[String, Gate]) =
    val cache = mut.Map.empty[String, Bit]
    gates.values.foreach(g => g.calc(gates, cache))

  // def parseInput(ctx: Context): mut.Map[String, Gate] =
  def parseInput(ctx: Context) =
    val Seq(valuesStr, gatesStr) = ctx.input.split("""\R\R""").toSeq
    val gates = mut.Map.empty[String, Gate]
    valuesStr.linesIterator.toSeq.foreach:
      case s"$name: $valueStr" => gates(name) = ValueGate(name, valueStr.toInt)
    gatesStr.linesIterator.toSeq.foreach:
      case s"$a AND $b -> $name" => gates(name) = AndGate(name, a, b)
      case s"$a OR $b -> $name"  => gates(name) = OrGate(name, a, b)
      case s"$a XOR $b -> $name" => gates(name) = XorGate(name, a, b)
    gates

  val idxRegex = raw"[1-9]\d?".r
  val idxStrRegex = raw"\d\d".r
  type Bit = Int
  trait Gate:
    val name: String
    val idStr: Option[String] = idxStrRegex.findFirstIn(name)
    val id: Option[Int] = idxRegex.findFirstIn(name).map(_.toInt)

    def calc(gates: mut.Map[String, Gate], cache: mut.Map[String, Bit]): Bit

  trait DualGate extends Gate:
    val a: String
    val b: String
    def copy_(a: String, b: String): DualGate

  case class ValueGate(name: String, value: Bit) extends Gate:
    def calc(gates: mut.Map[String, Gate], cache: mut.Map[String, Bit]): Bit =
      cache.getOrElseUpdate(name, value)
  case class AndGate(name: String, a: String, b: String) extends DualGate:
    def copy_(a: String, b: String) = copy(a = a, b = b)
    def calc(gates: mut.Map[String, Gate], cache: mut.Map[String, Bit]): Bit =
      cache.getOrElseUpdate(name, gates(a).calc(gates, cache) & gates(b).calc(gates, cache))
  case class OrGate(name: String, a: String, b: String) extends DualGate:
    def copy_(a: String, b: String) = copy(a = a, b = b)
    def calc(gates: mut.Map[String, Gate], cache: mut.Map[String, Bit]): Bit =
      cache.getOrElseUpdate(name, gates(a).calc(gates, cache) | gates(b).calc(gates, cache))
  case class XorGate(name: String, a: String, b: String) extends DualGate:
    def copy_(a: String, b: String) = copy(a = a, b = b)
    def calc(gates: mut.Map[String, Gate], cache: mut.Map[String, Bit]): Bit =
      cache.getOrElseUpdate(name, gates(a).calc(gates, cache) ^ gates(b).calc(gates, cache))
