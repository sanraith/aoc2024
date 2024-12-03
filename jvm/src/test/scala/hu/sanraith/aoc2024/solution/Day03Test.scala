package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/3 */
class Day03Test extends SolutionTestSpec:

  describe("Day03 for example input") {
    given day: Solution = Day03()

    it("solves part 1"):
      assertPart(
        day.part1,
        expected = 161,
        input = """xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"""
      )

    it("solves part 2"):
      assertPart(
        day.part2,
        expected = 48,
        input = """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""
      )
  }

  describe("Day03 for puzzle input") {
    given day: Solution = Day03()

    it("solves part 1")(assertPart(day.part1, 165225049))
    it("solves part 2")(assertPart(day.part2, 108830766))
  }
