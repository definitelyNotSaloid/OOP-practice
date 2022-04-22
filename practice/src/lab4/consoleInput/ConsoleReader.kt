package lab4.consoleInput

import lab4.map.controls.IPlayerController
import lab4.misc.InvalidCommandException

class ConsoleReader(private val playerController: IPlayerController) {
    var exitCommand: String = "exit"

    companion object {
        private val commandInstances =
            TextCommand::class.sealedSubclasses.mapNotNull {
                it.objectInstance
            }
    }

    // returns false when exit command is entered
    fun read(): Boolean {
        val input = readln().trim()
        if (input == exitCommand)
            return false

        for (command in commandInstances) {
            if (command.commandName == input.substringBefore(' ')) {
                try {
                    command.execute(input, playerController)
                }
                catch (e: IllegalArgumentException) {
                    throw InvalidCommandException(e.message)
                }
                return true
            }
        }

        throw InvalidCommandException("unknown command: $input")
    }
}