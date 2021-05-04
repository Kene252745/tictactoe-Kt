package pl.pwr.tictactoe.repository

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import pl.pwr.tictactoe.model.Player

@Repository
interface PlayerRepository : PagingAndSortingRepository<Player, Long> {

    fun findBySub(sub: String): Player?

}
