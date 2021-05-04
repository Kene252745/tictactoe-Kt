package pl.pwr.tictactoe.model.dto

data class MoveDTO(
    val moveNumber: Int,
    val x: Int,
    val y: Int,
    val playerId: Long,
)
