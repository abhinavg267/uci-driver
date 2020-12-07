package chessapi.model

import chessapi.model.Move.Castle.{LongCastle, ShortCastle}

sealed trait Move

object Move {
  private val shortCastleMoves = "O-O"
  private val longCastleMoves = "O-O-O"

  def fromString(moveStr: String): Move = {
    if(moveStr == shortCastleMoves) {
      ShortCastle
    } else if (moveStr == longCastleMoves) {
      LongCastle
    } else {
      moveStr.length match {
        case 4 =>
          val startPosStr = moveStr.substring(0, 2)
          val targetPosStr = moveStr.substring(2, 4)
          Advance(startPos = Position(startPosStr), targetPos = Position(targetPosStr))
        case 5 =>
          val startPosStr = moveStr.substring(0, 2)
          val targetPosStr = moveStr.substring(2, 4)
          val pieceTypeStr = moveStr.substring(4, 5)
          Promotion(startPos = Position(startPosStr), targetPos = Position(targetPosStr),
            pieceType = PieceType.fromString(pieceTypeStr))
        case _ => throw MoveParsingError(moveStr)
      }
    }
  }

  // Advance, En passant
  case class Advance(startPos: Position, targetPos: Position) extends Move
  // Promotion
  case class Promotion(startPos: Position, targetPos: Position, pieceType: PieceType) extends Move
  // Castle
  sealed trait Castle extends Move {
    def kingsMove(side: Side): Move
  }

  object Castle {
    case object ShortCastle extends Castle {
      def kingsMove(side: Side): Move = {
        side match {
          case Side.White => Move.fromString(s"e1g1")
          case Side.Black => Move.fromString("e8g8")
        }
      }
    }

    case object LongCastle extends Castle {
      def kingsMove(side: Side): Move = {
        side match {
          case Side.White => Move.fromString(s"e1c1")
          case Side.Black => Move.fromString("a8d8")
        }
      }
    }

    val all: Set[Castle] = Set(ShortCastle, LongCastle)
  }

  /** Exceptions */
  case class MoveParsingError(move: String) extends Exception(s"Cannot parse move: $move to ${classOf[Move]}")
}
