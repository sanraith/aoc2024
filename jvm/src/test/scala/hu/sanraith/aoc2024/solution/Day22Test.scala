package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/22 */
class Day22Test extends SolutionTestSpec:

  describe("Day22 for example input") {
    given day: Solution = Day22()

    it("solves part 1"):
      assertPart(
        day.part1,
        expected = 37327623,
        input = """
1
10
100
2024"""
      )

    it("solves part 2"):
      assertPart(
        day.part2,
        expected = 23,
        input = """
1
2
3
2024"""
      )
  }

  describe("Day22 for puzzle input") {
    given day: Solution = Day22()

    it("solves part 1")(assertPart(day.part1, 13764677935L))
    it("solves part 2")(assertPart(day.part2, 1619))
  }
