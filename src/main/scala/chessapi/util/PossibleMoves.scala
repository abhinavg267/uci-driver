package chessapi.util

import chessapi.model.Move.Castle.{LongCastle, ShortCastle}
import chessapi.model.Move.{Advance, Castle, Promotion}
import chessapi.model.PieceType._
import chessapi.model.Row._
import chessapi.model.Side.{Black, White}
import chessapi.model.{BoardState, Move, Piece, Position}
import chessapi.util.CommonImplicitExtensions._

object PossibleMoves {
  case class Shift(dr: Int, dc: Int)

  def getAllPossibleMoves(boardState: BoardState): Set[Move] = {
    Position.all.flatMap(getPossibleMovesForPosition(boardState, _))
  }

  // This also includes moves which will expose king to a threat
  def getPossibleMovesForPosition(boardState: BoardState, selectedPosition: Position): List[Move] = {
    // Get All possible moves, advance, kill, promotion, castle
    // filterOut moves where piece of same side exists

    val isEmptyAndInBoard: Shift => Boolean = selectedPosition.getNewPosition(_).fold(false)(boardState.board(_).isEmpty)
    val isNonEmptyAndInBoard: Shift => Boolean = selectedPosition.getNewPosition(_).fold(false)(boardState.board(_).isDefined)

    def getPossibleAdvance(shift: Shift): Option[Advance] = {
      val startPos = selectedPosition
      val targetPosOpt = selectedPosition.getNewPosition(shift)
      targetPosOpt.map(Advance(startPos, _))
    }

    def takeUntilNonEmptyOrOutOfBoard(drdc: Seq[(Int, Int)]): List[Shift] = {
      drdc.map(Shift.tupled).toList.takeUntil(isEmptyAndInBoard)
    }

    val inc: Seq[Int] = Range.inclusive(1, 8, 1)
    val dec: Seq[Int] = Range.inclusive(-1, -8, -1)

    val selectedPieceOpt = boardState.board(selectedPosition)

    val allPossibleMoves: List[Move] = selectedPieceOpt match {
      case Some(Piece(selectedPieceSide, selectedPieceType)) if selectedPieceSide == boardState.turn =>
        (selectedPieceSide, selectedPieceType) match {
          case (_, King) =>
            val advances = (List(1, -1, 0) * List(1, -1, 0)).diff(List((0, 0))).map(Shift.tupled).flatMap(getPossibleAdvance)
            val castles = Castle.all.flatMap { c =>
              if(boardState.castleState.canCastle(selectedPieceSide, c)) Some(c.kingsMove(selectedPieceSide)) else None
            }
            advances ++ castles
          case (_, Queen) =>
            (takeUntilNonEmptyOrOutOfBoard(inc.zip(inc)) ++
              takeUntilNonEmptyOrOutOfBoard(inc.zip(dec)) ++
              takeUntilNonEmptyOrOutOfBoard(dec.zip(inc)) ++
              takeUntilNonEmptyOrOutOfBoard(dec.zip(dec)) ++
              takeUntilNonEmptyOrOutOfBoard(inc.map((_, 0))) ++
              takeUntilNonEmptyOrOutOfBoard(inc.map((0, _))) ++
              takeUntilNonEmptyOrOutOfBoard(dec.map((_, 0))) ++
              takeUntilNonEmptyOrOutOfBoard(dec.map((0, _)))).flatMap(getPossibleAdvance)
          case (_, Knight) => ((List(1, -1) * List(2, -2)) ++ (List(2, -2) * List(1, -1))).map(Shift.tupled).flatMap(getPossibleAdvance)
          case (_, Bishop) =>
            (takeUntilNonEmptyOrOutOfBoard(inc.zip(inc)) ++
              takeUntilNonEmptyOrOutOfBoard(inc.zip(dec)) ++
              takeUntilNonEmptyOrOutOfBoard(dec.zip(inc)) ++
              takeUntilNonEmptyOrOutOfBoard(dec.zip(dec))).flatMap(getPossibleAdvance)
          case (_, Rook) =>
            (takeUntilNonEmptyOrOutOfBoard(inc.map((_, 0))) ++
              takeUntilNonEmptyOrOutOfBoard(inc.map((0, _))) ++
              takeUntilNonEmptyOrOutOfBoard(dec.map((_, 0))) ++
              takeUntilNonEmptyOrOutOfBoard(dec.map((0, _)))).flatMap(getPossibleAdvance)
          case (White, Pawn) =>
            val f = Shift(-1, 0)
            val ff = Shift(-2, 0)
            val fr = Shift(-1, 1)
            val fl = Shift(-1, -1)

            (selectedPosition.row match {
              case One | Eight => throw new Exception(s"$selectedPieceType cannot be in $selectedPosition")
              case Two =>
                Nil ++
                  Some(f).filter(isEmptyAndInBoard) ++
                  Some(ff).filter(_ => isEmptyAndInBoard(ff) && isEmptyAndInBoard(f)) ++
                  Some(fr).filter(isNonEmptyAndInBoard) ++
                  Some(fl).filter(isNonEmptyAndInBoard)

              case Three | Four | Five | Six | Seven =>
                Nil ++
                  Some(f).filter(isEmptyAndInBoard) ++
                  Some(fr).filter(isNonEmptyAndInBoard) ++
                  Some(fl).filter(isNonEmptyAndInBoard)
            }).flatMap(getPossibleAdvance)

          case (Black, Pawn) =>
            val f = Shift(1, 0)
            val ff = Shift(2, 0)
            val fr = Shift(1, 1)
            val fl = Shift(1, -1)

            (selectedPosition.row match {
              case One | Eight => throw new Exception(s"$selectedPieceType cannot be in $selectedPosition")
              case Seven =>
                Nil ++
                  Some(f).filter(isEmptyAndInBoard) ++
                  Some(ff).filter(_ => isEmptyAndInBoard(ff) && isEmptyAndInBoard(f)) ++
                  Some(fr).filter(isNonEmptyAndInBoard) ++
                  Some(fl).filter(isNonEmptyAndInBoard)

              case Three | Four | Five | Six | Two =>
                Nil ++
                  Some(f).filter(isEmptyAndInBoard) ++
                  Some(fr).filter(isNonEmptyAndInBoard) ++
                  Some(fl).filter(isNonEmptyAndInBoard)
            }).flatMap(getPossibleAdvance)
        }
      case Some(_) | None => List.empty[Move]
    }

    allPossibleMoves.filter {
      case Advance(_, targetPos) =>
        (for {
          selectedPiece <- selectedPieceOpt
          pieceOnTargetPosition <- boardState.board(targetPos)
        } yield pieceOnTargetPosition.side != selectedPiece.side).getOrElse(true)
      case Promotion(_, targetPos, _) =>
        (for {
          selectedPiece <- selectedPieceOpt
          pieceOnTargetPosition <- boardState.board(targetPos)
        } yield pieceOnTargetPosition.side != selectedPiece.side).getOrElse(true)

      case c: Castle =>
        def isPositionEmpty(pos: String) = boardState.board(Position(pos)).isEmpty

        (boardState.turn, c) match {
          case (White, ShortCastle) =>
            isPositionEmpty("f1") && isPositionEmpty("g1")
          case (White, LongCastle) =>
            isPositionEmpty("b1") && isPositionEmpty("c1") && isPositionEmpty("d1")
          case (Black, ShortCastle) =>
            isPositionEmpty("f8") && isPositionEmpty("g8")
          case (Black, LongCastle) =>
            isPositionEmpty("b8") && isPositionEmpty("c8") && isPositionEmpty("d8")
        }
    }
  }

  def isKingUnderThreat(boardState: BoardState): Boolean = {
    ???
    // if possible moves of other side has kings position then true
  }
}
