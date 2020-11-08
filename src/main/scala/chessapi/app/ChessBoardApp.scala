package chessapi.app

import chessapi.ChessBoard
import chessapi.model.Move.March
import chessapi.model.{Column, Position, Row}

object ChessBoardApp extends App {
  val chessBoard = ChessBoard(List.empty)
  println(chessBoard.move(March(startPos = Position(row = Row.Two, column = Column.B),
    targetPos = Position(row = Row.Four, column = Column.B))).getState)
}
