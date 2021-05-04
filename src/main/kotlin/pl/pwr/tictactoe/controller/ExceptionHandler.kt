package pl.pwr.tictactoe.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import pl.pwr.tictactoe.exceptions.*

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(GameFinishedException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun gameFinishedExceptionHandler(e: GameFinishedException) {

    }

    @ExceptionHandler(IllegalMoveException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun illegalMoveExceptionHandler(e: IllegalMoveException) {

    }

    @ExceptionHandler(IllegalPlayerException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun illegalPlayerExceptionHandler(e: IllegalPlayerException) {

    }

    @ExceptionHandler(NoSuchGameException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun noSuchGameExceptionHandler(e: NoSuchGameException) {

    }

    @ExceptionHandler(NoSuchPlayerException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun noSuchPlayerExceptionHandler(e: NoSuchPlayerException) {

    }

    @ExceptionHandler(PlayerAlreadyExistsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun playerAlreadyExistsExceptionHandler(e: PlayerAlreadyExistsException) {

    }

}
