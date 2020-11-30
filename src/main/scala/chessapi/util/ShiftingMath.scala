package chessapi.util

import chessapi.model.PieceType._
import chessapi.model.Row._
import chessapi.model.Side.{Black, White}
import chessapi.model.{Piece, Position}
import chessapi.util.CommonImplicitExtensions._

object ShiftingMath {
  case class Shift(dr: Int, dc: Int)

  def getPossibleShift(currentPosition: Position, piece: Piece): List[Shift] = {
    def isShiftInBound(shift: Shift) = currentPosition.update(shift).isDefined

    val f = Range.inclusive(1, 8, 1)
    val b = Range.inclusive(-1, -8, -1)

    val shifts: List[Shift] = piece.pieceType match {
      case King => (List(1, -1, 0) * List(1, -1, 0)).diff(List(0, 0)).map(Shift.tupled)
      case Knight => ((List(1, -1) * List(2, -2)) ++ (List(2, -2) * List(1, -1))).map(Shift.tupled)
      case Queen =>
        getPossibleShift(currentPosition, Piece(piece.side, Bishop)) ++
          getPossibleShift(currentPosition, Piece(piece.side, Rook))
      case Bishop => (f.zip(f) ++ f.zip(b) ++ b.zip(f) ++ b.zip(b)).map(Shift.tupled).toList
      case Rook => (f.map((_, 0)) ++ f.map((0, _)) ++ b.map((_, 0)) ++ b.map((0, _))).map(Shift.tupled).toList
      case Pawn => {
        val forwardWhite = List((1, 0), (1, 1), (1, -1))
        val forwardBlack = List((-1, 0), (-1, 1), (-1, -1))
        (piece.side, currentPosition.row) match {
          case (_, One) | (_, Eight) => throw new Exception
          case (White, Two) => List((2, 0)) ++ forwardWhite
          case (Black, Seven) => List((-2, 0)) ++ forwardBlack
          case (White, Three) | (White, Four) | (White, Five) | (White, Six) | (White, Seven) => forwardWhite
          case (Black, Two) | (Black, Three) | (Black, Four) | (Black, Five) | (Black, Six) => forwardBlack
        }
      }.map(Shift.tupled)
    }

    shifts.filter(isShiftInBound)
  }
}
