package day01

import readFile

private const val DAY = "01"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {
    fun sumTopCalories(input: String, limit: Int = 1) = input
        .split("\n\n")
        .asSequence()
        .map { backpack ->
            val calories = backpack.split("\n")
            calories.map { it.toInt() }
        }
        .map { it.sum() }
        .sortedDescending()
        .take(limit)
        .sum()

    fun part1(input: String) = sumTopCalories(input)

    fun part2(input: String) = sumTopCalories(input, 3)

    // test if implementation meets criteria from the description, like:
    val testInput = readFile(DAY_TEST)
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readFile(DAY_INPUT)
    println(part1(input)) // 73211
    println(part2(input)) // 213958
}
