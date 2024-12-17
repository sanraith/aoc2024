package hu.sanraith.aoc2024.solution

import scala.collection.{mutable => mut}
import scala.compiletime.ops.double

import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/** Solution for https://adventofcode.com/2024/day/17 */
class Day17 extends Solution:
  override val title: String = "Chronospatial Computer"

  val OPERATORS: Map[Int, Operand] =
    Seq(Adv, Bxl, Bst, Jnz, Bxc, Out, Bdv, Cdv).map(x => x.opCode -> x).toMap

  override def part1(ctx: Context): String =
    val (registers, program) = parseInput(ctx)
    while registers.ip < program.length do
      val op = OPERATORS(program(registers.ip))
      val p = program(registers.ip + 1)
      op.exec(p, registers)
      registers.ip += 2
    registers.output.mkString(",")

  override def part2(ctx: Context): Long =
    val (originalRegisters, program) = parseInput(ctx)
    val numThreads = 16 // Number of parallel threads
    val rangePerThread = 10000000L // Range of values each thread will test

    var start = 105553116266496L // 0L
    var found2 = false
    var finalResult = -1L
    while !found2 do {
      println(s"At $start...")
      val futures = (0 until numThreads).map { i =>
        Future {
          var testA = start + i * rangePerThread
          val maxA = start + (i + 1) * rangePerThread
          var found = false
          while (!found && testA < maxA) {
            // if (testA % 1000000 == 0) println(s"Thread $i at $testA...")
            if (runTest(testA, program, originalRegisters)) {
              found = true
              testA
            } else {
              testA += 1
            }
          }
          if (found) Some(testA) else None
        }
      }

      Await.result(Future.sequence(futures), Duration.Inf).flatten.sorted.headOption match
        case Some(value) => finalResult = value; found2 = true
        case None        => ()
      start += rangePerThread * numThreads
    }
    finalResult

  def runTest(testA: Long, program: Seq[Int], originalRegisters: Registers): Boolean = {
    val registers = Registers(testA, originalRegisters.b, originalRegisters.c)
    // originalRegisters.copy(a = testA)
    var canFind = true
    while (canFind && registers.ip < program.length) {
      val op = OPERATORS(program(registers.ip))
      val p = program(registers.ip + 1)
      op.exec(p, registers)
      registers.ip += 2

      op match {
        case Out if registers.output.length < program.length =>
          canFind = program.lift(registers.output.length - 1).contains(registers.output.last)
        case _ => ()
      }
    }
    canFind && registers.output.length == program.length
  }

  def parseInput(ctx: Context) =
    val registerRegex = """(?<=Register .:\ )-?\d+""".r
    val programRegex = """(?<=Program: ).*""".r
    val Seq(a, b, c) = registerRegex.findAllIn(ctx.input).map(_.toLong).toSeq
    val program = programRegex.findAllIn(ctx.input).flatMap(_.split(',')).map(_.toInt).toSeq
    (Registers(a, b, c), program)

  case class Registers(var a: Long, var b: Long, var c: Long):
    var ip: Int = 0
    val output: mut.Queue[Int] = mut.Queue.empty[Int]

  trait Operand(val opCode: Int):
    def exec(p: Int, r: Registers): Unit
    def combo(p: Int, r: Registers): Long = p match
      case p if p >= 0 && p <= 3 => p
      case 4                     => r.a
      case 5                     => r.b
      case 6                     => r.c
      case _                     => throw Exception("Invalid combo operand")

  case object Adv extends Operand(0):
    def exec(p: Int, r: Registers) = r.a = (r.a / Math.pow(2, combo(p, r))).toLong

  case object Bxl extends Operand(1):
    def exec(p: Int, r: Registers) = r.b = r.b ^ p

  case object Bst extends Operand(2):
    def exec(p: Int, r: Registers) = r.b = combo(p, r) % 8

  case object Jnz extends Operand(3):
    def exec(p: Int, r: Registers) = if r.a != 0 then r.ip = p - 2

  case object Bxc extends Operand(4):
    def exec(p: Int, r: Registers) = r.b = r.b ^ r.c

  case object Out extends Operand(5):
    def exec(p: Int, r: Registers) = r.output.enqueue((combo(p, r) % 8).toInt)

  case object Bdv extends Operand(6):
    def exec(p: Int, r: Registers) = r.b = (r.a / Math.pow(2, combo(p, r))).toLong

  case object Cdv extends Operand(7):
    def exec(p: Int, r: Registers) = r.c = (r.a / Math.pow(2, combo(p, r))).toLong
