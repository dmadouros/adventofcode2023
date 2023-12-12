package starter

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Location(val value: Char, val x: Long, val y: Long, val number: Long = 0)

fun minDistance(
    emptyColumnIndices: List<Long>,
    emptyRowIndices: List<Long>,
    loc1: Location,
    loc2: Location
): Long {
  val offset = 1_000_000 - 1
  val maxY = max(loc1.y, loc2.y)
  val minY = min(loc1.y, loc2.y)
  val riseOffset = emptyRowIndices.toSet().intersect(minY..maxY).count() * offset
  println("riseOffset: $riseOffset")

  val maxX = max(loc1.x, loc2.x)
  val minX = min(loc1.x, loc2.x)
  val runOffset = emptyColumnIndices.toSet().intersect(minX..maxX).count() * offset
  println("runOffset: $runOffset")

  val rise = abs(loc1.y - loc2.y) + riseOffset
  val run = abs(loc1.x - loc2.x) + runOffset
  return rise + run
}

class App {
  fun exec() {
    val input = this::class.java.getResource("/input.txt")!!.readText().trim()
    val lines = input.split("\n")

    val emptyRowIndices =
        lines.foldIndexed(emptyList<Long>()) { index, memo, line ->
          if (line.toSet() == setOf('.')) memo + listOf(index.toLong()) else memo
        }
    println(emptyRowIndices)

    val columnIndices = lines[0].indices
    val emptyColumnIndices =
        columnIndices.fold(emptyList<Long>()) { memo, colIndex ->
          if (lines.all { line -> line[colIndex] == '.' }) memo + listOf(colIndex.toLong()) else memo
        }
    println(emptyColumnIndices)

    val locations =
        lines.mapIndexed { y, line ->
          line.mapIndexed { x, ch -> Location(value = ch, x = x.toLong(), y = y.toLong()) }
        }

    var galaxies = locations.flatten().filter { it.value == '#' }
    galaxies = galaxies.mapIndexed { index, it -> it.copy(number = index.toLong() + 1) }
    val galaxy3 = galaxies[2]
    val galaxy4 = galaxies[3]
    println(minDistance(emptyColumnIndices, emptyRowIndices, galaxy3, galaxy4))
//     permutations = (9 * 8) / 2 == 36
//     combinations = (9 * 8) = 72
        val permutations = mutableListOf<Pair<Location, Location>>()
        for (i in 0 ..< galaxies.count()) {
          for (j in (i + 1) ..< galaxies.count()) {
            permutations.add(galaxies[i] to galaxies[j])
          }
        }

        val result = permutations.sumOf { (loc1, loc2) -> minDistance(emptyColumnIndices,
     emptyRowIndices, loc1, loc2) }
        println(result)
  }
}

fun main() {
  App().exec()
}
