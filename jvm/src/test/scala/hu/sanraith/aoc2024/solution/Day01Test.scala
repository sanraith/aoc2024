package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/1 */
class Day01Test extends SolutionTestSpec:

  describe("Day01 for example input") {
    given day: Solution = Day01()

    it("solves part 1"):
      assertPart(
        day.part1,
        expected = 11,
        input = exampleInput
      )

    it("solves part 2"):
      assertPart(
        day.part2,
        expected = 31,
        input = exampleInput
      )
  }

  describe("Day01 for puzzle input") {
    given day: Solution = Day01()

    it("solves part 1")(assertPart(day.part1, 2580760))
    it("solves part 2")(assertPart(day.part2, 25358365))
  }

  val exampleInput = """
3   4
4   3
2   5
1   3
3   9
3   3"""
