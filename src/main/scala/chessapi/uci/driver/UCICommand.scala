package chessapi.uci.driver

import chessapi.model.Move

sealed trait UCICommand {
  def cmd = s"$asString\n"
  def asString: String
}

object UCICommand {
  case object UCI extends UCICommand {
    override def asString: String = "uci"
  }

  case object IsReady extends UCICommand {
    override def asString: String = "isready"
  }

  case object NewGame extends UCICommand {
    override def asString: String = "ucinewgame"
  }

  /**
   * @param initialPosition: FEN string for initial position
   * */
  case class SetPosition(initialPosition: Option[String], moves: List[Move]) extends UCICommand {
    override def asString: String =
      s"position ${initialPosition.getOrElse("startpos")} moves ${moves.map(convertMoveToEngineReadableString).mkString(" ")}"
  }

  case class Calculate(depth: Int) extends UCICommand {
    override def asString: String = s"go depth $depth"
  }

  case object Stop extends UCICommand {
    override def asString: String = "stop"
  }

  /** Private Methods */
  def convertMoveToEngineReadableString(move: Move): String = {
    move match {
      case Move.Advance(startPos, targetPos) => s"$startPos$targetPos"
      case Move.Promotion(startPos, targetPos, pieceType) => s"$startPos$targetPos${pieceType.asString.toLowerCase}"
    }
  }
}
