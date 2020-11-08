package app

import uci.driver.UCIChess

object UCIChessApp extends App {
  val engine = UCIChess(s"/usr/local/bin/stockfish")
  engine.sendUCICommand("uci")
  engine.readResponse("uciok")
  engine.sendUCICommand("isready")
  engine.readResponse("readyok")
  engine.sendUCICommand("position startpos moves e2e4")
  engine.sendUCICommand("isready")
  engine.readResponse("readyok")
  engine.sendUCICommand("go depth 22")
  engine.readResponse("bestmove")
  engine.destroy()
}
