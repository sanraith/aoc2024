package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/18 */
class Day18Test extends SolutionTestSpec:

  describe("Day18 for example input") {
    given day: Day18 = Day18()

    it("solves part 1"):
      day.grid = Day18.Grid(7, 7)
      day.part1ByteCount = 12
      assertPart(day.part1, expected = 22, input = testInput)

    it("solves part 2"):
      day.grid = Day18.Grid(7, 7)
      day.part1ByteCount = 12
      assertPart(day.part2, expected = "6,1", input = testInput)
  }

  describe("Day18 for puzzle input") {
    given day: Solution = Day18()

    it("solves part 1")(assertPart(day.part1, 246))
    it("solves part 2")(assertPart(day.part2, "22,50"))
  }

  val testInput = """
5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0"""
