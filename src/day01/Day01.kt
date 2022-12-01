package day01

import readInput

private const val DAY = "01"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {
    fun getCaloriesList(input: List<String>): List<Int> {
        var collector = 0
        val calories = mutableListOf<Int>()
        input.forEach { calorieString ->
            if (calorieString == "") {
                calories.add(collector)
                collector = 0
            } else {
                collector += calorieString.toInt()
            }
        }
        calories.add(collector)
        return calories.sortedDescending()
    }

    fun part1(input: List<String>) = getCaloriesList(input).first()

    fun part2(input: List<String>) = getCaloriesList(input).take(3).sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput(DAY_INPUT)
    println(part1(input))
    println(part2(input))
}
