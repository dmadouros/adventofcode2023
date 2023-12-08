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
    val alteredRanges = mutableListOf<Pair<Long, Long>>()
    var temp = ranges.toMutableList()

    tuples.forEach { (dst, src, sz) ->
      val srcEnd = src + sz
      val newRanges = mutableListOf<Pair<Long, Long>>()
      while (temp.isNotEmpty()) {
        val (start, end) = temp.removeFirst()

        val before = Pair(start, min(end, src))
        val intersection = Pair(max(start, src), min(srcEnd, end))
        val after = Pair(max(srcEnd, start), end)

        if (before.second > before.first) {
          newRanges.add(before)
        }
        if (intersection.second > intersection.first) {
          alteredRanges.add(Pair(intersection.first - src + dst, intersection.second - src + dst))
        }
        if (after.second > after.first) {
          newRanges.add(after)
        }
      }
      temp = newRanges
    }
    return alteredRanges + temp
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
