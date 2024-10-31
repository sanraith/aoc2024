package hu.sanraith.aoc2024.solution

trait Context:
  val input: String
  def progress(value: Double): Unit = {}

type WholeNumberOrString = Int | Long | String

abstract class Solution:
  val title: String
  def part1(ctx: Context): WholeNumberOrString
  def part2(ctx: Context): WholeNumberOrString
  var println: Any => Unit = msg => Predef.println(msg)

sealed class SolutionInfo(
    val day: Int,
    val createInstance: () => Solution
):
  override def toString(): String = s"Day #$day"

val SolutionMap: Map[Int, SolutionInfo] = SolutionDefinitions.map(s => s.day -> s).toMap
