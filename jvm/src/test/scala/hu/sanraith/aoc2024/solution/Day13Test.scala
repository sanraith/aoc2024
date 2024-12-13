package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/13 */
class Day13Test extends SolutionTestSpec:

  describe("Day13 for example input") {
    given day: Solution = Day13()

    it("solves part 1"):
      assertPart(
        day.part1,
        expected = 480,
        input = """
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279"""
      )

    it("solves part 2"):
      assertPart(
        day.part2,
        expected = 480,
        input = """
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279"""
      )
  }

  describe("Day13 for puzzle input") {
    given day: Solution = Day13()

    it("solves part 1")(_assertPart(day.part1, 34393))
    it("solves part 2")(_assertPart(day.part2, "__PART_2_EXPECTED__"))
  }
