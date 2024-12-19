package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/19 */
class Day19Test extends SolutionTestSpec:

  describe("Day19 for example input") {
    given day: Solution = Day19()

    it("solves part 1"):
      assertPart(day.part1, expected = 6, input = testInput)

    it("solves part 2"):
      assertPart(day.part2, expected = 16, input = testInput)
  }

  describe("Day19 for puzzle input") {
    given day: Solution = Day19()

    it("solves part 1")(assertPart(day.part1, 251))
    it("solves part 2")(assertPart(day.part2, 616957151871345L))
  }

  val testInput = """
r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb"""
