package starter

import kotlin.math.max
import kotlin.math.min

class Function {
  private val tuples: List<Triple<Long, Long, Long>>

  constructor(value: String) {
    val entries = value.split("\n").drop(1)
    tuples =
        entries
            .map { entry -> entry.trim().split(" ").map(String::toLong) }
            .map { (a, b, c) -> Triple(a, b, c) }
  }

  fun applyOne(seed: Long): Long {
    return tuples
        .find { (_, src, sz) -> src <= seed && seed < src + sz }
        ?.let { (dst, src, _) -> seed + dst - src } ?: seed
  }

  fun applyRange(ranges: List<Pair<Long, Long>>): List<Pair<Long, Long>> {
    val (alteredRanges, leftoverRanges) =
        tuples.fold(Pair(emptyList<Pair<Long, Long>>(), ranges)) { memo, (dst, src, sz) ->
          val srcEnd = src + sz
          memo.second.fold(Pair(memo.first, emptyList())) { (alteredRanges, newRanges), range ->
            val (start, end) = range

            val before = Pair(start, min(end, src))
            val intersection = Pair(max(start, src), min(srcEnd, end))
            val after = Pair(max(srcEnd, start), end)
            val left = alteredRanges.toMutableList()
            val right = newRanges.toMutableList()

            if (before.second > before.first) {
              right += listOf(before)
            }
            if (intersection.second > intersection.first) {
              left += listOf(Pair(intersection.first - src + dst, intersection.second - src + dst))
            }
            if (after.second > after.first) {
              right += listOf(after)
            }

            Pair(left, right)
          }
        }
    return alteredRanges + leftoverRanges
  }
}

class Part2 {
  fun exec() {
    val input = this::class.java.getResource("/input.txt")!!.readText().trim()
    val parts = input.split("\n\n")
    val seeds = parts[0].split(":").last().trim().split(" ").map(String::toLong)
    val others = parts.drop(1)
    val part1 =
        seeds.minOfOrNull { seed ->
          others.map { Function(it) }.fold(seed) { memo, f -> f.applyOne(memo) }
        }
    println(part1)

    val pairs = seeds.chunked(2).map { (start, size) -> Pair(start, start + size) }
    val part2 =
        others
            .map { Function(it) }
            .fold(pairs) { memo, f -> f.applyRange(memo) }
            .minOfOrNull { min(it.first, it.second) }
    println(part2)
  }
}

fun main() {
  Part2().exec()
}
