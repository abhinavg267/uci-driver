package chessapi.model

sealed trait Move

object Move {
  // TODO: moveStr from pgn notation, currently we have defined custom notation,
  //  ie 1. e2e4 => first 2 char represents startPos, last 2 char represent endPost
  //     2. e7e8Q => promoting pawn in e7 to Queen in e8
  def fromString(moveStr: String): Move = {
    moveStr.length match {
      case 4 =>
        val startPosStr = moveStr.substring(0, 2)
        val targetPosStr = moveStr.substring(2, 4)
        Advance(startPos = Position.fromString(startPosStr), targetPos = Position.fromString(targetPosStr))
      case 5 =>
        val startPosStr = moveStr.substring(0, 2)
        val targetPosStr = moveStr.substring(2, 4)
        val pieceTypeStr = moveStr.substring(4, 5)
        Promotion(startPos = Position.fromString(startPosStr), targetPos = Position.fromString(targetPosStr),
          pieceType = PieceType.fromString(pieceTypeStr))
      case _ => throw MoveParsingError(moveStr)
    }
  }

  // Advance, En passant,
  case class Advance(startPos: Position, targetPos: Position) extends Move
  // Promotion
  case class Promotion(startPos: Position, targetPos: Position, pieceType: PieceType) extends Move

  // TODO: Castling, we don't allow castling now
//  sealed trait Castle extends Move
//  case object ShortCastle extends Castle
//  case object LongCastle extends Castle

  /** Exceptions */
  case class MoveParsingError(move: String) extends Exception(s"Cannot parse move: $move to ${classOf[Move]}")
}
