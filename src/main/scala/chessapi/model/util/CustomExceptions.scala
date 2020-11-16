package chessapi.model.util

object CustomExceptions {
  case class StringParsingError(string: String, className: String) extends Exception(
    s"Cannot parse string: $string to class: $className")
}
