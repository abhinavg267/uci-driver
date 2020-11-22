package chessapi.service

import chessapi.model.Move.Advance
import chessapi.model.PieceType.Pawn
import chessapi.model.Side.White
import chessapi.model.{BoardState, Move, Piece, Position, Row}
import com.google.inject.{ImplementedBy, Singleton}

@ImplementedBy(classOf[ChessAPIServiceImpl])
trait ChessAPIService {
  def getNewGame: BoardState

  def move(currentState: BoardState, move: Move): BoardState
}

@Singleton
class ChessAPIServiceImpl extends ChessAPIService {
  override def getNewGame: BoardState = BoardState.initialState

  override def move(currentState: BoardState, move: Move): BoardState = currentState.update(move)

//  private def getPossibleMoves(currentState: BoardState, selectedPosition: Position): Seq[Move] = {
//    val pieceOpt = currentState.board(selectedPosition)
//    pieceOpt match {
//      case Some(Piece(side, pieceType)) =>
//        (side, pieceType) match {
//          case (White, Pawn) =>
//            selectedPosition.row match {
//              case Row.Two => Seq.empty[Move] // advance
//              case Row.Seven => Seq.empty[Move] // promote
//              case Row.Three | Row.Four | Row.Five | Row.Six =>
//                // move forward or capture
//                val front = selectedPosition.getUpdatedPosition(dr = 1, dc = 0)
//                val frontLeft = selectedPosition.getUpdatedPosition(dr = 1, dc = 1)
//                val frontRight = selectedPosition.getUpdatedPosition(dr = 1, dc = -1)
//
//                val possiblePositions = (if(currentState.board(front).isEmpty) front else None).toSeq ++
//                  (if(currentState.board(frontRight).isDefined) frontRight else None).toSeq ++
//                  (if(currentState.board(frontLeft).isDefined) frontLeft else None).toSeq
//
//                possiblePositions.map(Advance(selectedPosition, _))
//
//              case Row.One | Row.Eight => throw new Exception(s"Illegal Position")
//            }
//        }
//      case None => Seq.empty[Move]
//    }
//  }
}