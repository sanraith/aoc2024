package hu.sanraith.aoc2024.solution

/** Tests for https://adventofcode.com/2024/day/23 */
class Day23Test extends SolutionTestSpec:

  describe("Day23 for example input") {
    given day: Solution = Day23()

    it("solves part 1"):
      assertPart(day.part1, expected = 7, input = testInput)

    it("solves part 2"):
      assertPart(day.part2, expected = "co,de,ka,ta", input = testInput)
  }

  describe("Day23 for puzzle input") {
    given day: Solution = Day23()

    it("solves part 1")(assertPart(day.part1, 1230))
    it("solves part 2")(assertPart(day.part2, "az,cj,kp,lm,lt,nj,rf,rx,sn,ty,ui,wp,zo"))
  }

  val testInput = """
kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn"""
