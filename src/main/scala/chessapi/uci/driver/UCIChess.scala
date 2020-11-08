package chessapi.uci.driver

import java.io.{BufferedReader, InputStreamReader, OutputStream}

import chessapi.uci.driver.util.{Logger, SafeProcessInteraction}

class UCIChess(engine: Process, responseReader: BufferedReader, commandStream: OutputStream) {

  def sendUCICommand(uciCommand: UCICommand): String = {
    uciCommand match {
      case UCICommand.SwitchToUCI =>
        sendUCICommand(uciCommand.cmd)
        readResponse(uciCommand.stopAt)
    }
  }

  def sendUCICommand(cmd: String): Unit = SafeProcessInteraction {
    commandStream.write(s"$cmd\n".getBytes())
    commandStream.flush()
  }(identity)

  def readResponse(stopAt: String, trace: Boolean = false): String = SafeProcessInteraction {
    val line =  responseReader.readLine()
    if(trace) println(line)
    if(line != null) {
      if(line.startsWith(stopAt)) line
      else readResponse(stopAt)
    } else throw new Exception(s"Illegal response!")
  }(identity)

  def destroy(): Unit = SafeProcessInteraction {
    engine.destroy()
    responseReader.close()
    commandStream.close()
    Logger.info(s"Engine Destroyed successfully!")
  }(identity)
}

object UCIChess {
  def apply(engine: String): UCIChess = SafeProcessInteraction {
    val chessEngine = new ProcessBuilder(engine).start()
    val out = chessEngine.getOutputStream
    val in = new BufferedReader(new InputStreamReader(chessEngine.getInputStream))
    (chessEngine, in, out)
  } {
    case (chessEngine, in, out) =>
      Logger.info("Engine Started successfully!")
      new UCIChess(chessEngine, in, out)
  }
}
