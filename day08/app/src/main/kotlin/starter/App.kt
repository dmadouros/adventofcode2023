package starter

data class Node(val value: String, val left: String, val right: String)

fun <T : Any> cycle(xs: List<T>): Sequence<T> {
  var i = 0
  return generateSequence { xs[i++ % xs.size] }
}

fun gcd(n1: Long, n2: Long): Long {
  var gcd = 1L

  var i = 1L
  while (i <= n1 && i <= n2) {
    if (n1 % i == 0L && n2 % i == 0L) {
      gcd = i
    }
    i++
  }

  return gcd
}

fun lcm(n1: Long, n2: Long): Long {
  return n1 * n2 / gcd(n1, n2)
}

class App {
  fun exec() {
    val regex = """\((\w+),\s(\w+)\)""".toRegex()
    val input = this::class.java.getResource("/input.txt")!!.readText().trim()
    val lines = input.split("\n")
    val directions = lines[0].toCharArray()
    val nodes =
        lines
            .drop(2)
            .map { line -> line.split("=") }
            .map {
              val value = it.first().trim()
              val matchResult =
                  regex.find(it.last())?.destructured?.let { (left, right) ->
                    Node(value, left, right)
                  }
              matchResult!!
            }
            .filter { it.value != it.left || it.value != it.right }
    val network =
        nodes.fold(emptyMap<String, Node>()) { memo, node -> memo + mapOf(node.value to node) }
    //    println(directions)
    //    network.forEach { println(it) }
    val startingNodes = nodes.filter { it.value.endsWith("A") }

    val journeys =
        startingNodes.map { node ->
          var count = 0L
          var nextNode = node
          val visited = mutableListOf(nextNode)
          for (direction in cycle(directions.toList())) {
            if (nextNode.value.endsWith("Z")) {
              break
            } else {
              count += 1L
              nextNode =
                  if (direction == 'L') {
                    network[nextNode.left]!!.also { visited.add(it) }
                  } else {
                    network[nextNode.right]!!.also { visited.add(it) }
                  }
            }
          }
          visited.count().toLong()
          visited
        }

    val counts = journeys.map { (it.count() - 1).toLong() }
    val n1 = counts[0]
    val result = counts.drop(1).fold(n1) { memo, count -> lcm(memo, count) }
    println(result)
  }
}

fun main() {
  App().exec()
}
