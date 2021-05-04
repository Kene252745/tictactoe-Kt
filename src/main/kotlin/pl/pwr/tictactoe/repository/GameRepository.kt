package pl.pwr.tictactoe.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pl.pwr.tictactoe.model.Game
import pl.pwr.tictactoe.model.Summary

@Repository
interface GameRepository : PagingAndSortingRepository<Game, Long> {

    @Query("SELECT g.winner AS status, COUNT(g.winner) AS count FROM Game g WHERE g.player1.id = :playerId GROUP BY g.winner")
    fun findSummaryForPlayer1(@Param("playerId") playerId: Long): List<Summary>

    @Query("SELECT g.winner AS status, COUNT(g.winner) AS count FROM Game g WHERE g.player2.id = :playerId GROUP BY g.winner")
    fun findSummaryForPlayer2(@Param("playerId") playerId: Long): List<Summary>

}
