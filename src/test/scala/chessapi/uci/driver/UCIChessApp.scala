package chessapi.uci.driver

object UCIChessApp extends App {
  val engine = UCIEngine(s"/usr/local/bin/stockfish")
  (UCIProcedure.switchToUCI ->
    UCIProcedure.isReady ->
    UCIProcedure.startANewGame).execute(engine)

//  engine.sendUCICommand("uci")
//  engine.readResponse("uciok", trace = true)
//  engine.sendUCICommand("isready")
//  println(engine.readResponse("readyok"))
//  engine.sendUCICommand("position startpos moves e2e4")
//  engine.sendUCICommand("isready")
//  println(engine.readResponse("readyok"))
//  engine.sendUCICommand("go depth 22")
//  println(engine.readResponse("bestmove"))
  engine.destroy()
}
