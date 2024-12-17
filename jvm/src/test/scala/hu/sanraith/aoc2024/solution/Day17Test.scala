package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/17 */
class Day17Test extends SolutionTestSpec:

  describe("Day17 for example input") {
    given day: Solution = Day17()

    it("solves part 1"):
      assertPart(
        day.part1,
        expected = "4,6,3,5,6,3,5,2,1,0",
        input = """
Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0"""
      )

    it("solves part 2"):
      assertPart(
        day.part2,
        expected = 117440,
        input = """
Register A: 2024
Register B: 0
Register C: 0

Program: 0,3,5,4,3,0"""
      )
  }

  describe("Day17 for puzzle input") {
    given day: Solution = Day17()

    it("solves part 1")(assertPart(day.part1, "3,5,0,1,5,1,5,1,0"))
    it("solves part 2")(_assertPart(day.part2, "__PART_2_EXPECTED__"))
  }
