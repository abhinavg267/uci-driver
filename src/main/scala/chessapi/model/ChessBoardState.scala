package chessapi.model

import chessapi.model.Move.{March, Promotion}
import chessapi.model.Side.White

sealed trait ChessBoardState {
  def state: Array[Array[Option[Piece]]]
  def turn: Side
  def asString: String = {
    state.map { row =>
      row.map {
        case Some(piece) => String.format("%4s", piece.toString)
        case None => String.format("%4s", "_")
      }.mkString(",")
    }.mkString("\n")
  }
  def update(move: Move): ChessBoardState
}

object ChessBoardState {
  val initialStateStr: String =
    """ bR, bN, bB, bQ, bK, bB, bN, bR
      | bP, bP, bP, bP, bP, bP, bP, bP
      |  _,  _,  _,  _,  _,  _,  _,  _
      |  _,  _,  _,  _,  _,  _,  _,  _
      |  _,  _,  _,  _,  _,  _,  _,  _
      |  _,  _,  _,  _,  _,  _,  _,  _
      | wP, wP, wP, wP, wP, wP, wP, wP
      | wR, wN, wB, wQ, wK, wB, wN, wR
      |""".stripMargin

  val initialTurn: Side = White

  val initialState: ChessBoardState = ChessBoardState(initialStateStr, turn = initialTurn)

  private case class ChessBoardStateImpl(state: Array[Array[Option[Piece]]], turn: Side) extends ChessBoardState {
    override def update(move: Move): ChessBoardState = {
      move match {
        case March(startPos, targetPos) =>
          val piece = state(startPos.x)(startPos.y).getOrElse(
            throw new Exception(s"Cannot move since initial position is empty"))
          placeThePiece(startPos, targetPos, piece)

        case Promotion(startPos, targetPos, pieceType) =>
          placeThePiece(startPos, targetPos, Piece(turn, pieceType))
      }
    }

    private def placeThePiece(startPos: Position, targetPos: Position, piece: Piece): ChessBoardStateImpl = {
      state(startPos.x).update(startPos.y, None)
      state(targetPos.x).update(targetPos.y, Some(piece))
      ChessBoardStateImpl(state, turn.next)
    }
  }

  def apply(state: String, turn: Side): ChessBoardState = {
    val elements = state.split("\n").map { row =>
      row.split(",")
    }

    assert(elements.length == 8, s"Total number of rows in board state should be 8, found ${elements.length}")
    elements.foreach(row => assert(row.length == 8, s"Row $row has length other then 8, ${row.length}"))

    val stateParsed = elements.map(row => row.map(piece => Piece.fromStringOpt(piece.trim)))

    ChessBoardStateImpl(stateParsed, turn)
  }
}
