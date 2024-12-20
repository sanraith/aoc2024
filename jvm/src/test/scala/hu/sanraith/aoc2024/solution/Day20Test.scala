package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/20 */
class Day20Test extends SolutionTestSpec:

  describe("Day20 for example input") {
    given day: Day20 = Day20()

    it("solves part 1"):
      day.saveThreshold = 12
      assertPart(day.part1, expected = 8, input = testInput)

    it("solves part 2"):
      day.saveThreshold = 76
      assertPart(day.part2, expected = 3, input = testInput)
  }

  describe("Day20 for puzzle input") {
    given day: Solution = Day20()

    it("solves part 1")(assertPart(day.part1, 1346))
    it("solves part 2")(assertPart(day.part2, 985482))
  }

  val testInput = """
###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############"""
