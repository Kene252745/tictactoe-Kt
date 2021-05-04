package pl.pwr.tictactoe.model

import pl.pwr.tictactoe.model.dto.MoveDTO
import javax.persistence.*

@Entity
@Table(name = "moves")
data class Move(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "move_id_generator")
    @SequenceGenerator(name = "move_id_generator", sequenceName = "move_id_seq")
    @Column(name = "id")
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "game_id")
    val game: Game,

    @Column(name = "move_number")
    val moveNumber: Int,

    @Column(name = "x")
    val x: Int,

    @Column(name = "y")
    val y: Int,

    @ManyToOne
    @JoinColumn(name = "player_id")
    val player: Player,
) {

    fun toDTO(): MoveDTO = MoveDTO(moveNumber, x, y, player.id)

}
