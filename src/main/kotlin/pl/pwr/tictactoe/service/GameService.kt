package pl.pwr.tictactoe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.pwr.tictactoe.exceptions.GameFinishedException
import pl.pwr.tictactoe.exceptions.IllegalMoveException
import pl.pwr.tictactoe.exceptions.IllegalPlayerException
import pl.pwr.tictactoe.exceptions.NoSuchGameException
import pl.pwr.tictactoe.model.Game
import pl.pwr.tictactoe.model.Move
import pl.pwr.tictactoe.model.Player
import pl.pwr.tictactoe.model.Winner
import pl.pwr.tictactoe.model.dto.SummaryDTO
import pl.pwr.tictactoe.repository.GameRepository

@Service
@Transactional(readOnly = true)
class GameService(
    private val gameRepository: GameRepository,
    private val playerService: PlayerService,
) {

    fun get(gameId: Long): Game {
        return gameRepository.findById(gameId).orElseThrow { NoSuchGameException(gameId) }
    }

    fun getWinner(gameId: Long): Winner = get(gameId).winner

    @Transactional
    fun save(
        playerSubject: String,
        opponentId: Long,
        size: Int = 10,
        winningNumber: Int = 5,
    ): Game {
        val game = Game(
            player1 = playerService.getPlayerBySubject(playerSubject),
            player2 = playerService.getPlayerById(opponentId),
            size = size,
            winningNumber = winningNumber,
            winner = Winner.NONE
        )
        return gameRepository.save(game)
    }

    @Transactional
    fun saveMove(gameId: Long, playerSubject: String, x: Int, y: Int): Move {
        val game = get(gameId)
        val player = playerService.getPlayerBySubject(playerSubject)
        val move = makeMove(game, player, x, y)
        gameRepository.save(game)
        return move
    }

    @Transactional
    fun getSummary(playerId: Long): SummaryDTO {
        val summary1 = gameRepository.findSummaryForPlayer1(playerId)
        val summary2 = gameRepository.findSummaryForPlayer2(playerId)
        val wins = (summary1.find { it.status == Winner.PLAYER_1 }?.count
            ?: 0) + (summary2.find { it.status == Winner.PLAYER_2 }?.count ?: 0)
        val loses = (summary1.find { it.status == Winner.PLAYER_2 }?.count
            ?: 0) + (summary2.find { it.status == Winner.PLAYER_1 }?.count ?: 0)
        val draws = (summary1.find { it.status == Winner.DRAW }?.count
            ?: 0) + (summary2.find { it.status == Winner.DRAW }?.count ?: 0)
        return SummaryDTO(wins, loses, draws)
    }

    private fun makeMove(game: Game, player: Player, x: Int, y: Int): Move {
        validateMove(game, player.id, x, y)
        val move = Move(game = game, moveNumber = game.moves.size, x = x, y = y, player = player)
        game.moves.add(move)
        game.winner = findWinner(game)
        return move
    }

    private fun validateMove(game: Game, playerId: Long, x: Int, y: Int) {
        when {
            //there is already a winner
            game.winner != Winner.NONE -> throw GameFinishedException()

            //the move is made by a player not attached to the game
            playerId != game.player1.id && playerId != game.player2.id -> throw IllegalPlayerException(playerId)

            //the move is made by the same player as last time
            playerId == game.lastMove()?.player?.id ?: game.player2.id -> throw IllegalPlayerException(playerId)

            //the field is already filled
            game.getMove(x, y) != null -> throw IllegalMoveException(x, y)

            //the move has invalid coordinates
            x < 0 || x >= game.size || y < 0 || y >= game.size -> throw IllegalMoveException(x, y)
        }
    }

    private fun findWinner(game: Game): Winner = when (game.moves.size) {
        0 -> Winner.NONE
        game.size * game.size -> Winner.DRAW
        else -> {
            val winnerId = findWinnerInRow(game)
                ?: findWinnerInColumn(game)
                ?: findWinnerInNWSEDiagonal(game)
                ?: findWinnerInNESWDiagonal(game)

            when (winnerId) {
                game.player1.id -> Winner.PLAYER_1
                game.player2.id -> Winner.PLAYER_2
                else -> Winner.NONE
            }
        }
    }

    private fun findWinnerInRow(game: Game): Long? {
        val lastMove = game.lastMove() ?: throw IllegalStateException()

        var left = 0
        var right = 0

        var y = lastMove.y
        while (--y >= 0) {
            if (game.getMove(lastMove.x, y)?.player?.id == lastMove.player.id) {
                left++
            } else {
                break
            }
        }

        y = lastMove.y
        while (++y <= game.size - 1) {
            if (game.getMove(lastMove.x, y)?.player?.id == lastMove.player.id) {
                right++
            } else {
                break
            }
        }

        return when {
            left + right + 1 >= game.winningNumber -> lastMove.player.id
            else -> null
        }
    }

    private fun findWinnerInColumn(game: Game): Long? {
        val lastMove = game.lastMove() ?: throw IllegalStateException()

        var up = 0
        var down = 0

        var x = lastMove.x
        while (--x >= 0) {
            if (game.getMove(x, lastMove.y)?.player?.id == lastMove.player.id) {
                up++
            } else {
                break
            }
        }

        x = lastMove.x
        while (++x <= game.size - 1) {
            if (game.getMove(x, lastMove.y)?.player?.id == lastMove.player.id) {
                down++
            } else {
                break
            }
        }

        return when {
            up + down + 1 >= game.winningNumber -> lastMove.player.id
            else -> null
        }
    }

    private fun findWinnerInNWSEDiagonal(game: Game): Long? {
        val lastMove = game.lastMove() ?: throw IllegalStateException()

        var upLeft = 0
        var downRight = 0

        var x = lastMove.x
        var y = lastMove.y
        while (--x >= 0 && --y >= 0) {
            if (game.getMove(x, y)?.player?.id == lastMove.player.id) {
                upLeft++
            } else {
                break
            }
        }

        x = lastMove.x
        y = lastMove.y
        while (++x <= game.size - 1 && ++y <= game.size - 1) {
            if (game.getMove(x, y)?.player?.id == lastMove.player.id) {
                downRight++
            } else {
                break
            }
        }

        return when {
            upLeft + downRight + 1 >= game.winningNumber -> lastMove.player.id
            else -> null
        }
    }

    private fun findWinnerInNESWDiagonal(game: Game): Long? {
        val lastMove = game.lastMove() ?: throw IllegalStateException()

        var upRight = 0
        var downLeft = 0

        var x = lastMove.x
        var y = lastMove.y
        while (--x >= 0 && ++y <= game.size - 1) {
            if (game.getMove(x, y)?.player?.id == lastMove.player.id) {
                upRight++
            } else {
                break
            }
        }

        x = lastMove.x
        y = lastMove.y
        while (++x <= game.size - 1 && --y >= 0) {
            if (game.getMove(x, y)?.player?.id == lastMove.player.id) {
                downLeft++
            } else {
                break
            }
        }

        return when {
            upRight + downLeft + 1 >= game.winningNumber -> lastMove.player.id
            else -> null
        }
    }

}
