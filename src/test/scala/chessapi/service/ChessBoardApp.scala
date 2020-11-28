package chessapi.service

import chessapi.model.Move

object ChessBoardApp extends App {
  val chessAPIService = new ChessAPIServiceImpl
  var currentState = chessAPIService.getNewGame

  while (true) {
    println(currentState.board.asString)
    println(s"Enter next move for ${currentState.turn}:  ")
    val moveStr = scala.io.StdIn.readLine()
    currentState = chessAPIService.move(currentState, Move.fromString(moveStr))
  }
}
