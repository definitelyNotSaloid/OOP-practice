package lab4.misc

interface IListener {
    fun notifyListener()
}

class JustCallLambdaListener(private val lambda : () -> Unit) : IListener {
    override fun notifyListener() {
        lambda()
    }
}