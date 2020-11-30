package chessapi.uci.driver

import java.io.{BufferedReader, InputStreamReader, OutputStream}

import chessapi.util.{Logger, SafeProcessInteraction}

class UCIEngine(engine: Process, responseReader: BufferedReader, commandStream: OutputStream) {

  def sendCommand(cmd: String): Unit = SafeProcessInteraction {
    Logger.info(s"Sending command: ${cmd.stripSuffix("\n")}")
    commandStream.write(s"$cmd".getBytes())
    commandStream.flush()
  }(identity)

  def readResponse(responseType: UCIResponseType, trace: Boolean): UCIResponse = SafeProcessInteraction {
    val line = responseReader.readLine()
    if(trace) println(line)
    if(line != null) {
      UCIResponse.UCIOkResponse(true)
      if(line.startsWith(responseType.startsWith)) {
        Logger.info(s"Parsing $responseType response: $line")
        UCIResponse.fromString(responseType, line)
      }
      else readResponse(responseType, trace)
    } else throw new Exception(s"Illegal response!")
  }(identity)

  def destroy(): Unit = SafeProcessInteraction {
    engine.destroy()
    responseReader.close()
    commandStream.close()
    Logger.info(s"Engine Destroyed successfully!")
  }(identity)
}

object UCIEngine {
  def apply(engine: String): UCIEngine = SafeProcessInteraction {
    val chessEngine = new ProcessBuilder(engine).start()
    val out = chessEngine.getOutputStream
    val in = new BufferedReader(new InputStreamReader(chessEngine.getInputStream))
    (chessEngine, in, out)
  } {
    case (chessEngine, in, out) =>
      Logger.info("Engine Started successfully!")
      new UCIEngine(chessEngine, in, out)
  }
}
