/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package starter

class Part1 {
  fun exec() {
    val lines = this::class.java.getResourceAsStream("/input.txt").bufferedReader().readLines()

    var partNumbers = emptyList<String>()
    for ((y, line) in lines.withIndex()) {
      var symbolFound = false
      var currentPartNumber = ""
      for ((x, value) in line.withIndex()) {
        if (value.isDigit()) {
          currentPartNumber += value

          val neighbors =
              listOf(
                  Pair(0, -1),
                  Pair(1, -1),
                  Pair(1, 0),
                  Pair(1, 1),
                  Pair(0, 1),
                  Pair(-1, 1),
                  Pair(-1, 0),
                  Pair(-1, -1),
              )
          symbolFound =
              symbolFound ||
                  neighbors.any { neighbor ->
                    val dy = neighbor.second + y
                    val dx = neighbor.first + x

                    if (dy >= 0 && dy < lines.size && dx >= 0 && dx < line.length) {
                      !lines[dy][dx].isDigit() && lines[dy][dx] != '.'
                    } else {
                      false
                    }
                  }
        } else {
          if (currentPartNumber.isNotEmpty()) {
            if (symbolFound) {
              partNumbers += listOf(currentPartNumber)
            }
            currentPartNumber = ""
            symbolFound = false
          }
        }
      }
      if (currentPartNumber.isNotEmpty()) {
        if (symbolFound) {
          partNumbers += listOf(currentPartNumber)
        }
      }
    }

    println(partNumbers.map { it.toInt() }.sum())
  }
}

data class PartNumber(val id: String, val left: Pair<Int, Int>, val right: Pair<Int, Int>)

data class Widget(val type: Char, val location: Pair<Int, Int>)

class Part2 {
  fun exec() {
    val lines = this::class.java.getResourceAsStream("/input.txt").bufferedReader().readLines()

    var partNumbers = emptyList<PartNumber>()
    var widgets = emptyList<Widget>()
    for ((y, line) in lines.withIndex()) {
      var currentId = ""
      var left: Pair<Int, Int>? = null
      for ((x, value) in line.withIndex()) {
        if (value.isDigit()) {
          currentId += value
          if (left == null) {
            left = Pair(x, y)
          }
        } else {
          if (value != '.') {
            widgets += listOf(Widget(type = value, location = Pair(x, y)))
          }
          if (currentId.isNotEmpty()) {
            val partNumber = PartNumber(id = currentId, left = left!!, right = Pair(x - 1, y))
            partNumbers += listOf(partNumber)
          }
          currentId = ""
          left = null
        }
      }
      if (currentId.isNotEmpty()) {
        val partNumber = PartNumber(id = currentId, left = left!!, right = Pair(line.length - 1, y))
        partNumbers += listOf(partNumber)
      }
    }

    val gears =
        widgets
            .filter { it.type == '*' }
            .map { widget ->
              val matches =
                  partNumbers.fold(emptyList<PartNumber>()) { memo, partNumber ->
                    val neighbors =
                        listOf(
                            Pair(0, -1), // north
                            Pair(1, -1), // northeast
                            Pair(1, 0), // east
                            Pair(1, 1), // southeast
                            Pair(0, 1), // south
                            Pair(-1, 1), // southwest
                            Pair(-1, 0), // west
                            Pair(-1, -1), // northwest
                        )
                    val match =
                        neighbors.any { neighbor ->
                          val dx = widget.location.first + neighbor.first
                          val dy = widget.location.second + neighbor.second

                          if (dy >= 0 && dy < lines.size && dx >= 0 && dx < lines[dy].length) {
                            dx <= partNumber.right.first &&
                                dx >= partNumber.left.first &&
                                dy <= partNumber.right.second &&
                                dy >= partNumber.left.second
                          } else {
                            false
                          }
                        }

                    if (match) {
                      memo + listOf(partNumber)
                    } else {
                      memo
                    }
                  }
              Pair(widget, matches)
            }
            .filter { it.second.size == 2 }

    val result = gears
        .map { it.second }
        .map { it -> it.map { it.id }.map { it.toInt() } }
        .map { (first, second) -> first * second }
        .sum()
    println(result)
  }
}

fun main() {
  Part2().exec()
}
