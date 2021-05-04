package pl.pwr.tictactoe.exceptions

class NoSuchPlayerException(uniqueId: Any) : NoSuchElementException(makeMessage(uniqueId))

fun makeMessage(uniqueId: Any): String {
    return when (uniqueId) {
        is Long -> "Player with id=$uniqueId does not exist"
        is String -> "Player with subject=$uniqueId does not exist"
        else -> throw IllegalStateException()
    }
}
