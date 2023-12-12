package starter

data class Location(val value: Char, val x: Int, val y: Int)

class App {
    fun exec() {
        val input =
            this::class.java.getResource("/sample11.txt")!!.readText().trim()
        val lines = input.split("\n")

        val locations = lines.mapIndexed { y, line ->
            line.mapIndexed { x, ch ->
                Location(ch, x, y)
            }
        }

        val emptyRows = locations.filter { row ->
            row.all { it.value == '.' }
        }.map { row: List<Location> ->
            row.first().y
        }
        emptyRows.forEach { println(it) }

//        val galaxies = locations.flatten().filter { it.value == '#' }
//        galaxies.forEach { println(it) }
    }
}

fun main() {
    App().exec()
}
