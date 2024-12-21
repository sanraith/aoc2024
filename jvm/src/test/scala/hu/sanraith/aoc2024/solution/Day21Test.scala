package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/21 */
class Day21Test extends SolutionTestSpec:

  describe("Day21 for example input") {
    given day: Solution = Day21()

    it("solves part 1"):
      assertPart(day.part1, expected = 126384, input = testInput)

    it("solves part 2"):
      assertPart(day.part2, expected = 154115708116294L, input = testInput)
  }

  describe("Day21 for puzzle input") {
    given day: Solution = Day21()

    it("solves part 1")(assertPart(day.part1, 278748))
    it("solves part 2")(assertPart(day.part2, 337744744231414L))
  }

  val testInput = """
029A
980A
179A
456A
379A"""
