import kotlin.math.pow

object Day03 : Task<List<List<Int>>> {
    override fun parse(input: List<String>): List<List<Int>> =
        input.map { it.map(Char::digitToInt) }

    fun solve(line: List<Int>, digits: Int): Long {
        val start = line.dropLast(digits - 1)
        val max = start.max()
        val idx = start.indexOf(max)
        return if (digits == 1)
            max.toLong()
        else
            10.0.pow(digits - 1.0).toLong() * max + solve(line.drop(idx + 1), digits - 1)
    }

    fun part1(input: List<List<Int>>): Long = input.sumOf { solve(it, 2) }

    fun part2(input: List<List<Int>>): Long = input.sumOf { solve(it, 12) }

    fun solve() {
        val testInput = readInput("Day03_test")
        assertThat(part1(testInput)).isEqualTo(357)
        assertThat(part2(testInput)).isEqualTo(3121910778619)

        val input = readInput("Day03")
        assertThat(part1(input)).isEqualTo(17193)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(171297349921310)
        println(part2(input))
    }
}

fun main() = Day03.solve()
