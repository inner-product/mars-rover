package mars

import scala.reflect.ClassTag

/** Two-dimensional grid that supports wrap-around indexing. For example, an
  * index of -1 will wrap around to the end of the row or column.
  */
final case class Grid[A](width: Int, height: Int, data: Array[A]) {

  /** Given x and y coordinates calculate the index into the data array */
  private def index(x: Int, y: Int): Int = {
    val (normalizedX, normalizedY) = normalize(x, y)

    normalizedY * width + normalizedX
  }

  /** Normalize coordinates to be within [0, width) and [0, height) */
  def normalize(x: Int, y: Int): (Int, Int) = {
    def computeIndex(idx: Int, max: Int): Int = {
      val normalized = idx % max
      if (normalized < 0) normalized + max else normalized
    }

    val normalizedX = computeIndex(x, width)
    val normalizedY = computeIndex(y, height)
    (normalizedX, normalizedY)
  }

  /** Functionally (copying) update the value at a given location. */
  def set(x: Int, y: Int, newValue: A)(implicit tag: ClassTag[A]): Grid[A] = {
    val newData = data.updated(index(x, y), newValue)
    this.copy(data = newData)
  }

  /** Get the element at the given row and column. */
  def apply(x: Int, y: Int): A = {
    data(index(x, y))
  }
}
object Grid {
  def parse(map: List[String]): Grid[Location] = {
    assert(
      map.map(_.size).distinct.size == 1,
      "The rows in the map must all have the same length."
    )

    val width = map.head.size
    val height = map.size
    val parsed = map.map(string =>
      string.map(char =>
        char match {
          case '*' => Rubble
          case 'a' => UndrilledRock(true)
          case 'r' => UndrilledRock(false)
          case 'x' => DrilledRock
          case ' ' => Sand
          case '.' => Sand
        }
      )
    )

    Grid(width, height, parsed.flatten.toArray)
  }
}
