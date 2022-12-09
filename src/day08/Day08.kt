package day08

import readInput
import kotlin.math.max

private const val DAY = "08"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {
    fun isTreeVisible(i: Int, j: Int, input: List<String>): Boolean {
        if (i == 0 || j == 0 || i == input.lastIndex || j == input[i].lastIndex) return true

        val tree = input[i][j].digitToInt()
        val visibleHLeft = input[i].substring(0 until j).all { it.digitToInt() < tree }
        val visibleHRight = input[i].substring(j + 1..input[i].lastIndex).all { it.digitToInt() < tree }
        val visibleVTop = input.subList(0, i).map { it[j] }.all { it.digitToInt() < tree }
        val visibleVBottom = input.subList(i + 1, input.size).map { it[j] }.all { it.digitToInt() < tree }

        return visibleHLeft || visibleHRight || visibleVTop || visibleVBottom
    }

    var lastVisibleTree = 0
    fun shouldCountTree(viewingTree: Int, currentTree: Int): Boolean {
        return if (currentTree in lastVisibleTree until viewingTree) {
            lastVisibleTree = currentTree
            true
        } else if (currentTree > lastVisibleTree) {
            lastVisibleTree = currentTree
            true
        } else false
    }

    fun countTreeVisibleScore(i: Int, j: Int, input: List<String>): Int {
        val tree = input[i][j].digitToInt()

        var counting = true
        val hLeftCount = input[i]
            .substring(0 until j)
            .reversed()
            .map { it.digitToInt() }
            .takeWhile { currTree ->
                val shouldCount = counting
                if (currTree >= tree) counting = false
                shouldCount
            }
            .size

        lastVisibleTree = 0
        counting = true
        val hRightCount = input[i]
            .substring(j + 1..input[i].lastIndex)
            .map { it.digitToInt() }
            .takeWhile { currTree ->
                val shouldCount = counting
                if (currTree >= tree) counting = false
                shouldCount
            }
            .size

        lastVisibleTree = 0
        counting = true
        val vTopCount = input.subList(0, i).reversed().map { it[j].digitToInt() }
            .takeWhile { currTree ->
                val shouldCount = counting
                if (currTree >= tree) counting = false
                shouldCount
            }
            .size

        lastVisibleTree = 0
        counting = true
        val vBottomCount = input.subList(i + 1, input.size).map { it[j].digitToInt() }
            .takeWhile { currTree ->
                val shouldCount = counting
                if (currTree >= tree) counting = false
                shouldCount
            }
            .size

//        println("$i:$j -> $hLeftCount $hRightCount $vTopCount $vBottomCount")
        return hLeftCount * hRightCount * vTopCount * vBottomCount
    }

    fun part1(input: List<String>): Int {
        var total = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (isTreeVisible(i, j, input)) {
                    total += 1
                }
            }
        }
        return total
    }

    fun part2(input: List<String>): Int {
        var maxim = 0
        for (i in input.indices) {
            for (j in input[i].indices) {
                val treeScore = countTreeVisibleScore(i, j, input)
                maxim = max(maxim, treeScore)
            }
        }
        return maxim
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 21)
    println(part2(testInput))
    check(part2(testInput) == 8)

    val input = readInput(DAY_INPUT)
    println(part1(input))
    println(part2(input))

    // 2156 too low
}