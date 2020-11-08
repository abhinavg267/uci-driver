package chessapi.model.util

trait WithAsString {
  def asString: String
}

trait StringCompanion[T <: WithAsString] {
  def all: Set[T]
  def fromStringOpt(str: String): Option[T] = all.collectFirst {
    case ele if ele.asString == str => ele
  }

  def fromString(str: String, className: String): T = fromStringOpt(str).getOrElse(
    throw new Exception(s"String $str is not a defined type of $className"))
}