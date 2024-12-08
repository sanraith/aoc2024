package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/8 */
class Day08Test extends SolutionTestSpec:

  describe("Day08 for example input") {
    given day: Solution = Day08()

    it("solves part 1 for simple example"):
      assertPart(
        day.part1,
        expected = 2,
        input = """
..........
..........
..........
....a.....
..........
.....a....
..........
..........
..........
.........."""
      )

    it("solves part 2 for simple example"):
      assertPart(
        day.part2,
        expected = 9,
        input = """
T.........
...T......
.T........
..........
..........
..........
..........
..........
..........
.........."""
      )

    it("solves part 1"):
      assertPart(day.part1, expected = 14, input = exampleInput)

    it("solves part 2"):
      assertPart(day.part2, expected = 34, input = exampleInput)
  }

  describe("Day08 for puzzle input") {
    given day: Solution = Day08()

    it("solves part 1")(assertPart(day.part1, 394))
    it("solves part 2")(assertPart(day.part2, 1277))
  }

  val exampleInput = """
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............"""
