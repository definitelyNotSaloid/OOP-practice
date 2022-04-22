package lab4.map

import lab4.misc.Vector2dInt

// could have made it an abstract class
// but whaever, not like im going to add complex mechanics here
enum class CellContent(val char: Char) {
    WALL('#'),
    PLAYER('P'),
    EXIT('E')
}

interface IMazeMap {
    operator fun get(x: Int, y: Int): MutableList<CellContent>
    operator fun get(pos: Vector2dInt): MutableList<CellContent>

    val width: Int
    val height: Int

    fun addAt(content: CellContent, x: Int, y: Int)
    fun addAt(content: CellContent, pos: Vector2dInt)

    fun clearCell(x: Int, y: Int)
    fun clearCell(pos: Vector2dInt)

    fun containsAt(content: CellContent, x: Int, y: Int): Boolean
    fun containsAt(content: CellContent, pos: Vector2dInt): Boolean

    fun removeAt(content: CellContent, x: Int, y: Int): Boolean
    fun removeAt(content: CellContent, pos: Vector2dInt): Boolean

    fun withinBorders(x: Int, y: Int): Boolean
    fun withinBorders(pos: Vector2dInt): Boolean

    fun getCellWeight(x: Int, y: Int): Double
    fun getCellWeight(pos: Vector2dInt): Double
}

class MazeMap
    : IMazeMap {

    // val masks: MutableList<IWeightedMapMask> = mutableListOf()

    // list of rows, each row contains.. something. or nothing
    // size can be changed only during initialization
    // could have used array, but in the name of compatibility it will be a list
    private var map: MutableList<
            MutableList<MutableList<CellContent>>
            >

    private val _height: Int
    override val height: Int
        get() = _height

    private val _width: Int
    override val width: Int
        get() = _width

    constructor(width: Int, height: Int) {
        _width = width
        _height = height

        map = ArrayList(height)
        for (i in 0 until height) {
            map.add(ArrayList(width))

            for (j in 0 until width) {
                map[i].add(mutableListOf())
            }
        }
    }

    constructor(string: String) {
        map = mutableListOf()

        var lineLen = -1
        var lineIndex = 0
        for (line in string.splitToSequence('\n', '\r').filter { it.isNotEmpty() }) {

            if (lineLen == -1)
                lineLen = line.length
            else if (lineLen != line.length)
                throw IllegalArgumentException("bad formatting")

            map.add(ArrayList(lineLen))

            for (char in line) {
                when {
                    char.isWhitespace() -> map[lineIndex].add(mutableListOf())
                    char == CellContent.WALL.char -> map[lineIndex].add(mutableListOf(CellContent.WALL))
                    char == CellContent.PLAYER.char -> map[lineIndex].add(mutableListOf(CellContent.PLAYER))
                    char == CellContent.EXIT.char -> map[lineIndex].add(mutableListOf(CellContent.EXIT))
                    else -> throw IllegalArgumentException("unknown cell content: $char")
                }
            }

            lineIndex++
        }

        _width = lineLen
        _height = lineIndex
    }

    override fun clearCell(pos: Vector2dInt) {
        clearCell(pos.x, pos.y)
    }

    override fun clearCell(x: Int, y: Int) {
        map[y][x].clear()
    }

    override fun get(pos: Vector2dInt): MutableList<CellContent> {
        return get(pos.x, pos.y)
    }

    override fun get(x: Int, y: Int): MutableList<CellContent> {
        return map[y][x]
    }

    override fun addAt(content: CellContent, pos: Vector2dInt) {
        addAt(content, pos.x, pos.y)
    }

    override fun addAt(content: CellContent, x: Int, y: Int) {
        map[y][x].add(content)
    }

    override fun containsAt(content: CellContent, pos: Vector2dInt): Boolean {
        return containsAt(content, pos.x, pos.y)
    }

    override fun containsAt(content: CellContent, x: Int, y: Int): Boolean {
        return map[y][x].contains(content)
    }

    override fun removeAt(content: CellContent, pos: Vector2dInt): Boolean {
        return removeAt(content, pos.x, pos.y)
    }

    override fun removeAt(content: CellContent, x: Int, y: Int): Boolean {
        return map[y][x].remove(content)
    }

    override fun withinBorders(pos: Vector2dInt): Boolean {
        return withinBorders(pos.x, pos.y)
    }

    override fun withinBorders(x: Int, y: Int): Boolean {
        return (x in 0 until width
                &&
                y in 0 until height)
    }

    override fun getCellWeight(pos: Vector2dInt): Double {
        return getCellWeight(pos.x, pos.y)
    }

    override fun getCellWeight(x: Int, y: Int): Double {
        return 1.0 //masks.sumOf { it[x, y] }
    }

    override fun toString(): String {
        val sBuilder = StringBuilder()

        for (row in map) {
            for (cell in row) {
                when {
                    cell.contains(CellContent.WALL) -> sBuilder.append(CellContent.WALL.char)
                    cell.contains(CellContent.PLAYER) -> sBuilder.append(CellContent.PLAYER.char)
                    cell.contains(CellContent.EXIT) -> sBuilder.append(CellContent.EXIT.char)
                    else -> sBuilder.append(" ")
                }
            }
            sBuilder.append('\n')
        }

        return sBuilder.toString()
    }
}

//interface IWeightedMapMask {
//    operator fun get(x: Int, y: Int): Double
//    operator fun get(pos: Vector2dInt): Double
//
//    operator fun set(x: Int, y: Int, value: Double)
//    operator fun set(pos: Vector2dInt, value: Double)
//}
//
//class DefaultMapMask(map: IMazeMap) : IWeightedMapMask {
//    private val maskMatrix: MutableList<MutableList<Double>>
//
//    init {
//        maskMatrix = ArrayList(map.height)
//        for (i in 0 until map.height) {
//            maskMatrix.add(ArrayList(map.width))
//
//            for (j in 0 until map.width)
//                maskMatrix[i].add(0.0)
//        }
//    }
//
//    override fun get(pos: Vector2dInt): Double {
//        return this[pos.x, pos.y]
//    }
//
//    override fun get(x: Int, y: Int): Double {
//        return maskMatrix[y][x]
//    }
//
//    override fun set(pos: Vector2dInt, value: Double) {
//        this[pos.x, pos.y] = value
//    }
//
//    override fun set(x: Int, y: Int, value: Double) {
//        maskMatrix[y][x] = value
//    }
//}