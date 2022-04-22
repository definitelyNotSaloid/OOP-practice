package lab4.map.controls

import lab4.map.*
import lab4.misc.IListener
import lab4.misc.Vector2dInt


interface IPlayerController {
    var playerPos : Vector2dInt
    val playerAtExit : Boolean

    fun attachToMap(map : IMazeMap)

    fun tryMovePlayer(move : Vector2dInt) : Boolean
}

class PlayerController(private var map : IMazeMap) : IPlayerController {

    val notifyOnStateChange = mutableListOf<IListener>()

    private var _playerPos = Vector2dInt(0,0)             // primary ctor will change those values

    override fun attachToMap(map: IMazeMap) {
        this.map = map
        notifyOnStateChange.forEach { it.notifyListener() }
    }

    override val playerAtExit: Boolean
        get() = map.containsAt(CellContent.EXIT, playerPos)

    override var playerPos: Vector2dInt
        get() {
            updatePlayerPos()
            return _playerPos
        }
        set(value) {
            require (map.withinBorders(value))
            { "new position is out of map borders" }

            updatePlayerPos()

            map.removeAt(CellContent.PLAYER, _playerPos)
            map.addAt(CellContent.PLAYER, value)
            _playerPos = value
        }

    init {
        updatePlayerPos()
    }

    override fun tryMovePlayer(move : Vector2dInt) : Boolean {
        require(move.x == 0 || move.y == 0)
        { "diagonal moves are not supported" }

        updatePlayerPos()
        val newPos = Vector2dInt(move.x + _playerPos.x, move.y + _playerPos.y)
        if (!map.withinBorders(newPos))
            return false


        if (move.x!=0) {
            for (x in
            if (move.x>0) _playerPos.x..newPos.x          // positive x shift
            else newPos.x.._playerPos.x) {                // negative
                if (map.containsAt(CellContent.WALL,x,_playerPos.y))
                    return false
            }
        }
        else {
            for (y in
            if (move.y>0) _playerPos.y..newPos.y          // positive y shift
            else newPos.y.._playerPos.y) {                // negative
                if (map.containsAt(CellContent.WALL, _playerPos.x, y))
                    return false
            }
        }

        playerPos = newPos
        notifyOnStateChange.forEach { it.notifyListener() }
        return true
    }

    private fun updatePlayerPos(){
        if (map.containsAt(CellContent.PLAYER, _playerPos))
            return

        for (x in 0 until map.width){
            for (y in 0 until map.height) {
                if (map.containsAt(CellContent.PLAYER,x,y)){
                    _playerPos.x = x
                    _playerPos.y = y
                    return
                }
            }
        }

        throw IllegalStateException("Map has no player on it")
    }
}