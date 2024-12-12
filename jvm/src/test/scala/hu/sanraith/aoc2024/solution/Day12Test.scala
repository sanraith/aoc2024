package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/12 */
class Day12Test extends SolutionTestSpec:

  describe("Day12 for example input") {
    given day: Solution = Day12()

    it("solves part 1"):
      assertPart(day.part1, expected = 1930, input = testInput)

    it("solves part 2"):
      assertPart(day.part2, expected = 1206, input = testInput)

    it("solves part 2 with simple example"):
      assertPart(
        day.part2,
        expected = 80,
        input = """
AAAA
BBCD
BBCC
EEEC"""
      )
  }

  describe("Day12 for puzzle input") {
    given day: Solution = Day12()

    it("solves part 1")(assertPart(day.part1, 1477924))
    it("solves part 2")(assertPart(day.part2, 841934))
  }

  val testInput = """
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE"""
