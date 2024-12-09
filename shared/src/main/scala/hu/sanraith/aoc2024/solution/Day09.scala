package hu.sanraith.aoc2024.solution

import scala.collection.mutable.ArrayBuffer
import scala.collection.SortedSet
import hu.sanraith.aoc2024.util._

/** Solution for https://adventofcode.com/2024/day/9 */
class Day09 extends Solution:
  override val title: String = "Disk Fragmenter"

  override def part1(ctx: Context): Long =
    val buffer = ArrayBuffer.from(parseInput(ctx)).flatMap {
      case Space(_, size)    => ArrayBuffer.fill(size)(-1)
      case File(id, _, size) => ArrayBuffer.fill(size)(id)
    }

    var freeIdx = buffer.indexOf(-1)
    var fileIdx = buffer.lastIndexWhere(_ >= 0)
    while (freeIdx < buffer.length && fileIdx >= 0 && freeIdx < fileIdx)
      buffer(freeIdx) = buffer(fileIdx)
      buffer(fileIdx) = -1
      freeIdx = buffer.indexOf(-1, freeIdx + 1)
      while (fileIdx >= 0 && buffer(fileIdx) == -1)
        fileIdx -= 1

    buffer.takeWhile(_ >= 0).zipWithIndex.foldLeft(0L) { case (acc, (x, idx)) => acc + idx * x }

  override def part2(ctx: Context): Long =
    var segments = parseInput(ctx)
    val files = segments.collect { case f: File => f }.toSeq.reverse
    files.iterator.zipWithIndex.foreach { case (file, idx) =>
      ctx.progress(idx.toDouble / files.size)
      segments.collectFirst { case s: Space if s.size >= file.size && s.pos < file.pos => s } match
        case Some(space) =>
          segments -= space
          segments -= file
          segments += File(file.id, space.pos, file.size)
          segments += Space(file.pos, file.size)
          val updatedSpace = Space(space.pos + file.size, space.size - file.size)
          if updatedSpace.size > 0 then segments += updatedSpace
        case None => ()
    }
    checkSum(segments)

  def checkSum(segments: SortedSet[Segment]): Long = segments.toSeq
    .flatMap:
      case File(id, pos, size) => Seq.fill(size)(id)
      case Space(pos, size)    => Seq.fill(size)(-1)
    .zipWithIndex
    .filter { case (id, _) => id >= 0 }
    .foldLeft(0L) { case (acc, (id, idx)) => acc + idx * id }

  def parseInput(ctx: Context): SortedSet[Segment] =
    ctx.input.trim
      .map(_.asDigit)
      .zipWithIndex
      .filter { case (segSize, _) => segSize > 0 }
      .foldLeft((0, SortedSet.empty[Segment])) { case ((segStart, segments), (segSize, segIdx)) =>
        val segment =
          if segIdx % 2 == 0 then File(segIdx / 2, segStart, segSize) else Space(segStart, segSize)
        (segStart + segSize, segments + segment)
      } match { case (_, segments) => segments }

  case class Space(pos: Int, size: Int) extends Segment
  case class File(id: Int, pos: Int, size: Int) extends Segment
  trait Segment extends Ordered[Segment]:
    val pos: Int
    val size: Int
    override def compare(other: Segment): Int = pos.compare(other.pos)
