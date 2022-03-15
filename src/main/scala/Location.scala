package mars

/** A location in the grid world of Mars */
sealed abstract class Location extends Product with Serializable

/** Rubble is unpassable */
case object Rubble extends Location

/** Sand is normal terrain */
case object Sand extends Location

/** Rock may contain interesting finds (alien artifacts!) if drilled, and this
  * rock has not been drilled
  */
final case class UndrilledRock(alienArtifacts: Boolean) extends Location

/** This rock has been drilled, and anything interesting will already have been
  * found
  */
case object DrilledRock extends Location
