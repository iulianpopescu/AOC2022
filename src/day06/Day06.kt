package day06

import readInput

private const val DAY = "06"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {
    fun findUniqueForLength(code: String, length: Int): Int {
        val queue = ArrayDeque<Char>()
        queue.addAll(code.take(length).toList())
        if (queue.containsUniqueItems())
            return length

        for (i in length..code.lastIndex) {
            queue.removeFirst()
            queue.add(code[i])
            if (queue.containsUniqueItems())
                return i + 1
        }
        throw IllegalStateException("No index found to satisfy condition")
    }

    fun part1(input: List<String>) = findUniqueForLength(input.first(), 4)

    fun part2(input: List<String>) = findUniqueForLength(input.first(), 14)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput(DAY_INPUT)
    println(part1(input))
    println(part2(input))
}

private fun ArrayDeque<Char>.containsUniqueItems(): Boolean {
    val itemsSet = HashSet<Char>()
    return this.all { item ->
        !itemsSet.contains(item).also {
            itemsSet.add(item)
        }
    }
}