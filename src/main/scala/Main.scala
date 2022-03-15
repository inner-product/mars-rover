package mars

import cats.effect.IOApp
import doodle.interact.syntax._
import doodle.java2d._

object Main extends IOApp.Simple {
  val smallWorld =
    Grid.parse(
      List(
        "**********",
        "*........*",
        "*....r...*",
        "*........*",
        "*..r.....*",
        "*......a.*",
        "*........*",
        "*..a.....*",
        "*......r.*",
        "**********"
      )
    )

  val rover = Rover(1, 1, 10)
  val system = System(rover, smallWorld, 0)
  val actions = List(Right, Right, Right, Right, Down, Drill)

  val run = Render.render(system, actions).animateToIO(Frame.size(400, 400))
}
