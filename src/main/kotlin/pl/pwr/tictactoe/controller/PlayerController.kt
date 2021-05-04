package pl.pwr.tictactoe.controller

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import pl.pwr.tictactoe.model.Player
import pl.pwr.tictactoe.model.dto.SummaryDTO
import pl.pwr.tictactoe.service.GameService
import pl.pwr.tictactoe.service.PlayerService

@RestController
@RequestMapping("/player")
class PlayerController(
    private val playerService: PlayerService,
    private val gameService: GameService,
) {

    @PostMapping
    fun createPlayer(@AuthenticationPrincipal jwt: Jwt) {
        playerService.createAccount(jwt.subject)
    }

    @GetMapping("/{playerId}")
    fun getPlayer(@PathVariable playerId: Long): Player {
        return playerService.getPlayerById(playerId)
    }

    @GetMapping("/{playerId}/summary")
    fun getPlayerSummary(@PathVariable playerId: Long): SummaryDTO {
        return gameService.getSummary(playerId)
    }

}
