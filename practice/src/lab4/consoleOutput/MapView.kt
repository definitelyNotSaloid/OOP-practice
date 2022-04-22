package lab4.consoleOutput

import lab4.map.IMazeMap
import lab4.map.controls.PlayerController
import lab4.misc.Finder
import lab4.misc.JustCallLambdaListener


class MapView(var map: IMazeMap, private val controller: PlayerController) {
    private val listener : JustCallLambdaListener

    init {
        listener = JustCallLambdaListener {
            this.draw()
            this.victoryCheck()
        }
        controller.notifyOnStateChange.add(listener)
    }

    fun activate() {
        Finder.view = this
        draw()
    }

    fun die() {
        controller.notifyOnStateChange.remove(listener)
        if (Finder.view == this)
            Finder.view = null
    }

    private fun victoryCheck() {
        if (controller.playerAtExit) {
            println("Victory!!111!")
        }
    }

    private fun draw() {
        println(map)
    }
}