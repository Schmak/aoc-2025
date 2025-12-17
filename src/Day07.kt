import Day07.Input

object Day07 : Task<Input> {
    override fun parse(input: List<String>) =
        Input(
            source = input.first().indexOf('S'),
            width = input.first().length,
            splitters = input.asSequence().chunked(2).drop(1)
                .map { (line) ->
                    line.indices.filter { line[it] == '^' }
                }.toList()
        )

    fun part1(input: Input): Int {
        var result = 0
        var beams = setOf(input.source)
        input.splitters.forEach { splitters ->
            beams = buildSet {
                beams.forEach { beam ->
                    if (beam in splitters) {
                        add(beam - 1)
                        add(beam + 1)
                        result++
                    } else {
                        add(beam)
                    }
                }
            }
        }
        return result
    }

    fun part2(input: Input): Long {
        val x = MutableList(input.width) { 1L }
        input.splitters.reversed().forEach { splitters ->
            splitters.forEach { splitter ->
                x[splitter] = x[splitter - 1] + x[splitter + 1]
            }
        }
        return x[input.source]
    }


    fun solve() {
        val testInput = readInput("Day07_test")
        assertThat(part1(testInput)).isEqualTo(21)
        assertThat(part2(testInput)).isEqualTo(40)

        val input = readInput("Day07")
        assertThat(part1(input)).isEqualTo(1672)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(231229866702355)
        println(part2(input))
    }

    data class Input(
        val source: Int,
        val splitters: List<List<Int>>,
        val width: Int,
    )
}

fun main() = Day07.solve()
