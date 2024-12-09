package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/9 */
class Day09Test extends SolutionTestSpec:

  describe("Day09 for example input") {
    given day: Solution = Day09()

    it("solves part 1"):
      assertPart(day.part1, expected = 1928, input = """2333133121414131402""")

    it("solves part 2"):
      assertPart(day.part2, expected = 2858, input = """2333133121414131402""")
  }

  describe("Day09 for puzzle input") {
    given day: Solution = Day09()

    it("solves part 1")(assertPart(day.part1, 6399153661894L))
    it("solves part 2")(assertPart(day.part2, 6421724645083L))
  }
