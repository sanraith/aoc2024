package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/25 */
class Day25Test extends SolutionTestSpec:

  describe("Day25 for example input") {
    given day: Solution = Day25()

    it("solves part 1"):
      assertPart(day.part1, expected = 3, input = testInput)
  }

  describe("Day25 for puzzle input") {
    given day: Solution = Day25()

    it("solves part 1")(assertPart(day.part1, 3287))
    it("solves part 2")(assertPart(day.part2, "*"))
  }

  val testInput = """
#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####"""
