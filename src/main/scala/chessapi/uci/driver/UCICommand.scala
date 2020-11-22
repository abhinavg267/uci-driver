package chessapi.uci.driver

sealed trait UCICommand {
  def cmd = s"$asString\n"
  def asString: String
  def stopAt: String
}

object UCICommand {
  case object UCI extends UCICommand {
    override def asString: String = "uci"
    override def stopAt: String = "uciok"
  }

  case object IsReady extends UCICommand {
    override def asString: String = "isready"
    override def stopAt: String = "readyok"
  }

  case object NewGame extends UCICommand {
    override def asString: String = "ucinewgame"
    override def stopAt: String = ???
  }
}
