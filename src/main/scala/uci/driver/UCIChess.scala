package uci.driver

import java.io.{BufferedReader, InputStreamReader, OutputStream}

import uci.driver.util.{Logger, SafeProcessInteraction}

class UCIChess(engine: Process, responseReader: BufferedReader, commandStream: OutputStream) {

  def sendUCICommand(cmd: String): Unit = SafeProcessInteraction {
    commandStream.write(s"$cmd\n".getBytes())
    commandStream.flush()
  }(identity)

  def readResponse(stopAt: String): String = {
    val line =  responseReader.readLine()
    println(line)
    if(line != null) {
      if(line.startsWith(stopAt)) line
      else readResponse(stopAt)
    } else throw new Exception(s"Illegal response!")
  }

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
