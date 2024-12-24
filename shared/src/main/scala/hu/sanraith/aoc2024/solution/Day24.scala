package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.mutable as mut

/** Solution for https://adventofcode.com/2024/day/24 */
class Day24 extends Solution:
  override val title: String = "Crossed Wires"

  override def part1(ctx: Context): Long =
    val gates = parseInput(ctx)
    val binary =
      gates.values.filter(_.name.startsWith("z")).toSeq.sortBy(_.name).reverse.map(_.value).mkString
    java.lang.Long.parseLong(binary, 2)

  override def part2(ctx: Context): Long =
    val gates = parseInput(ctx)

    ???

  def parseInput(ctx: Context): mut.Map[String, Gate] =
    val Seq(valuesStr, gatesStr) = ctx.input.split("""\R\R""").toSeq
    val gates = mut.Map.empty[String, Gate]
    valuesStr.linesIterator.toSeq.foreach:
      case s"$name: $valueStr" => gates(name) = ValueGate(name, valueStr.toInt)
    gatesStr.linesIterator.toSeq.foreach:
      case s"$a AND $b -> $name" => gates(name) = AndGate(name, a, b, gates)
      case s"$a OR $b -> $name"  => gates(name) = OrGate(name, a, b, gates)
      case s"$a XOR $b -> $name" => gates(name) = XorGate(name, a, b, gates)
    gates

  type GateValue = Int
  trait Gate(nameIn: String):
    val name: String = nameIn
    lazy val value: GateValue
    val sources: Seq[Gate] = Seq.empty

  case class ValueGate(nameIn: String, constValue: GateValue) extends Gate(nameIn):
    lazy val value = constValue
  case class AndGate(nameIn: String, a: String, b: String, gates: mut.Map[String, Gate])
      extends Gate(nameIn):
    lazy val value = gates(a).value & gates(b).value
  case class OrGate(nameIn: String, a: String, b: String, gates: mut.Map[String, Gate])
      extends Gate(nameIn):
    lazy val value = gates(a).value | gates(b).value
  case class XorGate(nameIn: String, a: String, b: String, gates: mut.Map[String, Gate])
      extends Gate(nameIn):
    lazy val value = gates(a).value ^ gates(b).value
