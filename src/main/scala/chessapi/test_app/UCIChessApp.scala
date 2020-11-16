package chessapi.test_app

import chessapi.uci.driver.UCIChess

object UCIChessApp extends App {
  val engine = UCIChess(s"/usr/local/bin/stockfish")
  engine.sendUCICommand("uci")
  println(engine.readResponse("uciok"))
  engine.sendUCICommand("isready")
  println(engine.readResponse("readyok"))
  engine.sendUCICommand("position startpos moves e2e4")
  engine.sendUCICommand("isready")
  println(engine.readResponse("readyok"))
  engine.sendUCICommand("go depth 22")
  println(engine.readResponse("bestmove"))
  engine.destroy()
}
