import lab4.map.controls.PlayerController
import lab4.consoleInput.*
import lab4.consoleOutput.MapView
import lab4.map.generation.genMap
import lab4.misc.InvalidCommandException

fun main() {
    val maze = genMap()
    val controller = PlayerController(maze)
    val inputHandler = ConsoleReader(controller)
    val view = MapView(maze, controller)
    view.activate()

    try {
        var exitCommandEntered = false
        while (!exitCommandEntered) {
            try {
                exitCommandEntered = !inputHandler.read()
            }
            catch (e: InvalidCommandException) {
                println("invalid input: ${e.message}")
            }
        }
    }
    catch (e : Exception) {
        println("something happened... ${e.message}")
        println("im going to die rn. see ya")
    }

    view.die()
}