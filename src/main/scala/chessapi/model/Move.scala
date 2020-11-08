package chessapi.model

sealed trait Move

object Move {
  case class March(startPos: Position, targetPos: Position) extends Move
  case class Promotion(startRow: Row, pieceType: PieceType) extends Move
}
