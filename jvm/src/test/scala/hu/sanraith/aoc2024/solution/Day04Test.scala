package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/4 */
class Day04Test extends SolutionTestSpec:

  describe("Day04 for example input") {
    given day: Solution = Day04()

    it("solves part 1"):
      assertPart(day.part1, expected = 18, input = exampleInput)

    it("solves part 2"):
      assertPart(day.part2, expected = 9, input = exampleInput)
  }

  describe("Day04 for puzzle input") {
    given day: Solution = Day04()

    it("solves part 1")(assertPart(day.part1, 2468))
    it("solves part 2")(assertPart(day.part2, 1864))
  }

  val exampleInput = """
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX"""
