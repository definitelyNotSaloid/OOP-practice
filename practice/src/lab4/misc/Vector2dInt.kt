package lab4.misc

data class Vector2dInt(var x: Int, var y: Int) {
    constructor() : this(0, 0)

    operator fun plus(other: Vector2dInt): Vector2dInt {
        return Vector2dInt(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2dInt): Vector2dInt {
        return Vector2dInt(x - other.x, y - other.y)
    }

    companion object {
        val NORTH = Vector2dInt(0, -1)
        val EAST = Vector2dInt(1, 0)
        val SOUTH = Vector2dInt(0, 1)
        val WEST = Vector2dInt(-1, 0)
    }

    override fun toString(): String {
        return "($x, $y)"
    }

    operator fun times(scalar: Int): Vector2dInt {
        return Vector2dInt(x * scalar, y * scalar)
    }
}

fun adjacentCells(cell: Vector2dInt): List<Vector2dInt> {
    return listOf(
        cell + Vector2dInt.NORTH,
        cell + Vector2dInt.EAST,
        cell + Vector2dInt.SOUTH,
        cell + Vector2dInt.WEST,
    )
}