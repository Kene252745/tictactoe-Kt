package pl.pwr.tictactoe.model

import javax.persistence.*

@Entity
@Table(name = "players")
data class Player(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_id_generator")
    @SequenceGenerator(name = "player_id_generator", sequenceName = "player_id_seq")
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "sub")
    val sub: String,
)
