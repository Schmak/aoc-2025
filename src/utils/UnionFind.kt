package utils

class UnionFind(n: Int) {
    private val parent = IntArray(n) { it }
    val size = IntArray(n) { 1 }

    fun find(x: Int): Int {
        var v = x
        while (v != parent[v]) {
            parent[v] = parent[parent[v]]
            v = parent[v]
        }
        return v
    }

    fun union(a: Int, b: Int): Boolean {
        val ra = find(a)
        val rb = find(b)
        if (ra == rb) return false

        if (size[ra] < size[rb]) {
            parent[ra] = rb
            size[rb] += size[ra]
        } else {
            parent[rb] = ra
            size[ra] += size[rb]
        }
        return true
    }
}