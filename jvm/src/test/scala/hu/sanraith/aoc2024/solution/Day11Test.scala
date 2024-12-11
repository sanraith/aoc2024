package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/11 */
class Day11Test extends SolutionTestSpec:

  describe("Day11 for example input") {
    given day: Solution = Day11()

    it("solves part 1"):
      assertPart(day.part1, expected = 55312, input = """125 17""")
  }

  describe("Day11 for puzzle input") {
    given day: Solution = Day11()

    it("solves part 1")(assertPart(day.part1, 203609))
    it("solves part 2")(assertPart(day.part2, 240954878211138L))
  }
