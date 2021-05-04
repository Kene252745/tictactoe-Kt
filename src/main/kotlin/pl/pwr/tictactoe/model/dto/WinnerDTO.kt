package pl.pwr.tictactoe.model.dto

import pl.pwr.tictactoe.model.Winner

data class WinnerDTO(
    val gameId: Long,
    val winner: Winner
)
