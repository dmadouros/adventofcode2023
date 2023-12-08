package part2

data class HandStrength(val regex: Regex, val label: String, val strength: Int) :
    Comparable<HandStrength> {
  override fun compareTo(other: HandStrength): Int {
    return this.strength.compareTo(other.strength)
  }

  override fun toString(): String {
    return "[$label = $strength]"
  }
}

data class Hand(val value: String, val handStrength: HandStrength, val bid: Int) :
    Comparable<Hand> {
  companion object {
    private val CARD_VALUES =
        mapOf(
            'A' to 14,
            'K' to 13,
            'Q' to 12,
            'J' to 1,
            'T' to 10,
            '9' to 9,
            '8' to 8,
            '7' to 7,
            '6' to 6,
            '5' to 5,
            '4' to 4,
            '3' to 3,
            '2' to 2,
        )
  }

  private fun getCardValues(): List<Int> {
    return value.toCharArray().map { CARD_VALUES[it]!! }
  }

  override fun compareTo(other: Hand): Int {
    return if (this.handStrength.compareTo(other.handStrength) == 0) {
      this.getCardValues()
          .zip(other.getCardValues())
          .find { (left, right) -> left.compareTo(right) != 0 }
          ?.let { (left, right) -> left.compareTo(right) } ?: 0
    } else {
      this.handStrength.compareTo(other.handStrength)
    }
  }
}

class App {
  private val handStrengths =
      listOf(
          HandStrength(regex = """^(.)\1{4}""".toRegex(), label = "five of a kind", strength = 7),
          HandStrength(regex = """^(.)\1{3}.""".toRegex(), label = "four of a kind", strength = 6),
          HandStrength(regex = """^(.)\1{2}(.)\2""".toRegex(), label = "full house", strength = 5),
          HandStrength(
              regex = """^(.)\1{2}..""".toRegex(), label = "three of a kind", strength = 4),
          HandStrength(regex = """^(.)\1(.)\2.""".toRegex(), label = "two pair", strength = 3),
          HandStrength(regex = """^(.)\1...""".toRegex(), label = "one pair", strength = 2),
          HandStrength(regex = """^.....""".toRegex(), label = "high card", strength = 1),
      )

  fun exec() {
    val input = this::class.java.getResource("/input.txt")!!.readText()
    val lines = input.split("\n")
    val hands = lines.map { line -> line.split("""\s+""".toRegex()) }.map { it.first() }
    val bids = lines.map { line -> line.split("""\s+""".toRegex()) }.map { it.last().toInt() }
    val part1 =
        hands
            .map { it.toCharArray() }
            .map { it.groupBy { ch -> ch }.entries.sortedBy { it.value.count() }.reversed() }
            .map { hand -> hand.flatMap { cards -> cards.value } }
            .map { it.filter { it != 'J' } }
            .map {
              val string = it.joinToString("")
              val first = if (string.isEmpty()) 'J' else string[0]
              string.padStart(5, first)
            }
            .map { hand -> handStrengths.find { it.regex.containsMatchIn(hand) }!! }
            .zip(hands)
            .zip(bids)
            .map { Hand(value = it.first.second, handStrength = it.first.first, bid = it.second) }
            .sorted()
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()

    println(part1)
  }
}

fun main() {
  App().exec()
}
