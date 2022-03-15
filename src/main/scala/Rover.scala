package mars

final case class Rover(x: Int, y: Int, energy: Int) {
  def left: Rover =
    Rover(x - 1, y, energy - 1)

  def right: Rover =
    Rover(x + 1, y, energy - 1)

  def up: Rover =
    Rover(x, y - 1, energy - 1)

  def down: Rover =
    Rover(x, y + 1, energy - 1)

  def decreaseEnergy: Rover =
    this.copy(energy = energy - 1)
}
