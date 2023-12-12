package starter

class App {
  fun exec() {
//    val line1 = "???.### 1,1,3"
    val line1 = ".??..??...?##. 1,1,3"
    var row = line1.split(" ").first()
    println("row: $row")
    val cont = line1.split(" ").last().split(",").map { it.toInt() }
    println("cont: $cont")

    var c = cont[0]
    for (i in 0..<c) {
      row = row.replaceFirst('?', '#')
    }
    println(row)
    if (row[c + 1] == '?') {
      row = row.replaceFirst('?', '.')
    }
    println(row)
    c = cont[1]
    for (i in 0..<c) {
      row = row.replaceFirst('?', '#')
    }
    println(row)
    if (row[c + 1] == '?') {
      row = row.replaceFirst('?', '.')
    }
    println(row)
  }
}

fun main() {
  App().exec()
}
