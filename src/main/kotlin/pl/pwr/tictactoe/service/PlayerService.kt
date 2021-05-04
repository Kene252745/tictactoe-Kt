package pl.pwr.tictactoe.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.pwr.tictactoe.exceptions.NoSuchPlayerException
import pl.pwr.tictactoe.exceptions.PlayerAlreadyExistsException
import pl.pwr.tictactoe.model.Player
import pl.pwr.tictactoe.repository.PlayerRepository

@Service
@Transactional(readOnly = true)
class PlayerService(
    private val playerRepository: PlayerRepository
) {

    @Transactional
    fun createAccount(subject: String): Player {
        val player = playerRepository.findBySub(subject)
        if (player == null) {
            return playerRepository.save(Player(sub = subject))
        } else {
            throw PlayerAlreadyExistsException()
        }
    }

    fun getPlayerBySubject(subject: String): Player {
        return playerRepository.findBySub(subject) ?: throw NoSuchPlayerException(subject)
    }

    fun getPlayerById(playerId: Long): Player {
        return playerRepository.findById(playerId)
            .orElseThrow { NoSuchPlayerException(playerId) }
    }

}
