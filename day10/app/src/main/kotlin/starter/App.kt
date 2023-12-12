package starter

data class Tile(val value: Char, val x: Int, val y: Int)

data class Direction(val value: Char, val x: Int, val y: Int)

fun findNextTile(
    directions: Map<Char, Direction>,
    field: List<List<Tile>>,
    currentDir: Direction,
    currentTile: Tile
): Pair<Direction, Tile> {
  var nextDir =
      if (currentTile.value == '-') {
        currentDir
      } else if (currentTile.value == '|') {
        currentDir
      } else if (currentTile.value == 'F') {
        if (currentDir.value == 'W') {
          directions['S']
        } else {
          directions['E']
        }
      } else if (currentTile.value == '7') {
        if (currentDir.value == 'E') {
          directions['S']
        } else {
          directions['W']
        }
      } else if (currentTile.value == 'L') {
        if (currentDir.value == 'W') {
          directions['N']
        } else {
          directions['E']
        }
      } else {
        if (currentDir!!.value == 'E') {
          directions['N']
        } else {
          directions['W']
        }
      }
  val dx = nextDir!!.x + currentTile.x
  val dy = nextDir!!.y + currentTile.y
  return nextDir!! to field[dy][dx]
}

val directions =
    mapOf(
        'N' to Direction('N', 0, -1),
        'E' to Direction('E', 1, 0),
        'S' to Direction('S', 0, 1),
        'W' to Direction('W', -1, 0),
    )

/*
 * north = F, |
 * east = -, J
 * south = |, J
 * west = -, F
 */

class App {
  fun exec() {
    val input = this::class.java.getResource("/input.txt")!!.readText().trim()
    val lines = input.split("\n")
    val field =
        lines.mapIndexed { y, line -> line.mapIndexed { x, ch -> Tile(value = ch, x = x, y = y) } }
    var start: Tile? = null
    for (line in field) {
      for (tile in line) {
        if (tile.value == 'S') {
          start = tile
          break
        }
      }
    }
    println("start: $start")

    // find real start
    var current: Pair<Direction, Tile>? = null
    for (direction in directions.values) {
      val dx = direction.x + start!!.x
      val dy = direction.y + start!!.y
      val nextDir = field[dy][dx]
      if (direction.value == 'N' && nextDir.value in listOf('|', '7', 'F')) {
        current = direction to nextDir
        break
      } else if (direction.value == 'S' && nextDir.value in listOf('|', 'J', 'L')) {
        current = direction to nextDir
        break
      } else if (direction.value == 'W' && nextDir.value in listOf('-', 'F', 'L')) {
        current = direction to nextDir
        break
      } else if (direction.value == 'E' && nextDir.value in listOf('-', 'J', '7')) {
        current = direction to nextDir
        break
      }
    }
    println(current)

    var count = 0
    while (current!!.second.value != 'S') {
      current = findNextTile(directions, field, current.first, current.second)
      count++
    }
    if (count % 2 == 1) {
      println(count.div(2) + 1)
    } else {
      println(count.div(2))
    }
  }
}

fun main() {
  App().exec()
}
