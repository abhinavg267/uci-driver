package chessapi.service

import chessapi.model.Move
import chessapi.uci.driver.UCIProcedure.{getBestMove, setPosition, switchToUCI, isReady, startANewGame}
import chessapi.uci.driver.{UCIEngine, UCIResponse}

object ChessBoardApp extends App {
  val chessAPIService = new ChessAPIServiceImpl
  var currentState = chessAPIService.getNewGame

  val engine = UCIEngine(s"/usr/local/bin/stockfish")
  var moves = List.empty[Move]
  val initialPos: Option[String] = None

  (switchToUCI -> isReady -> startANewGame).execute(engine)

  while(moves.length < 50) {
    // Current BoardState
    println(currentState.board.asString)

    val bestMove = getBestMove(10).execute(engine) match {
      case _: UCIResponse.UCIOkResponse | _: UCIResponse.ReadyOkResponse => throw new Exception(s"")
      case UCIResponse.BestMoveResponse(bestMove, ponder) => bestMove
    }

    // Update states
    println(s"Playing the best Move: $bestMove")
    moves = moves :+ bestMove
    currentState = chessAPIService.move(currentState, bestMove)
    setPosition(None, moves).execute(engine)

    // Sleep for some time
    Thread.sleep(100)
  }

  println(s"All the moves played: ${moves.mkString(", ")}")
  println(s"Final State:\n ${currentState.board.asString}")
  Thread.sleep(1000)
  engine.destroy()
}
