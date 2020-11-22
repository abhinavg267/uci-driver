package chessapi.uci.driver

import chessapi.uci.driver.UCIProcedure.UCIProcedureImpl

sealed trait UCIProcedure {
  def execute(uciEngine: UCIEngine): Any
  def ->(uciProcedure: UCIProcedure): UCIProcedure = {
    def procedure: UCIEngine => Any = {
      engine =>
        execute(engine)
        uciProcedure.execute(engine)
    }
    UCIProcedureImpl(procedure)
  }
}

object UCIProcedure {
  case class UCIProcedureImpl(procedure: UCIEngine => Any) extends UCIProcedure {
    override def execute(uciEngine: UCIEngine): Any = procedure(uciEngine)
  }

  case class SendUCICommand(command: UCICommand) extends UCIProcedure {
    override def execute(uciEngine: UCIEngine): Unit = uciEngine.sendUCICommand(command.cmd)
  }

  case class ReadUCIResponse(response: UCIResponse) extends UCIProcedure {
    override def execute(uciEngine: UCIEngine): String = uciEngine.readResponse("response.asString", trace = true)
  }

  val switchToUCI: UCIProcedure = SendUCICommand(UCICommand.UCI) -> ReadUCIResponse(UCIResponse.UCIOk)
}
