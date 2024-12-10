package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/10 */
class Day10Test extends SolutionTestSpec:

  describe("Day10 for example input") {
    given day: Solution = Day10()

    it("solves part 1"):
      assertPart(day.part1, expected = 36, input = exampleInput)

    it("solves part 1 small example"):
      assertPart(
        day.part1,
        expected = 1,
        input = """
0123
1234
8765
9876"""
      )

    it("solves part 2"):
      assertPart(day.part2, expected = 81, input = exampleInput)

    it("solves part 2 small example"):
      assertPart(
        day.part2,
        expected = 3,
        input = """
.....0.
..4321.
..5..2.
..6543.
..7..4.
..8765.
..9...."""
      )
  }

  describe("Day10 for puzzle input") {
    given day: Solution = Day10()

    it("solves part 1")(assertPart(day.part1, 552))
    it("solves part 2")(assertPart(day.part2, 1225))
  }

  val exampleInput = """
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732"""
