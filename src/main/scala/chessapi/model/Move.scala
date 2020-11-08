package chessapi.model

sealed trait Move

object Move {
  def fromString(moveStr: String): Move = {
    moveStr.length match {
      case 4 =>
        val startPosStr = moveStr.substring(0, 2)
        val targetPosStr = moveStr.substring(2, 4)
        March(startPos = Position.fromString(startPosStr), targetPos = Position.fromString(targetPosStr))
      case 5 =>
        val startPosStr = moveStr.substring(0, 2)
        val targetPosStr = moveStr.substring(2, 4)
        val pieceTypeStr = moveStr.substring(4, 5)
        Promotion(startPos = Position.fromString(startPosStr), targetPos = Position.fromString(targetPosStr),
          pieceType = PieceType.fromString(pieceTypeStr, "Piece"))
      case _ => throw new Exception(s"Cannot parse move: $moveStr to ${classOf[Move]}")
    }
  }

  case class March(startPos: Position, targetPos: Position) extends Move
  case class Promotion(startPos: Position, targetPos: Position, pieceType: PieceType) extends Move
}
