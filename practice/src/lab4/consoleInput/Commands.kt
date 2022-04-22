package lab4.consoleInput

import lab4.map.CellContent
import lab4.map.controls.IPlayerController
import lab4.map.generation.genMap
import lab4.map.path.genPath
import lab4.misc.Finder
import lab4.misc.Vector2dInt


sealed class TextCommand {
    // i should have made tryExecute : Boolean/String method here to avoid double parsing, but whaever
    abstract fun isValid(command: String): Boolean
    abstract fun execute(command: String, controller: IPlayerController)

    abstract val commandName: String
}

object GoNCellsInDirectionCommand : TextCommand() {
    private const val NORTH = "north"
    private const val EAST = "east"
    private const val SOUTH = "south"
    private const val WEST = "west"

    override val commandName: String
        get() = "move"

    override fun isValid(command: String): Boolean {
        val blocks = command.split(' ').filter { it.isNotBlank() }

        if (blocks.size != 3)
            return false

        if (blocks[0] != commandName)
            return false

        if (blocks[1] != NORTH &&
            blocks[1] != SOUTH &&
            blocks[1] != EAST &&
            blocks[1] != WEST
        )
            return false

        val stepCount = blocks[2].toIntOrNull() ?: return false
        if (stepCount <= 0)
            return false

        return true
    }

    override fun execute(command: String, controller: IPlayerController) {
        val blocks = command.split(' ')

        if (blocks.size != 3)
            throw IllegalArgumentException("invalid arg count. Command $commandName requires 2 args")

        if (blocks[0] != commandName)
            throw IllegalStateException("why did you call this? CHECK COMMAND NAME")

        val stepCount = blocks[2].toIntOrNull() ?: throw IllegalArgumentException("${blocks[2]} is not a valid integer")
        if (stepCount <= 0)
            throw IllegalArgumentException("step count - ${blocks[2]} - must be positive")

        val status = when (blocks[1]) {
            NORTH -> controller.tryMovePlayer(Vector2dInt.NORTH * stepCount)
            SOUTH -> controller.tryMovePlayer(Vector2dInt.SOUTH * stepCount)
            EAST -> controller.tryMovePlayer(Vector2dInt.EAST * stepCount)
            WEST -> controller.tryMovePlayer(Vector2dInt.WEST * stepCount)
            else -> throw IllegalArgumentException("unknown direction: ${blocks[1]}")
        }

        if (!status)
            throw IllegalArgumentException("Invalid move")
    }
}

object ResetCommand : TextCommand() {
    override val commandName: String
        get() = "reset"

    override fun isValid(command: String): Boolean {
        if (command.trim()!=commandName)
            return false

        return true
    }

    override fun execute(command: String, controller: IPlayerController) {
        val newMap = genMap()
        Finder.view!!.map = newMap
        controller.attachToMap(newMap)
    }
}

object UsePathfinder : TextCommand() {
    override val commandName: String
        get() = "showpath"

    override fun isValid(command: String): Boolean {
        if (command.trim()!= ResetCommand.commandName)
            return false

        return true
    }

    override fun execute(command: String, controller: IPlayerController) {
        val map = Finder.view!!.map

        // i bet there is a better way to assign value
        // but the less you know...
        var exit : Vector2dInt? = null
        for (x in 0 until map.width) {
            for (y in 0 until map.height) {
                if (map[x,y].contains(CellContent.EXIT)) {
                    exit = Vector2dInt(x,y)
                    break
                }
            }
            if (exit!=null)
                break
        }

        if (exit==null)
            throw IllegalStateException("map has no exit on it")

        val path = genPath(map, controller.playerPos, exit) ?: throw IllegalStateException("cant find legitimate path")

        path.apply(controller)
    }
}