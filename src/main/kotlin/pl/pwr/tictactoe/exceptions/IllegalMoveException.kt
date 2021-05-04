package pl.pwr.tictactoe.exceptions

class IllegalMoveException(x: Int, y: Int) : IllegalArgumentException("The field ($x, $y) is already filled.")
