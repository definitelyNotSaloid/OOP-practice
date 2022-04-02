package lab4.map.path

import lab4.map.CellContent
import lab4.map.IMazeMap
import lab4.map.controls.IPlayerController
import lab4.misc.*
import kotlin.math.abs


interface IPath {
    fun apply(controller: IPlayerController)
    fun tryApply(controller: IPlayerController): Boolean

    fun steps(): Sequence<Vector2dInt>         // returns sequence of every cell that will be visited in order. INCLUDING start and destination
}

class SingleStepPath(moves: List<Vector2dInt>, private val start: Vector2dInt) : IPath {
    private val moves: List<Vector2dInt>

    init {
        moves.forEach {
            require(
                it == Vector2dInt.EAST
                        || it == Vector2dInt.WEST
                        || it == Vector2dInt.NORTH
                        || it == Vector2dInt.SOUTH
            )
            { "move $it is not a unit step" }

        }

        this.moves =
            ArrayList(moves)                   // i could have come up with something better than double array creation, but whaever. TODO
    }

    override fun apply(controller: IPlayerController) {
        val start = controller.playerPos
        for (move in moves) {
            if (!controller.tryMovePlayer(move)) {
                controller.playerPos = start
                throw IllegalStateException("Path can't be applied")
            }
        }
    }

    override fun tryApply(controller: IPlayerController): Boolean {
        val start = controller.playerPos
        for (move in moves) {
            if (!controller.tryMovePlayer(move)) {
                controller.playerPos = start
                return false
            }
        }

        return true
    }

    override fun steps(): Sequence<Vector2dInt> {
        return sequence {
            yield(start)

            var curCell = start
            moves.forEach {
                curCell += it
                yield(curCell)
            }
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (move in moves)
            sb.append("$move\n")

        return sb.toString()
    }
}

fun genPath(
    map: IMazeMap,
    start: Vector2dInt,
    destination: Vector2dInt,
    approxStepCost: Double = 1.0                // better not set it for 0.0
): IPath? {
    if (!map.withinBorders(start) || !map.withinBorders(destination))
        return null

    var cell = genCellRecordChain(map, start, destination, approxStepCost) ?: return null

    val moves = mutableListOf<Vector2dInt>()

    while (cell.parent != null) {
        moves.add(cell.position - cell.parent!!.position)                   // this is always unit-len vector. at least it should be

        cell = cell.parent ?: throw IllegalStateException("IT IS NOT NULL. if it is, idk what to do")
    }

    moves.reverse()             // last jump was added first and first - at the end of list building
    return SingleStepPath(moves, start)
}


// null if there is no valid path
// returns last CR
private fun genCellRecordChain(
    map: IMazeMap,
    start: Vector2dInt,
    destination: Vector2dInt,
    stepCost: Double
): CellRecord? {
    // good ol' A* algorithm

    val toBeVisited = mutableListOf(
        CellRecord(start, 0.0, distanceFromTo(start, destination).toDouble(), null)
    )

    val visited = mutableListOf<Vector2dInt>()

    while (toBeVisited.isNotEmpty()) {
        val newCell = toBeVisited.minByOrNull { it.totalCost }
            ?: throw IllegalStateException("something went wrong. horribly wrong")
        // just getting rid of compiler warnings here. it cant return null here. absolutely. like, if you are sure - it will not. never happened, never going to happen.

        if (newCell.position == destination)
            return newCell

        toBeVisited.remove(newCell)                     // TODO optimize. double list parsing
        visited.add(newCell.position)

        adjacentCells(newCell.position).forEach() { adjacentCell ->
            val cellRec = toBeVisited.find() {
                it.position == adjacentCell
            }

            if (cellRec == null) {
                if (map.withinBorders(adjacentCell)                     // if this is passable, non-visited cell
                    && !map.containsAt(CellContent.WALL, adjacentCell)
                    && !visited.contains(adjacentCell)
                ) {
                    toBeVisited.add(
                        CellRecord(
                            adjacentCell,
                            newCell.costStartToThis + map.getCellWeight(adjacentCell) + stepCost,
                            distanceFromTo(adjacentCell, destination).toDouble() * stepCost,
                            newCell
                        )
                    )
                }
            } else {      // TODO lil clean up
                if (cellRec.costStartToThis > newCell.costStartToThis + map.getCellWeight(adjacentCell) + stepCost) {
                    cellRec.costStartToThis = newCell.costStartToThis + map.getCellWeight(adjacentCell) + stepCost
                    cellRec.parent = newCell
                }
            }
        }
    }

    return null
}

private class CellRecord(
    val position: Vector2dInt,
    var costStartToThis: Double,
    var costThisToDestinationDirectly: Double,
    var parent: CellRecord?
) {
    val totalCost
        get() = costStartToThis + costThisToDestinationDirectly
}

// direct path len, considering we can only go straight horizontally/vertically
private fun distanceFromTo(pos1: Vector2dInt, pos2: Vector2dInt): Int {
    val delta = pos1 - pos2
    return abs(delta.x) + abs(delta.y)
}