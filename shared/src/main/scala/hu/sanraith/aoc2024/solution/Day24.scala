package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.util._
import scala.collection.mutable as mut

/** Solution for https://adventofcode.com/2024/day/24 */
class Day24 extends Solution:
  override val title: String = "Crossed Wires"

  override def part1(ctx: Context): Long =
    val gates = parseInput(ctx)
    getNumber("z", gates)

  override def part2(ctx: Context): Long =
    val gates = parseInput(ctx)
    val x = getNumber("x", gates)
    val y = getNumber("y", gates)

    val zExpected = x + y
    val zExpectedBin =
      zExpected.toBinaryString.reverse.padTo(32, '0').reverse.map(_.asDigit).reverse.toList
    val zActual = getNumber("z", gates)
    val zActualBin = getBinNumber("z", gates).reverse.toList

    zActualBin.zip(zExpectedBin).zipWithIndex.collectFirst {
      case ((a, b), i) if a != b =>
        val idxStr = "z" + i.toString().reverse.padTo(2, '0').reverse
        trace(idxStr, gates)
    }

    println(zActual)
    println(zExpected)
    println(zActualBin)
    println(zExpectedBin)

    ???

  def trace(name: String, gates: mut.Map[String, Gate]): Unit =
    gates(name) match
      case ValueGate(name, constValue, a, b) => () // println(s"${name}\n")
      case g: Gate                           =>
        // println(s"${g.name} <- ${g.a}, ${g.b}")
        val gName = g.getClass.getSimpleName.replace("Gate", "").toUpperCase + " " + g.name
        println(s"${g.a} --> ${name}[$gName]")
        println(s"${g.b} --> ${name}[$gName]")

        trace(g.a, gates)
        trace(g.b, gates)

  def getBinNumber(namePrefix: String, gates: mut.Map[String, Gate]): Seq[Int] =
    gates.values.filter(_.name.startsWith(namePrefix)).toSeq.sortBy(_.name).reverse.map(_.value)

  def getNumber(namePrefix: String, gates: mut.Map[String, Gate]): Long =
    val binary = getBinNumber(namePrefix, gates).mkString
    java.lang.Long.parseLong(binary, 2)

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
  trait Gate():
    val name: String
    val a: String
    val b: String
    lazy val value: GateValue
    val sources: Seq[Gate] = Seq.empty

  case class ValueGate(name: String, constValue: GateValue, a: String = "", b: String = "")
      extends Gate:
    lazy val value = constValue
  case class AndGate(name: String, a: String, b: String, gates: mut.Map[String, Gate]) extends Gate:
    lazy val value = gates(a).value & gates(b).value
  case class OrGate(name: String, a: String, b: String, gates: mut.Map[String, Gate]) extends Gate:
    lazy val value = gates(a).value | gates(b).value
  case class XorGate(name: String, a: String, b: String, gates: mut.Map[String, Gate]) extends Gate:
    lazy val value = gates(a).value ^ gates(b).value
