package chessapi.util

import chessapi.model.PieceType.{Knight, Queen}
import chessapi.model.Side.White
import chessapi.model.{Piece, Position}


object UtilApp extends App {
  ShiftingMath.getPossibleShift(Position.fromString("d4"), Piece(White, Queen)).foreach(println)
}
