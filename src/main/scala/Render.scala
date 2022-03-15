package mars

import cats.effect.IO
import doodle.core._
import doodle.syntax.all._
import doodle.interact.syntax._
import doodle.java2d._
import fs2.Stream
import scala.concurrent.duration._

object Render {
  val blockSize = 20.0

  def renderLocation(location: Location): Picture[Unit] =
    location match {
      case DrilledRock =>
        square[Algebra, Drawing](blockSize).fillColor(Color.brown)
      case Rubble => square(blockSize).fillColor(Color.crimson)
      case Sand   => square(blockSize).fillColor(Color.beige)
      case UndrilledRock(alienArtifacts) =>
        if (alienArtifacts) square(blockSize).fillColor(Color.aquamarine)
        else square(blockSize).fillColor(Color.turquoise)
    }

  def renderMap(map: Grid[Location]): Picture[Unit] =
    0.until(map.width)
      .toList
      .map(x =>
        0.until(map.height).toList.map(y => renderLocation(map(x, y))).allAbove
      )
      .allBeside
      .noStroke

  val renderRover: Picture[Unit] =
    circle[Algebra, Drawing](blockSize)
      .fillColor(Color.blueViolet)
      .noStroke

  def mapXtoScreenX(x: Int, width: Int): Double = {
    val offset = if (width % 2 == 0) (blockSize / 2) else 0

    (x - (width / 2)) * blockSize + offset
  }

  def mapYtoScreenY(y: Int, height: Int): Double = {
    val offset = if (height % 2 == 0) (blockSize / 2) else 0

    (((height / 2) - y) * blockSize) - offset
  }

  def render(system: System, actions: List[Action]): Stream[IO, Picture[Unit]] =
    Stream
      .emits(actions)
      .scan(system)((system, action) => system.act(action))
      .map(system =>
        renderRover
          .at(
            mapXtoScreenX(system.rover.x, system.map.width),
            mapYtoScreenY(system.rover.y, system.map.height)
          )
          .on(renderMap(system.map))
      )
      .withFrameRate(500.milli)
}
