package chessapi.uci.driver

sealed trait UCICommand {
  def cmd = s"$asString\n"
  def asString: String
  def stopAt: String
}

object UCICommand {
  case object SwitchToUCI extends UCICommand {
    override def asString: String = "uci"
    override def stopAt: String = "uciok"
  }
}
