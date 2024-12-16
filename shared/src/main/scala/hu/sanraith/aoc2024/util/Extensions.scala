package hu.sanraith.aoc2024.util

import scala.collection.Iterable

implicit class IntModWrap(private val dividend: Int) extends AnyVal:
  /** Mod operator that wraps around from negative to positive. */
  def %%(divisor: Int): Int = ((dividend % divisor) + divisor) % divisor

implicit class LongModWrap(private val dividend: Long) extends AnyVal:
  /** Mod operator that wraps around from negative to positive. */
  def %%(divisor: Long): Long = ((dividend % divisor) + divisor) % divisor

implicit class IteratorExtensions[A](val it: Iterator[A]) extends AnyVal:
  def tapEachWithIndex(f: (item: A, index: Int) => Unit): Iterator[A] =
    it.zipWithIndex
      .tapEach((x, i) => f(x, i))
      .map((x, _) => x)

implicit class IterableExtensions[A](val it: Iterable[A]) extends AnyVal:
  def tapEachWithIndex(f: (item: A, index: Int) => Unit): Iterable[A] =
    it.zipWithIndex
      .tapEach((x, i) => f(x, i))
      .map((x, _) => x)
