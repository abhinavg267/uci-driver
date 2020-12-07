package chessapi.model

import chessapi.model.BoardState.Board
import chessapi.model.Move.Castle.{LongCastle, ShortCastle}
import chessapi.model.Move.{Advance, Castle, Promotion}
import chessapi.model.PieceType.{King, Rook}
import chessapi.model.Side.{Black, White}
import chessapi.util.CommonImplicitExtensions._

// TODO: BoardState should not have private constructor,
//  it may need to be called from fen service in near future,
//  also move update method to ChessAPIService,
//  you may expose this method in future, but I don't have any foreseeable use-case for this

case class CastleState(castleState: String) {
  def canCastle(side: Side, castle: Castle): Boolean = (side, castle) match {
    case (White, ShortCastle) => castleState.charAt(0) == '1'
    case (White, LongCastle) => castleState.charAt(1) == '1'
    case (Black, ShortCastle) => castleState.charAt(2) == '1'
    case (Black, LongCastle) => castleState.charAt(3) == '1'
  }

  def disableCastle(side: Side, castle: Castle): CastleState = {
    val newCastleState = (side, castle) match {
      case (White, ShortCastle) => castleState.replaceCharAt(i = 0, c = '0')
      case (White, LongCastle) => castleState.replaceCharAt(i = 1, c = '0')
      case (Black, ShortCastle) => castleState.replaceCharAt(i = 2, c = '0')
      case (Black, LongCastle) => castleState.replaceCharAt(i = 3, c = '0')
    }

    CastleState(newCastleState)
  }
}

sealed trait BoardState {
  def board: Board
  def turn: Side
  def castleState: CastleState
  def getNewState(move: Move): BoardState
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

  private val initialCastleState = CastleState("1111")

  val initialState: BoardState = BoardState(initialStateStr, turn = initialTurn, initialCastleState)

  private case class BoardStateImpl(board: Board, turn: Side, castleState: CastleState) extends BoardState {
    override def getNewState(move: Move): BoardState = {
      move match {
        case Advance(startPos, targetPos) =>
          val piece = board(startPos).getOrElse(
            throw new Exception(s"Cannot move since initial position is empty"))
          val newCastleState = {
            startPos.asString match {
              case "a1" => castleState.disableCastle(White, LongCastle)
              case "e1" => castleState.disableCastle(White, LongCastle).disableCastle(White, ShortCastle)
              case "h1" => castleState.disableCastle(White, ShortCastle)
              case "a8" => castleState.disableCastle(Black, LongCastle)
              case "e8" => castleState.disableCastle(Black, LongCastle).disableCastle(Black, ShortCastle)
              case "h8" => castleState.disableCastle(Black, ShortCastle)
              case _ => castleState
            }
          }
          updateBoardState(startPos, targetPos, piece, newCastleState)

        case Promotion(startPos, targetPos, pieceType) =>
          updateBoardState(startPos, targetPos, Piece(turn, pieceType), castleState)

        case castle: Castle =>
          def moveForCastle(startPos: String, targetPosition: String, pieceType: PieceType): BoardStateImpl = {
            val existingPiece = board(Position(startPos)).getOrElse(throw new Exception(s"Cannot move since " +
              s"initial position is empty"))
            assert(existingPiece == Piece(turn, pieceType))
            updateBoardState(Position(startPos), Position(targetPosition), existingPiece,
              castleState.disableCastle(turn, castle))
          }

          castle match {
            case ShortCastle =>
              turn match {
                case Side.White =>
                  moveForCastle("e1", "g1", King) // Move king
                  moveForCastle("h1", "f1", Rook) // Move Rook

                case Side.Black =>
                  moveForCastle("e8", "g8", King) // Move king
                  moveForCastle("h8", "f8", Rook) // Move Rook
              }
            case LongCastle =>
              turn match {
                case Side.White =>
                  moveForCastle("e1", "c1", King) // Move king
                  moveForCastle("a1", "d1", Rook) // Move Rook

                case Side.Black =>
                  moveForCastle("e8", "c8", King) // Move king
                  moveForCastle("a8", "d8", Rook) // Move Rook
              }
          }
      }
    }

    private def updateBoardState(startPos: Position, targetPos: Position, piece: Piece, newCastleState: CastleState): BoardStateImpl = {
      board.update(startPos, None)
      board.update(targetPos, Some(piece))
      BoardStateImpl(board, turn.next, newCastleState)
    }
  }

  private def apply(state: String, turn: Side, castleState: CastleState): BoardState = {
    val elements = state.split("\n").map { row =>
      row.split(",")
    }

    assert(elements.length == 8, s"Total number of rows in board state should be 8, found ${elements.length}")
    elements.foreach(row => assert(row.length == 8, s"Total number of element in Row $row should be 8, found ${row.length}"))

    val stateParsed = elements.map(row => row.map(piece => Piece.fromStringOpt(piece.trim)))

    BoardStateImpl(Board(stateParsed), turn, castleState)
  }
}
