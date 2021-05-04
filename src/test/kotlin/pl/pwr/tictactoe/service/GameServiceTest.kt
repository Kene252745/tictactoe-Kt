package pl.pwr.tictactoe.service

import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pl.pwr.tictactoe.exceptions.GameFinishedException
import pl.pwr.tictactoe.exceptions.IllegalMoveException
import pl.pwr.tictactoe.exceptions.IllegalPlayerException
import pl.pwr.tictactoe.model.Game
import pl.pwr.tictactoe.model.Move
import pl.pwr.tictactoe.model.Player
import pl.pwr.tictactoe.model.Winner
import pl.pwr.tictactoe.repository.GameRepository
import java.util.*

class GameServiceTest {

    private lateinit var game: Game
    private val player1 = Player(1L, "1")
    private val player2 = Player(2L, "2")
    private val player3 = Player(3L, "3")

    private val players = listOf(player1, player2, player3)
    private val games = mutableListOf<Game>()
    private val moves = mutableListOf<Move>()
    private val mockGamesRepository = mock<GameRepository>()
    private val mockPlayerService = mock<PlayerService>()
    private val gameService: GameService = GameService(mockGamesRepository, mockPlayerService)

    @BeforeEach
    fun beforeEach() {
        val size = 10
        val winningNumber = 5

        val saveGameCaptor = argumentCaptor<Game>()
        whenever(mockGamesRepository.save(saveGameCaptor.capture())).then {
            if (games.none { game -> game.id == saveGameCaptor.lastValue.id }) {
                games.add(saveGameCaptor.lastValue)
            }
            saveGameCaptor.lastValue
        }

        val findGameByIdCaptor = argumentCaptor<Long>()
        whenever(mockGamesRepository.findById(findGameByIdCaptor.capture())).then {
            val game = games.find { game -> game.id == findGameByIdCaptor.lastValue }
            Optional.ofNullable(game)
        }

        val getPlayerByIdCaptor = argumentCaptor<Long>()
        whenever(mockPlayerService.getPlayerById(getPlayerByIdCaptor.capture())).then {
            players.find { player -> player.id == getPlayerByIdCaptor.lastValue }
        }

        val getPlayerBySubCaptor = argumentCaptor<String>()
        whenever(mockPlayerService.getPlayerBySubject(getPlayerBySubCaptor.capture())).then {
            players.find { player -> player.sub == getPlayerBySubCaptor.lastValue }
        }

        games.clear()
        moves.clear()
        game = gameService.save(player1.sub, player2.id, size, winningNumber)

    }

    @Test
    fun `initial board should be empty`() {
        for (y in 0 until game.size) {
            for (x in 0 until game.size) {
                assertNull(game.getMove(x, y))
            }
        }
    }


    @Test
    fun `player1 should not be able to move when it's player2's turn`() {
        assertThrows(IllegalPlayerException::class.java) {
            gameService.saveMove(game.id, player1.sub, 0, 0)
            gameService.saveMove(game.id, player1.sub, 0, 1)
        }
    }

    @Test
    fun `player2 should not be able to move when it's player1's turn`() {
        assertThrows(IllegalPlayerException::class.java) {
            gameService.saveMove(game.id, player1.sub, 0, 0)
            gameService.saveMove(game.id, player2.sub, 0, 1)
            gameService.saveMove(game.id, player2.sub, 0, 2)
        }
    }

    @Test
    fun `making move on the filled field should fail`() {
        assertThrows(IllegalMoveException::class.java) {
            gameService.saveMove(game.id, player1.sub, 0, 0)
            gameService.saveMove(game.id, player2.sub, 0, 0)
        }
    }

    @Test
    fun `player not attached to the board should not be able to make move`() {
        assertThrows(IllegalPlayerException::class.java) {
            gameService.saveMove(game.id, player3.sub, 0, 0)
        }
    }


}
