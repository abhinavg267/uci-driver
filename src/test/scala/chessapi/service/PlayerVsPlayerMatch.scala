package chessapi.service

import chessapi.model.{Move, Position}
import chessapi.util.PossibleMoves

import scala.util.{Failure, Success, Try}

object PlayerVsPlayerMatch extends App {
  val chessAPIService = new ChessAPIServiceImpl
  var currentState = chessAPIService.getNewGame

  def positionQuery = {
    println(s"Enter a position to fetch possible moves:\n")
    val selectedPositionStr: String = scala.io.StdIn.readLine()
    println(s"Possible moves are: ${PossibleMoves.getPossibleMovesForPosition(currentState, Position(selectedPositionStr))}")
  }

  def makeMove = {
    println(s"Provide a move now:\n")
    val moveStr: String = scala.io.StdIn.readLine()
    currentState = chessAPIService.move(currentState, Move.fromString(moveStr))
  }

  def printCurrentSate = {
    println(s"Current State:\n${currentState.board.asString} ")
    println(s"Now, it's ${currentState.turn}'s turn")
  }

  while(true) {
    Try {
      println(s"Enter 1 to fetch current state, 2 to query possible positions and 3 to make a move")
      val selection = scala.io.StdIn.readInt()
      selection match {
        case 1 => printCurrentSate
        case 2 => positionQuery
        case 3 => makeMove
        case _ => println(s"Unknown selection, please try again!")
      }
    } match {
      case Failure(exception) =>
        exception.printStackTrace()
        println(s"Error:Try again")
      case Success(_) => ()
    }
  }
}
