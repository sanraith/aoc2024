package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/7 */
class Day07Test extends SolutionTestSpec:

  describe("Day07 for example input") {
    given day: Solution = Day07()

    it("solves part 1"):
      assertPart(day.part1, expected = 3749, input = exampleInput)

    it("solves part 2"):
      assertPart(day.part2, expected = 11387, input = exampleInput)
  }

  describe("Day07 for puzzle input") {
    given day: Solution = Day07()

    it("solves part 1")(assertPart(day.part1, 465126289353L))
    it("solves part 2")(assertPart(day.part2, 70597497486371L))
  }

  val exampleInput = """
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20"""
