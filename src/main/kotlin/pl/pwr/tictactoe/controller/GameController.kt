package pl.pwr.tictactoe.controller

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import pl.pwr.tictactoe.model.dto.*
import pl.pwr.tictactoe.service.GameService

@RestController
@RequestMapping("/game")
class GameController(
    private val gameService: GameService,
) {

    @GetMapping("/{gameId}")
    fun get(@PathVariable gameId: Long): GameDTO {
        return gameService.get(gameId).toDTO()
    }

    @GetMapping("/{gameId}/winner")
    fun getWinner(@PathVariable gameId: Long): WinnerDTO {
        val winner = gameService.getWinner(gameId)
        return WinnerDTO(gameId, winner)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@AuthenticationPrincipal jwt: Jwt, @RequestBody gameDTO: CreateGameDTO): GameDTO {
        return gameService.save(jwt.subject, gameDTO.opponentId, gameDTO.size, gameDTO.winningNumber).toDTO()
    }

    @PostMapping("/{gameId}/move")
    @ResponseStatus(HttpStatus.CREATED)
    fun saveMove(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable gameId: Long,
        @RequestBody moveDTO: CreateMoveDTO
    ): MoveDTO {
        return gameService.saveMove(gameId, jwt.subject, moveDTO.x, moveDTO.y).toDTO()
    }

}
