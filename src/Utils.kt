import kotlin.io.path.Path
import kotlin.io.path.readText

interface Task<INPUT> {
    fun parse(input: List<String>): INPUT

    fun readInput(name: String) = parse(Path("res/$name.txt").readText().trim().lines())
}

data class Assertion<T>(val value: T) {
    fun isEqualTo(other: T) {
        check(value == other) { "Expecting value to be '$other' but was '$value'" }
    }
}

fun <T> assertThat(obj: T) = Assertion(obj)
