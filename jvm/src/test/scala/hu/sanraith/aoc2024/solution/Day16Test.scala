package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/16 */
class Day16Test extends SolutionTestSpec:

  describe("Day16 for example input") {
    given day: Solution = Day16()

    it("solves part 1 first"):
      assertPart(
        day.part1,
        expected = 7036,
        input = """
###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############"""
      )

    it("solves part 1 second"):
      assertPart(
        day.part1,
        expected = 11048,
        input = """
#################
#...#...#...#..E#
#.#.#.#.#.#.#.#.#
#.#.#.#...#...#.#
#.#.#.#.###.#.#.#
#...#.#.#.....#.#
#.#.#.#.#.#####.#
#.#...#.#.#.....#
#.#.#####.#.###.#
#.#.#.......#...#
#.#.###.#####.###
#.#.#...#.....#.#
#.#.#.#####.###.#
#.#.#.........#.#
#.#.#.#########.#
#S#.............#
#################"""
      )

    it("solves part 2"):
      assertPart(
        day.part2,
        expected = "__PART_2_TEST_EXPECTED__",
        input = """__PART_2_TEST_INPUT__"""
      )
  }

  describe("Day16 for puzzle input") {
    given day: Solution = Day16()

    it("solves part 1")(assertPart(day.part1, 95476))
    it("solves part 2")(_assertPart(day.part2, "__PART_2_EXPECTED__"))
  }
