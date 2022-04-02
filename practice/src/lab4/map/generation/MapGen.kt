package lab4.map.generation

import lab4.map.*
import lab4.map.path.IPath
import lab4.map.path.genPath
import lab4.misc.*
import kotlin.random.Random

// TBD
// probably got to rewrite it at whole

fun generateMap(width: Int, height: Int): IMazeMap {
    // TODO prefer long paths

    val map = MazeMap(width, height)

    map.masks.add(
        RandomizedGenMask(
            map,
            10.0, 20.0,              // TODO config? something else? idk. but parameters should be editable
            Rand.next().hashCode()
        )
    )

    val mapBuildMask = BinaryMask(map)              // true for passable cell, false for wall
    val allPaths = mutableListOf<IPath>()

    val start = Vector2dInt(0, 0)
    val finish = Vector2dInt(width - 1, height - 1)

    val truePath = genPath(map, start, finish, -15.0)
        ?: throw IllegalStateException("no way its null")            // TODO randomize P and E positions

    map[start].add(CellContent.PLAYER)
    map[finish].add(CellContent.EXIT)


    genFalseTurnsRecursive(map, mapBuildMask, truePath)


    for (x in 0 until map.width) {                  // fillin map with walls
        for (y in 0 until map.height) {
            if (mapBuildMask[x, y] == 0.0)
                map[x, y].add(CellContent.WALL)
        }
    }

    return map
}

private class RandomizedGenMask(
    map: IMazeMap,
    minWeight: Double, maxWeight: Double,
    seed: Int
) : IWeightedMapMask {
    private val maskMatrix: MutableList<MutableList<Double>>

    init {
        val rng = Random(seed)

        maskMatrix = ArrayList(map.height)
        for (i in 0 until map.height) {
            maskMatrix.add(ArrayList(map.width))

            for (j in 0 until map.width)
                maskMatrix[i].add(rng.nextDouble(minWeight, maxWeight))
        }
    }

    override fun get(pos: Vector2dInt): Double {
        return this[pos.x, pos.y]
    }

    override fun get(x: Int, y: Int): Double {
        return maskMatrix[y][x]
    }

    override fun set(pos: Vector2dInt, value: Double) {
        this[pos.x, pos.y] = value
    }

    override fun set(x: Int, y: Int, value: Double) {
        maskMatrix[y][x] = value
    }
}

// zeros and positive vals (1.0)
private class BinaryMask(map: IMazeMap) : IWeightedMapMask {
    private val maskMatrix: MutableList<MutableList<Boolean>>

    init {
        maskMatrix = ArrayList(map.height)
        for (i in 0 until map.height) {
            maskMatrix.add(ArrayList(map.width))

            for (j in 0 until map.width)
                maskMatrix[i].add(false)
        }
    }

    override fun get(pos: Vector2dInt): Double {
        return this[pos.x, pos.y]
    }

    override fun get(x: Int, y: Int): Double {
        return if (maskMatrix[y][x]) 1.0 else 0.0
    }

    override fun set(pos: Vector2dInt, value: Double) {
        this[pos.x, pos.y] = value
    }

    override fun set(x: Int, y: Int, value: Double) {
        maskMatrix[y][x] = value != 0.0
    }

    operator fun set(pos: Vector2dInt, value: Boolean) {
        this[pos.x, pos.y] = value
    }

    operator fun set(x: Int, y: Int, value: Boolean) {
        maskMatrix[y][x] = value
    }
}

// TODO maxDeepness parameter
private fun genFalseTurnsRecursive(
    map: IMazeMap,
    binaryMask: BinaryMask,
    originalPath: IPath
) {
    val tmpGenMask =
        DefaultMapMask(map)                  // used to avoid false paths coming through original one too often
    map.masks.add(DefaultMapMask(map))

    for (cell in originalPath.steps()) {
        binaryMask[cell] = true
        tmpGenMask[cell] += 9999.0

        adjacentCells(cell).forEach {
            if (map.withinBorders(it))
                tmpGenMask[it] += 9999.0
        }
    }

    for (cell in originalPath.steps()) {
        if (Rand.next() > 0.99) {
            val newPath =
                genPath(map, cell, Rand.randomCellInRect(cell - Vector2dInt(10, 10), cell + Vector2dInt(10, 10)), -14.0)
            if (newPath != null)
                genFalseTurnsRecursive(map, binaryMask, newPath)
        }
    }

    map.masks.removeLast()
}