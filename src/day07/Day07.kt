package day07

import readInput

private const val DAY = "07"
private const val DAY_TEST = "day${DAY}/Day${DAY}_test"
private const val DAY_INPUT = "day${DAY}/Day${DAY}"
fun main() {

    fun parseInput(commands: List<String>): Directory {
        val queue = ArrayDeque<Directory>()
        queue.add(Directory("/")) // create root
        var index = 1
        while (index <= commands.lastIndex) {
            if (commands[index] == "$ ls") {
                val currentDirectory = queue.first()
                index++
                while (index <= commands.lastIndex && !commands[index].startsWith("$")) {
                    if (commands[index].startsWith("dir")) {
                        val dir = Directory(commands[index].removePrefix("dir "))
                        currentDirectory.directories.add(dir)
                    } else {
                        val (size, name) = commands[index].split(' ')
                        currentDirectory.files.add(File(name, size.toInt()))
                    }
                    index++
                }
            } else if (commands[index] == "$ cd ..") {
                queue.removeFirst()
                index++
            } else if (commands[index].startsWith("$ cd ")) {
                val dir = commands[index].removePrefix("$ cd ")
                val currentDirectory = queue.first()
                queue.add(0, currentDirectory.directories.single { dir == it.name })
                index++
            }
        }
        return queue.last() // should be root
    }

    fun computeSizes(root: Directory): Int {
        val dirSize = root.files.sumOf { it.size } + root.directories.sumOf {
            computeSizes(it)
        }
        root.size = dirSize
        return dirSize
    }

    fun sum(root: Directory): Int {
        var initialSum = if (root.size < 100000) root.size else 0
        initialSum += root.directories.sumOf { sum(it) }
        return initialSum
    }

    fun part1(input: List<String>): Int {
        val root = parseInput(input)
        computeSizes(root)
        return sum(root)
    }

    val totalSpace = 70000000
    val targetMinSpace = 30000000
    var unusedSpace = 0
    var minDirectory: Directory? = null

    fun minDelete(root: Directory) {
        if (unusedSpace + root.size >= targetMinSpace && (minDirectory == null || minDirectory!!.size > root.size)) {
            minDirectory = root
        }
        root.directories.forEach { minDelete(it) }
    }

    fun part2(input: List<String>): Int {
        val root = parseInput(input)
        computeSizes(root)
        unusedSpace = totalSpace - root.size
        minDelete(root)
        return minDirectory!!.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(DAY_TEST)
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput(DAY_INPUT)
    println(part1(input))
    println(part2(input))
}

data class File(val name: String, val size: Int)
data class Directory(
    val name: String,
    val files: MutableList<File> = mutableListOf(),
    val directories: MutableList<Directory> = mutableListOf()
) {
    var size: Int = 0
}
