package chessapi.uci.driver

import chessapi.uci.driver.UCIProcedure.UCIProcedureImpl

sealed trait UCIProcedure {
  def execute[T](uciEngine: UCIEngine): T
  def ->[T](uciProcedure: UCIProcedure): UCIProcedure = {
    def procedure: UCIEngine => T = {
      engine =>
        execute(engine)
        uciProcedure.execute(engine)
    }
    UCIProcedureImpl(procedure)
  }
}

object UCIProcedure {
  case class UCIProcedureImpl(procedure: UCIEngine => Any) extends UCIProcedure {
    override def execute[T](uciEngine: UCIEngine): T = procedure(uciEngine)
  }

  case class SendUCICommand(command: UCICommand) extends UCIProcedure {
    override def execute(uciEngine: UCIEngine): Unit = uciEngine.sendUCICommand(command.cmd)
  }

  case class ReadUCIResponse(response: UCIResponse) extends UCIProcedure {
    override def execute(uciEngine: UCIEngine): String = uciEngine.readResponse(response.asString, trace = true)
  }

  val switchToUCI: UCIProcedure = SendUCICommand(UCICommand.UCI) -> ReadUCIResponse(UCIResponse.UCIOk)
  def isReady(engine: UCIEngine): String = (SendUCICommand(UCICommand.IsReady) -> ReadUCIResponse(UCIResponse.ReadyOk)).execute(engine)
  val startANewGame: UCIProcedure = SendUCICommand(UCICommand.NewGame) -> isReady
}
