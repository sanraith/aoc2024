package hu.sanraith.aoc2024.solution

import scala.collection.{mutable => mut}

/** Solution for https://adventofcode.com/2024/day/17 */
class Day17 extends Solution:
  override val title: String = "Chronospatial Computer"

  val OPERATORS: Map[Int, Instruction] =
    Seq(Adv, Bxl, Bst, Jnz, Bxc, Out, Bdv, Cdv).map(x => x.opCode -> x).toMap

  override def part1(ctx: Context): String =
    val (program, registers) = parseInput(ctx)
    while registers.ip < program.length do executeNext(program, registers)
    registers.output.mkString(",")

  override def part2(ctx: Context): Long =
    val (program, initialRegisters) = parseInput(ctx)
    val GROWTH_FACTOR = 8 // We find a new element of the result ~every 8^x iteration.

    var registerA = -1L
    var foundSolution = false
    while !foundSolution do
      registerA += 1
      val registers = initialRegisters.copy(a = registerA)
      val output = registers.output

      var isPartialMatch = true
      while registers.ip < program.length && isPartialMatch do
        executeNext(program, registers) match
          case Out => isPartialMatch = program.containsSlice(output)
          case _   => ()

      if isPartialMatch && program.drop(program.length - output.length).sameElements(output) then
        ctx.progress(output.length.toDouble / program.length)
        if output.length == program.length then foundSolution = true
        else registerA = Math.max(1, registerA) * GROWTH_FACTOR - 1

    registerA

  def executeNext(program: Seq[Int], registers: Registers): Instruction =
    val instruction = OPERATORS(program(registers.ip))
    val param = program(registers.ip + 1)
    instruction.exec(param, registers)
    registers.ip += 2
    instruction

  def parseInput(ctx: Context): (Seq[Int], Registers) =
    val registerRegex = """(?<=Register .:\ )-?\d+""".r
    val programRegex = """(?<=Program: ).*""".r
    val Seq(a, b, c) = registerRegex.findAllIn(ctx.input).map(_.toLong).toSeq
    val program = programRegex.findAllIn(ctx.input).flatMap(_.split(',')).map(_.toInt).toSeq
    (program, Registers(a, b, c))

  case class Registers(var a: Long, var b: Long, var c: Long):
    var ip: Int = 0
    val output: mut.Queue[Int] = mut.Queue.empty[Int]

  trait Instruction(val opCode: Int):
    def exec(p: Int, r: Registers): Unit
    def combo(p: Int, r: Registers): Long = p match
      case p if p >= 0 && p <= 3 => p
      case 4                     => r.a
      case 5                     => r.b
      case 6                     => r.c
      case _                     => throw Exception("Invalid combo operand")

  case object Adv extends Instruction(0):
    def exec(p: Int, r: Registers) = r.a = (r.a / Math.pow(2, combo(p, r))).toLong

  case object Bxl extends Instruction(1):
    def exec(p: Int, r: Registers) = r.b = r.b ^ p

  case object Bst extends Instruction(2):
    def exec(p: Int, r: Registers) = r.b = combo(p, r) % 8

  case object Jnz extends Instruction(3):
    def exec(p: Int, r: Registers) = if r.a != 0 then r.ip = p - 2

  case object Bxc extends Instruction(4):
    def exec(p: Int, r: Registers) = r.b = r.b ^ r.c

  case object Out extends Instruction(5):
    def exec(p: Int, r: Registers) = r.output.enqueue((combo(p, r) % 8).toInt)

  case object Bdv extends Instruction(6):
    def exec(p: Int, r: Registers) = r.b = (r.a / Math.pow(2, combo(p, r))).toLong

  case object Cdv extends Instruction(7):
    def exec(p: Int, r: Registers) = r.c = (r.a / Math.pow(2, combo(p, r))).toLong
