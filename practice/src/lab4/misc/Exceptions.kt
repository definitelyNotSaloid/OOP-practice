package lab4.misc

class InvalidCommandException(override val message: String?) : Exception(message) { }