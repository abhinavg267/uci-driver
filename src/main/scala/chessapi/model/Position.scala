package chessapi.model

import chessapi.util.{StringCompanion, WithAsString}

sealed trait Axis extends WithAsString {
  def asString: String
  def index: Int
}

sealed trait Row extends Axis
object Row extends StringCompanion[Row] {
  case object One extends Row {
    override def asString: String = "1"
    override def index: Int = 7
  }

  case object Two extends Row {
    override def asString: String = "2"
    override def index: Int = 6
  }

  case object Three extends Row {
    override def asString: String = "3"
    override def index: Int = 5
  }

  case object Four extends Row {
    override def asString: String = "4"
    override def index: Int = 4
  }

  case object Five extends Row {
    override def asString: String = "5"
    override def index: Int = 3
  }

  case object Six extends Row {
    override def asString: String = "6"
    override def index: Int = 2
  }

  case object Seven extends Row {
    override def asString: String = "7"
    override def index: Int = 1
  }

  case object Eight extends Row {
    override def asString: String = "8"
    override def index: Int = 0
  }

  override def all: Set[Row] = Set(One, Two, Three, Four, Five, Six, Seven, Eight)
}

sealed trait Column extends Axis
object Column extends StringCompanion[Column] {
  case object A extends Column {
    override def asString: String = "a"
    override def index: Int = 0
  }

  case object B extends Column {
    override def asString: String = "b"
    override def index: Int = 1
  }

  case object C extends Column {
    override def asString: String = "c"
    override def index: Int = 2
  }

  case object D extends Column {
    override def asString: String = "d"
    override def index: Int = 3
  }

  case object E extends Column {
    override def asString: String = "e"
    override def index: Int = 4
  }

  case object F extends Column {
    override def asString: String = "f"
    override def index: Int = 5
  }

  case object G extends Column {
    override def asString: String = "g"
    override def index: Int = 6
  }

  case object H extends Column {
    override def asString: String = "h"
    override def index: Int = 7
  }

  override def all: Set[Column] = Set(A, B, C, D, E, F, G, H)
}

case class Position(row: Row, column: Column) {
  override def toString: String = asString
  def asString: String = s"${column.asString}${row.asString}"
  def getUpdatedPosition(dr: Int, dc: Int): Option[Position] =
    Position.fromIndexOpt(row.index + dr, column.index + dc)
}

object Position {
  def fromString(positionStr: String): Position = {
    positionStr.length match {
      case 2 =>
        val colStr = positionStr.substring(0, 1)
        val rowStr = positionStr.substring(1, 2)

        Position(Row.fromString(rowStr), Column.fromString(colStr))
      case _ => throw new Exception(s"Cannot parse string $positionStr to ${classOf[Position]}")
    }
  }

  private def fromIndexOpt(rowIndex: Int, columnIndex: Int): Option[Position] = {
    for {
      row <- Row.all.collectFirst { case row if row.index == rowIndex => row }
      column <- Column.all.collectFirst { case column if column.index == columnIndex => column }
    } yield Position(row, column)
  }
}
