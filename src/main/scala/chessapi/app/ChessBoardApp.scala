package chessapi.app

import chessapi.ChessBoard
import chessapi.model.Move

object ChessBoardApp extends App {
  var chessBoard = ChessBoard(List.empty)
  while (true) {
    println(chessBoard.getState)
    println(s"Enter next move for ${chessBoard.getTurn}:  ")
    val moveStr = scala.io.StdIn.readLine()
    chessBoard = chessBoard.move(Move.fromString(moveStr))
  }
}
