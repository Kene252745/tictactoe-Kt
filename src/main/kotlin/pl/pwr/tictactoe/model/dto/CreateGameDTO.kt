package pl.pwr.tictactoe.model.dto

data class CreateGameDTO(
    val opponentId: Long,
    val size: Int = 10,
    val winningNumber: Int = 5
)
