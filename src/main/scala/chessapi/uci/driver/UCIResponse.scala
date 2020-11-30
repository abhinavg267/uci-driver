package chessapi.uci.driver

import chessapi.model.Move
import chessapi.uci.driver.UCIResponseType.{BestMove, ReadyOk, UCIOk}
import chessapi.util.CommonImplicitExtensions._

sealed trait UCIResponseType {
  def startsWith: String
}
object UCIResponseType {
  case object UCIOk extends UCIResponseType {
    override def startsWith: String = "uciok"
  }

  case object ReadyOk extends UCIResponseType {
    override def startsWith: String = "readyok"
  }

  case object BestMove extends UCIResponseType {
    override def startsWith: String = "bestmove"
  }
}

sealed trait UCIResponse {
  def responseType: UCIResponseType
}

object UCIResponse {
  case class UCIOkResponse(isOk: Boolean) extends UCIResponse {
    override def responseType: UCIResponseType = UCIOk
  }

  case class ReadyOkResponse(isReady: Boolean) extends UCIResponse {
    override def responseType: UCIResponseType = ReadyOk
  }

  case class BestMoveResponse(bestMove: Move, ponder: Option[Move]) extends UCIResponse {
    override def responseType: UCIResponseType = BestMove
  }

  def fromString(responseType: UCIResponseType, str: String): UCIResponse = {
    responseType match {
      case UCIOk => UCIOkResponse(str == responseType.startsWith)
      case ReadyOk => ReadyOkResponse(str == responseType.startsWith)
      case BestMove =>
        val res = str.split(" ")
        val bestMove = res.get(1) match {
          case Some(moveStr) => Move.fromString(moveStr)
          case None => throw new Exception(s"Cannot parse string $str to ${classOf[BestMoveResponse]}")
        }
        val ponder = res.get(3).map(Move.fromString)

        BestMoveResponse(bestMove, ponder)
    }
  }
}
