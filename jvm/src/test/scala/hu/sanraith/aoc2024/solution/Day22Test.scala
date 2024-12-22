package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/22 */
class Day22Test extends SolutionTestSpec:

  describe("Day22 for example input") {
    given day: Solution = Day22()

    it("solves part 1"):
      assertPart(day.part1, expected = 37327623, input = testInput)

    it("solves part 2"):
      assertPart(day.part2, expected = 23, input = testInput)
  }

  describe("Day22 for puzzle input") {
    given day: Solution = Day22()

    it("solves part 1")(assertPart(day.part1, 13764677935L))
    it("solves part 2")(_assertPart(day.part2, "__PART_2_EXPECTED__"))
  }

  val testInput = """
1
10
100
2024"""
