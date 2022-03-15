package mars

sealed abstract class Action extends Product with Serializable
case object Up extends Action
case object Down extends Action
case object Left extends Action
case object Right extends Action
case object Drill extends Action
