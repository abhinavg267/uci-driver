package chessapi.util

/** Logs to console
 * */
object Logger {
  def info(message: String): Unit = println(s"[INFO] $message")
  def error(message: String): Unit = println(s"[ERROR] $message")
  def debug(message: String): Unit = println(s"[DEBUG] $message")
}
