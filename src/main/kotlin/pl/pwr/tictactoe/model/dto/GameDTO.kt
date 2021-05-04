package pl.pwr.tictactoe.model.dto

data class GameDTO(
    val id: Long,
    val player1: Long,
    val player2: Long,
    val size: Int,
    val winningNumber: Int
)
