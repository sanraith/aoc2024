package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/6 */
class Day06Test extends SolutionTestSpec:

  describe("Day06 for example input") {
    given day: Solution = Day06()

    it("solves part 1"):
      assertPart(day.part1, expected = 41, input = exampleInput)

    it("solves part 2"):
      assertPart(day.part2, expected = 6, input = exampleInput)
  }

  describe("Day06 for puzzle input") {
    given day: Solution = Day06()

    it("solves part 1")(assertPart(day.part1, 4696))
    it("solves part 2")(assertPart(day.part2, 1443))
  }

  val exampleInput = """
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#..."""
