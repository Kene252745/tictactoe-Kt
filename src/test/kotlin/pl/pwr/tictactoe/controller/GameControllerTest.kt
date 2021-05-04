package pl.pwr.tictactoe.controller

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher.matchAll
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import pl.pwr.tictactoe.model.Player
import pl.pwr.tictactoe.model.Winner.NONE
import pl.pwr.tictactoe.service.GameService
import pl.pwr.tictactoe.service.PlayerService

@SpringBootTest
@ExtendWith(SpringExtension::class)
@ActiveProfiles("dev")
@TestInstance(PER_CLASS)
class GameControllerTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var playerService: PlayerService

    @Autowired
    private lateinit var gameService: GameService

    private lateinit var mockMvc: MockMvc

    private val subject: String = "email@example.com"
    private val opponentSubject: String = "opponent@example.com"

    private lateinit var player: Player
    private lateinit var opponent: Player

    @BeforeAll
    fun setupOnce() {
        player = playerService.createAccount(subject)
        opponent = playerService.createAccount(opponentSubject)
    }

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply<DefaultMockMvcBuilder>(springSecurity()).build()
    }

    @Test
    fun shouldGet() {
        val size = 10
        val winningSize = 5
        val game = gameService.save(player.sub, opponent.id, size, winningSize)

        mockMvc.perform(
            get("/game/${game.id}")
                .with(jwt().jwt { it.subject(subject) })
        ).andExpect(
            matchAll(
                status().isOk,
                content().contentType(APPLICATION_JSON),
                jsonPath("$.id").value(game.id),
                jsonPath("$.player1").value(player.id),
                jsonPath("$.player2").value(opponent.id),
                jsonPath("$.size").value(size),
                jsonPath("$.winningNumber").value(winningSize),
            )
        )
    }

    @Test
    fun getWinner() {
        val game = gameService.save(player.sub, opponent.id)

        mockMvc.perform(
            get("/game/${game.id}/winner")
        ).andExpect(
            matchAll(
                status().isOk,
                content().contentType(APPLICATION_JSON),
                jsonPath("$.gameId").value(game.id),
                jsonPath("$.winner").value(NONE.toString()),
            )
        )
    }

    @Test
    fun toCreate() {
        val size = 10
        val winningSize = 5
        val json = """{
            |   "opponentId": ${opponent.id},
            |   "size": $size,
            |   "winningNumber": $winningSize
            |}""".trimMargin()

        mockMvc.perform(
            post("/game")
                .with(jwt().jwt { it.subject(subject) })
                .content(json)
                .contentType(APPLICATION_JSON)
        ).andExpect(
            matchAll(
                status().isCreated,
                content().contentType(APPLICATION_JSON),
                jsonPath("$.player1").value(player.id),
                jsonPath("$.player2").value(opponent.id),
                jsonPath("$.size").value(size),
                jsonPath("$.winningNumber").value(winningSize),
            )
        )
    }

    @Test
    fun shouldSaveMove() {
        val game = gameService.save(player.sub, opponent.id)

        val x = 0
        val y = 0
        val json = """{
            |   "x": $x,
            |   "y": $y
            |}""".trimMargin()

        mockMvc.perform(
            post("/game/${game.id}/move")
                .with(jwt().jwt { it.subject(subject) })
                .content(json)
                .contentType(APPLICATION_JSON)
        ).andExpect(
            matchAll(
                status().isCreated,
                content().contentType(APPLICATION_JSON),
                jsonPath("$.moveNumber").value(0),
                jsonPath("$.x").value(x),
                jsonPath("$.y").value(y),
                jsonPath("$.playerId").value(player.id),
            )
        )
    }

}
