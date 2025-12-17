import Day05.Input

object Day05 : Task<Input> {
    override fun parse(input: List<String>): Input {
        val emptyLine = input.indexOfFirst { it.isBlank() }
        return Input(
            ranges = input.take(emptyLine).map { line ->
                line.split("-").map { it.toLong() }.let { (start, end) -> start..end }
            },
            requests = input.drop(emptyLine + 1).map { it.toLong() }
        )
    }

    fun part1(input: Input): Int =
        input.requests.count { req -> input.ranges.any { req in it } }

    fun part2(input: Input): Long {
        val ranges = input.ranges.sortedBy { it.first }
        val (acc, lastRange) = ranges.fold(0L to ranges.first()) { (acc, prevRange), newRange ->
            if (prevRange.last < newRange.first)
                (acc + prevRange.size) to newRange
            else
                acc to LongRange(prevRange.first, maxOf(prevRange.last, newRange.last))
        }
        return acc + lastRange.size
    }

    fun solve() {
        val testInput = readInput("Day05_test")
        assertThat(part1(testInput)).isEqualTo(3)
        assertThat(part2(testInput)).isEqualTo(14)

        val input = readInput("Day05")
        assertThat(part1(input)).isEqualTo(598)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(360341832208407)
        println(part2(input))
    }

    private val LongRange.size: Long get() = last - first + 1

    data class Input(
        val ranges: List<LongRange>,
        val requests: List<Long>
    )
}

fun main() = Day05.solve()
