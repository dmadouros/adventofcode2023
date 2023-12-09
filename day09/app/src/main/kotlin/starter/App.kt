package starter

fun solve(sequences: List<List<Int>>, part: Int): Int {
  return sequences
      .map { sequence ->
        var currentSeq = sequence
        var nextSeq = mutableListOf<Int>()
        var history = mutableListOf(currentSeq)

        while (nextSeq.isEmpty() || nextSeq.toSet() != setOf(0)) {
          var i = 0
          var j = 1
          nextSeq = mutableListOf()
          while (j < currentSeq.size) {
            val first = currentSeq[i]
            val second = currentSeq[j]
            val diff = second - first
            nextSeq.add(diff)
            i++
            j++
          }
          history.add(nextSeq)
          currentSeq = nextSeq
        }

        history.reversed().fold(0) { memo, sequence ->
          if (part == 1) sequence.last() + memo else sequence.first() - memo
        }
      }
      .sum()
}

class App {
  fun exec() {
    val input = this::class.java.getResource("/input.txt")!!.readText().trim()
    val lines = input.split("\n")
    val sequences = lines.map { it.split(" ") }.map { sequence -> sequence.map { it.toInt() } }
    println(solve(sequences, part = 1))
    println(solve(sequences, part = 2))
  }
}

fun main() {
  App().exec()
}
