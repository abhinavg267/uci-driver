package chessapi.uci.driver

import chessapi.uci.driver.UCIProcedure.UCIProcedureImpl

sealed trait UCIProcedure[LHT] {
  def execute(uciEngine: UCIEngine): LHT

  final def ->[RHT](uciProcedure: UCIProcedure[RHT]): UCIProcedure[RHT] = {
    def procedure: UCIEngine => RHT = {
      engine =>
        execute(engine)
        uciProcedure.execute(engine)
    }
    UCIProcedureImpl(procedure)
  }
}

object UCIProcedure {
  case class UCIProcedureImpl[T](procedure: UCIEngine => T) extends UCIProcedure[T] {
    override def execute(uciEngine: UCIEngine): T = procedure(uciEngine)
  }

  case class SendUCICommand(command: UCICommand) extends UCIProcedure[Unit] {
    override def execute(uciEngine: UCIEngine): Unit = uciEngine.sendUCICommand(command.cmd)
  }

  case class ReadUCIResponse(response: UCIResponse) extends UCIProcedure[String] {
    override def execute(uciEngine: UCIEngine): String = uciEngine.readResponse(response.asString, trace = true)
  }

  def switchToUCI(uciEngine: UCIEngine) = {
    (SendUCICommand(UCICommand.UCI) -> ReadUCIResponse(UCIResponse.UCIOk)).execute(uciEngine)

  }
  val isReady: UCIProcedure[String] = SendUCICommand(UCICommand.IsReady) -> ReadUCIResponse(UCIResponse.ReadyOk)
  val startANewGame: UCIProcedure[String] = SendUCICommand(UCICommand.NewGame) -> isReady
}
