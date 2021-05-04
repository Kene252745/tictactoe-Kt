package pl.pwr.tictactoe.model

import pl.pwr.tictactoe.model.dto.GameDTO
import javax.persistence.*

@Entity
@Table(name = "games")
data class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_id_generator")
    @SequenceGenerator(name = "game_id_generator", sequenceName = "game_id_seq")
    @Column(name = "id")
    val id: Long = 0L,

    @ManyToOne
    @JoinColumn(name = "player_1_id")
    val player1: Player,

    @ManyToOne
    @JoinColumn(name = "player_2_id")
    val player2: Player,

    @Column(name = "size")
    val size: Int,

    @Column(name = "winning_number")
    val winningNumber: Int,

    @Column(name = "winner")
    @Enumerated(EnumType.STRING)
    var winner: Winner,
) {

    @OneToMany(mappedBy = "game")
    val moves: MutableList<Move> = mutableListOf()

    fun getMove(x: Int, y: Int) = moves.find { move -> move.x == x && move.y == y }

    fun lastMove() = moves.lastOrNull()

    fun toDTO() = GameDTO(id, player1.id, player2.id, size, winningNumber)

    fun printDebug(): String {
        val evenLine = (0 until size).joinToString("+", "---+", "\n") { "---" }
        val headerLine = (0 until size).joinToString("|", "x\\y|", "\n$evenLine") { " $it " }

        return (0 until size).joinToString(separator = "\n$evenLine", prefix = headerLine, postfix = "\n") { x ->
            (0 until size).joinToString(separator = "|", prefix = " $x |") { y ->
                when (getMove(x, y)?.player?.id) {
                    player1.id -> " o "
                    player2.id -> " x "
                    else -> "   "
                }
            }
        }
    }

}
