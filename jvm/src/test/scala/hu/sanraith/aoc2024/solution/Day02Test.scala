package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/2 */
class Day02Test extends SolutionTestSpec:

  describe("Day02 for example input") {
    given day: Solution = Day02()

    it("solves part 1"):
      assertPart(
        day.part1,
        expected = 2,
        input = exampleInput
      )

    it("solves part 2"):
      assertPart(
        day.part2,
        expected = 4,
        input = exampleInput
      )

    it("solves part 2 when first number is wrong"):
      assertPart(
        day.part2,
        expected = 1,
        input = "999 8 7 6 5 4"
      )
  }

  describe("Day02 for puzzle input") {
    given day: Solution = Day02()

    it("solves part 1")(assertPart(day.part1, 524))
    it("solves part 2")(assertPart(day.part2, 569))
  }

  val exampleInput = """
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9"""
