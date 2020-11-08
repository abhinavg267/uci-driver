package chessapi

import chessapi.model.{ChessBoardState, Move, Side}

/**
 * @param state: list of moves to define a chessBoard state
 * */
class ChessBoard(state: ChessBoardState) {
  def move(move: Move): ChessBoard = new ChessBoard(state.update(move))
  def getState: String = state.asString
  def getTurn: Side = state.turn
}

object ChessBoard {
  def apply(moves: List[Move]): ChessBoard = {
    val chessBoard = new ChessBoard(ChessBoardState.initialState)
    moves.foldRight(chessBoard)((move, chessBoard) => chessBoard.move(move))
  }
}

