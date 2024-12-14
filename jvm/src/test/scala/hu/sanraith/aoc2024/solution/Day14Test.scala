package hu.sanraith.aoc2024.solution

import hu.sanraith.aoc2024.solution.Day14._

/** Tests for https://adventofcode.com/2024/day/14 */
class Day14Test extends SolutionTestSpec:

  describe("Day14 for example input") {
    given day: Solution = Day14()

    it("solves part 1"):
      day.asInstanceOf[Day14].gridSize = Point(11, 7)
      assertPart(
        day.part1,
        expected = 12,
        input = """
p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3"""
      )

    it("solves part 2"):
      _assertPart(
        day.part2,
        expected = "__PART_2_TEST_EXPECTED__",
        input = """__PART_2_TEST_INPUT__"""
      )
  }

  describe("Day14 for puzzle input") {
    given day: Solution = Day14()

    it("solves part 1")(assertPart(day.part1, 231782040L))
    it("solves part 2")(_assertPart(day.part2, "__PART_2_EXPECTED__"))
  }
