package chessapi

import chessapi.model.Move.{March, Promotion}
import chessapi.model.{ChessBoardState, Move, Piece}

/**
 * @param state: list of moves to define a chessBoard state
 * */
class ChessBoard(state: ChessBoardState) {
  def move(move: Move): ChessBoard  = move match {
    case March(startPos, targetPos) => {
      new ChessBoard(state)
    }
    case Promotion(startRow, pieceType) => {
      throw new Exception(s"Not supported yet")
    }
  }

  def getState: String = state.asString
}

object ChessBoard {
  def apply(moves: List[Move]): ChessBoard = {
    val chessBoard = new ChessBoard(ChessBoardState.initialState)
    moves.foldRight(chessBoard)((move, chessBoard) => chessBoard.move(move))
  }
}

