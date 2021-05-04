package pl.pwr.tictactoe.exceptions

class NoSuchGameException(gameId: Long) : NoSuchElementException("Game with id=$gameId does not exist")
