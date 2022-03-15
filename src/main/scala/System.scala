package mars

final case class System(rover: Rover, map: Grid[Location], reward: Int) {
  def act(action: Action): System =
    action match {
      case Left =>
        map(rover.x - 1, rover.y) match {
          case DrilledRock      => this.copy(rover = rover.left)
          case Rubble           => this.copy(rover = rover.decreaseEnergy)
          case Sand             => this.copy(rover = rover.left)
          case UndrilledRock(_) => this.copy(rover = rover.left)
        }
      case Down =>
        map(rover.x, rover.y + 1) match {
          case DrilledRock      => this.copy(rover = rover.down)
          case Rubble           => this.copy(rover = rover.decreaseEnergy)
          case Sand             => this.copy(rover = rover.down)
          case UndrilledRock(_) => this.copy(rover = rover.down)
        }
      case Drill =>
        val incrementalReward =
          map(rover.x, rover.y) match {
            case DrilledRock => 0
            case Rubble      => 0
            case Sand        => 0
            case UndrilledRock(alienArtifacts) =>
              if (alienArtifacts) 1 else 0
          }
        val newMap = map(rover.x, rover.y) match {
          case DrilledRock => map
          case Rubble      => map
          case Sand        => map
          case UndrilledRock(_) =>
            map.set(rover.x, rover.y, DrilledRock)
        }

        System(rover.decreaseEnergy, newMap, reward + incrementalReward)
      case Right =>
        map(rover.x + 1, rover.y) match {
          case DrilledRock      => this.copy(rover = rover.right)
          case Rubble           => this.copy(rover = rover.decreaseEnergy)
          case Sand             => this.copy(rover = rover.right)
          case UndrilledRock(_) => this.copy(rover = rover.right)
        }
      case Up =>
        map(rover.x, rover.y - 1) match {
          case DrilledRock      => this.copy(rover = rover.up)
          case Rubble           => this.copy(rover = rover.decreaseEnergy)
          case Sand             => this.copy(rover = rover.up)
          case UndrilledRock(_) => this.copy(rover = rover.up)
        }
    }
}
