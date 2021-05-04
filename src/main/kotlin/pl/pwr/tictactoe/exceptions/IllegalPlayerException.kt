package pl.pwr.tictactoe.exceptions

class IllegalPlayerException(playerId: Long) :
    IllegalArgumentException("Player with id=$playerId is not permitted to make a move")
