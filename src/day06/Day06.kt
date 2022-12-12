package day06

import readInput

private const val DAY = "06"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {
    fun findUniqueSubstringIndex(code: String, length: Int) = code
        .windowed(length)
        .mapIndexed { index, substring ->
            // reconstruct index from the original string before splitting into slices
            index + length to substring.containsUniqueChars()
        }.find {
            it.second
        }?.first ?: throw IllegalStateException("No index found to satisfy condition")

    fun part1(input: List<String>) = findUniqueSubstringIndex(input.first(), length = 4)

    fun part2(input: List<String>) = findUniqueSubstringIndex(input.first(), length = 14)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput(DAY_INPUT)
    println(part1(input)) // 1848
    println(part2(input)) // 2308
}

private fun String.containsUniqueChars(): Boolean {
    val itemsSet = HashSet<Char>()
    return this.all { item ->
        !itemsSet.contains(item).also {
            itemsSet.add(item)
        }
    }
}