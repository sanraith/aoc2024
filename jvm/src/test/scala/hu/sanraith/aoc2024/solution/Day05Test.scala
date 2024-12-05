package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/5 */
class Day05Test extends SolutionTestSpec:

  describe("Day05 for example input") {
    given day: Solution = Day05()

    it("solves part 1"):
      assertPart(day.part1, expected = 143, input = exampleInput)

    it("solves part 2"):
      assertPart(day.part2, expected = 123, input = exampleInput)
  }

  describe("Day05 for puzzle input") {
    given day: Solution = Day05()

    it("solves part 1")(assertPart(day.part1, 5964))
    it("solves part 2")(assertPart(day.part2, 4719))
  }

  val exampleInput = """47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47"""
