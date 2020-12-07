package chessapi.service

import chessapi.model.{BoardState, Move}
import chessapi.util.{Logger, PossibleMoves}
import com.google.inject.{ImplementedBy, Singleton}

@ImplementedBy(classOf[ChessAPIServiceImpl])
trait ChessAPIService {
  def getNewGame: BoardState

  def move(currentState: BoardState, move: Move): BoardState
}

@Singleton
class ChessAPIServiceImpl extends ChessAPIService {
  override def getNewGame: BoardState = BoardState.initialState

  override def move(currentState: BoardState, move: Move): BoardState = {
    if(PossibleMoves.getAllPossibleMoves(currentState).contains(move)) currentState.getNewState(move)
    else {
      Logger.error(s"Cannot execute move: $move, returning existing state back")
      currentState
    }
  }
}