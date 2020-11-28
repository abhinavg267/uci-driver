package chessapi.model

import chessapi.model.BoardState.Board
import chessapi.model.Move.{Advance, Promotion}
import chessapi.model.Side.White

// TODO: BoardState should not have private constructor,
//  it may need to be called from fen service in near future,
//  also move update method to ChessAPIService,
//  you may expose this method in future, but I don't have any foreseeable use-case for this
sealed trait BoardState {
  def board: Board
  def turn: Side
  def update(move: Move): BoardState
}

object BoardState {
  case class Board(boardMatrix: Array[Array[Option[Piece]]]) {

    def apply(position: Position): Option[Piece] = boardMatrix(position.row.index)(position.column.index)

    def apply(positionOpt: Option[Position]): Option[Piece] = positionOpt.flatMap(p => apply(p))

    def update(position: Position, pieceOpt: Option[Piece]): Unit =
      boardMatrix(position.row.index).update(position.column.index, pieceOpt)

    def asString: String = {
      boardMatrix.map { row =>
        row.map {
          case Some(piece) => String.format("%4s", piece.toString)
          case None => String.format("%4s", "_")
        }.mkString(",")
      }.mkString("\n")
    }
  }

  private val initialStateStr: String =
    """ bR, bN, bB, bQ, bK, bB, bN, bR
      | bP, bP, bP, bP, bP, bP, bP, bP
      |  _,  _,  _,  _,  _,  _,  _,  _
      |  _,  _,  _,  _,  _,  _,  _,  _
      |  _,  _,  _,  _,  _,  _,  _,  _
      |  _,  _,  _,  _,  _,  _,  _,  _
      | wP, wP, wP, wP, wP, wP, wP, wP
      | wR, wN, wB, wQ, wK, wB, wN, wR
      |""".stripMargin

  private val initialTurn: Side = White

  val initialState: BoardState = BoardState(initialStateStr, turn = initialTurn)

  private case class BoardStateImpl(board: Board, turn: Side) extends BoardState {
    override def update(move: Move): BoardState = {
      move match {
        case Advance(startPos, targetPos) =>
          val piece = board(startPos).getOrElse(
            throw new Exception(s"Cannot move since initial position is empty"))
          placeThePiece(startPos, targetPos, piece)

        case Promotion(startPos, targetPos, pieceType) =>
          placeThePiece(startPos, targetPos, Piece(turn, pieceType))
      }
    }

    private def placeThePiece(startPos: Position, targetPos: Position, piece: Piece): BoardStateImpl = {
      board.update(startPos, None)
      board.update(targetPos, Some(piece))
      BoardStateImpl(board, turn.next)
    }
  }

  private def apply(state: String, turn: Side): BoardState = {
    val elements = state.split("\n").map { row =>
      row.split(",")
    }

    assert(elements.length == 8, s"Total number of rows in board state should be 8, found ${elements.length}")
    elements.foreach(row => assert(row.length == 8, s"Total number of element in Row $row should be 8, found ${row.length}"))

    val stateParsed = elements.map(row => row.map(piece => Piece.fromStringOpt(piece.trim)))

    BoardStateImpl(Board(stateParsed), turn)
  }
}
