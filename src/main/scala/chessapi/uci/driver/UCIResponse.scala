package chessapi.uci.driver

sealed trait UCIResponse {
  def asString: String
}

object UCIResponse {
  case object UCIOk extends UCIResponse {
    override def asString: String = "uciok"
  }

  case object ReadyOk extends UCIResponse {
    override def asString: String = "readyok"
  }
}
