import utils.Point
import utils.Vector

object Day04 : Task<Set<Point>> {
    override fun parse(input: List<String>): Set<Point> = buildSet {
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                if (ch == '@')
                    add(Point(x, y))
            }
        }
    }

    val neighbors = buildSet {
        for (dx in -1..1) {
            for (dy in -1..1) {
                if (dx == 0 && dy == 0) continue
                add(Vector(dx, dy))
            }
        }
    }

    fun part1(input: Set<Point>): Int =
        input.count { p -> neighbors.count { v -> (p + v) in input } < 4 }

    fun part2(input: Set<Point>): Int {
        val set = input.toMutableSet()
        while (true) {
            val remove = set.filterTo(mutableSetOf()) { p -> neighbors.count { v -> (p + v) in set } < 4 }
            if (remove.isEmpty()) break
            set -= remove
        }
        return input.size - set.size
    }

    fun solve() {
        val testInput = readInput("Day04_test")
        assertThat(part1(testInput)).isEqualTo(13)
        assertThat(part2(testInput)).isEqualTo(43)

        val input = readInput("Day04")
        assertThat(part1(input)).isEqualTo(1551)
        println(part1(input))
        assertThat(part2(input)).isEqualTo(9784)
        println(part2(input))
    }

}

fun main() = Day04.solve()
