package chessapi.uci.driver

import chessapi.model.Move
import chessapi.uci.driver.UCIProcedure._

object UCIChessApp extends App {
  val engine = UCIEngine(s"/usr/local/bin/stockfish")
  var moves = List.empty[Move]
  val initialPos: Option[String] = None

  (switchToUCI -> isReady -> startANewGame).execute(engine)
  while(moves.length < 10) {
    Thread.sleep(100)
    val bestMove = getBestMove(10).execute(engine) match {
      case _: UCIResponse.UCIOkResponse | _: UCIResponse.ReadyOkResponse => throw new Exception(s"")
      case UCIResponse.BestMoveResponse(bestMove, ponder) => bestMove
    }

    println(s"Playing the best Move: $bestMove")
    moves = moves :+ bestMove

    setPosition(None, moves).execute(engine)
  }

  println(s"All the moves played: ${moves.mkString(", ")}")

  Thread.sleep(100)
  engine.destroy()
}
