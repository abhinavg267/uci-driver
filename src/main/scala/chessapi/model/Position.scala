package chessapi.model

import chessapi.model.util.{StringCompanion, WithAsString}

sealed trait Axis extends WithAsString {
  def asString: String
}

sealed trait Row extends Axis
object Row extends StringCompanion[Row] {
  case object One extends Row {
    override def asString: String = "1"
  }

  case object Two extends Row {
    override def asString: String = "2"
  }

  case object Three extends Row {
    override def asString: String = "3"
  }

  case object Four extends Row {
    override def asString: String = "4"
  }

  case object Five extends Row {
    override def asString: String = "5"
  }

  case object Six extends Row {
    override def asString: String = "6"
  }

  case object Seven extends Row {
    override def asString: String = "7"
  }

  case object Eight extends Row {
    override def asString: String = "8"
  }

  override def all: Set[Row] = Set(One, Two, Three, Four, Five, Six, Seven, Eight)
}

sealed trait Column extends Axis
object Column extends StringCompanion[Column] {
  case object A extends Column {
    override def asString: String = "a"
  }

  case object B extends Column {
    override def asString: String = "b"
  }

  case object C extends Column {
    override def asString: String = "c"
  }

  case object D extends Column {
    override def asString: String = "d"
  }

  case object E extends Column {
    override def asString: String = "e"
  }

  case object F extends Column {
    override def asString: String = "f"
  }

  case object G extends Column {
    override def asString: String = "g"
  }

  case object H extends Column {
    override def asString: String = "h"
  }

  override def all: Set[Column] = Set(A, B, C, D, E, F, G, H)
}

case class Position(row: Row, column: Column) {
  def asString: String = s"${row.asString}${column.asString}"

  def x: Int = row match {
    case Row.One => 7
    case Row.Two => 6
    case Row.Three => 5
    case Row.Four => 4
    case Row.Five => 3
    case Row.Six => 2
    case Row.Seven => 1
    case Row.Eight => 0
  }

  def y: Int = column match {
    case Column.A => 0
    case Column.B => 1
    case Column.C => 2
    case Column.D => 3
    case Column.E => 4
    case Column.F => 5
    case Column.G => 6
    case Column.H => 7
  }
}

object Position {
  def fromString(positionStr: String): Position = {
    positionStr.length match {
      case 2 =>
        val colStr = positionStr.substring(0, 1)
        val rowStr = positionStr.substring(1, 2)

        Position(Row.fromString(rowStr, "Row"), Column.fromString(colStr, "Col"))
      case _ => throw new Exception(s"Cannot parse string $positionStr to ${classOf[Position]}")
    }
  }
}
