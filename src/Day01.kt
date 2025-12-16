import java.lang.Math.floorDiv
import kotlin.math.abs

object Day01 : Task<List<Int>> {
    const val SIZE = 100
    override fun parse(input: List<String>): List<Int> =
        input.map {
            val sign = if (it[0] == 'L') -1 else 1
            sign * it.substring(1).toInt()
        }

    fun part1(input: List<Int>): Int =
        input.asSequence().scan(50) { acc, i -> (acc + i + SIZE) % SIZE }.count { it == 0 }

    fun part2(input: List<Int>): Int =
        input.asSequence().scan(50) { acc, i -> acc + i }
            .zipWithNext()
            .sumOf { (a, b) ->
                val ad = floorDiv(a, SIZE)
                val bd = floorDiv(b, SIZE)
                abs(ad - bd) + when {
                    a < b -> 0
                    a % SIZE == 0 -> -1
                    b % SIZE == 0 -> 1
                    else -> 0
                }
            }

    fun solve() {
        val testInput = readInput("Day01_test")
        assertThat(part1(testInput)).isEqualTo(3)
        assertThat(part2(testInput)).isEqualTo(6)

        val input = readInput("Day01")
        assertThat(part1(input)).isEqualTo(997)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(5978)
        println(part2(input))
    }
}

fun main() = Day01.solve()
