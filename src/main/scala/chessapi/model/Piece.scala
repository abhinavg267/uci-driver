package chessapi.model

import chessapi.model.Side.{Black, White}
import chessapi.model.util.{StringCompanion, WithAsString}

sealed trait Side extends WithAsString {
  def next: Side = this match {
    case Side.White => Black
    case Side.Black => White
  }
}
object Side extends StringCompanion[Side] {
  case object White extends Side {
    override def asString: String = "w"
  }
  case object Black extends Side {
    override def asString: String = "b"
  }

  override def all: Set[Side] = Set(White, Black)
}

sealed trait PieceType extends WithAsString {
  def asString: String
}
object PieceType extends StringCompanion[PieceType] {
  // B, K, N, P, Q and R
  case object King extends PieceType {
    override def asString: String = "K"
  }

  case object Queen extends PieceType {
    override def asString: String = "Q"
  }

  case object Bishop extends PieceType {
    override def asString: String = "B"
  }

  case object Knight extends PieceType {
    override def asString: String = "N"
  }

  case object Rook extends PieceType {
    override def asString: String = "R"
  }

  case object Pawn extends PieceType {
    override def asString: String = "P"
  }

  override def all: Set[PieceType] = Set(King, Queen, Bishop, Knight, Rook, Pawn)
}

case class Piece(side: Side, pieceType: PieceType) {
  def asString: String = s"${side.asString}${pieceType.asString}"
  override def toString: String = asString
}

object Piece {
  def fromStringOpt(str: String): Option[Piece] = {
    str.length match {
      case 1 if str == "_" => None
      case 2 =>
        val sideStr: String = str.substring(0, 1)
        val pieceTypeStr = str.substring(1, 2)

        val side: Side = Side.fromString(sideStr, "Side")
        val pieceType: PieceType = PieceType.fromString(pieceTypeStr, "Piece")

        Some(Piece(side, pieceType))
      case _ => throw new Exception(s"Cannot parse string: $str to ${classOf[Piece]}")
    }
  }
}
