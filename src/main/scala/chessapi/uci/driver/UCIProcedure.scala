package chessapi.uci.driver

import chessapi.model.Move
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
    override def execute(uciEngine: UCIEngine): Unit = uciEngine.sendCommand(command.cmd)
  }

  case class ReadUCIResponse(responseType: UCIResponseType) extends UCIProcedure[UCIResponse] {
    override def execute(uciEngine: UCIEngine): UCIResponse = uciEngine.readResponse(responseType, trace = true)
  }

  val switchToUCI: UCIProcedure[UCIResponse] = SendUCICommand(UCICommand.UCI) -> ReadUCIResponse(UCIResponseType.UCIOk)
  val isReady: UCIProcedure[UCIResponse] = SendUCICommand(UCICommand.IsReady) -> ReadUCIResponse(UCIResponseType.ReadyOk)
  val startANewGame: UCIProcedure[UCIResponse] = SendUCICommand(UCICommand.NewGame) -> isReady
  def setPosition(initialPosition: Option[String], moves: List[Move]): UCIProcedure[Unit] = SendUCICommand(UCICommand.Position(initialPosition, moves))
  def getBestMove(depth: Int): UCIProcedure[UCIResponse] = SendUCICommand(UCICommand.Calculate(depth)) -> ReadUCIResponse(UCIResponseType.BestMove)
}
