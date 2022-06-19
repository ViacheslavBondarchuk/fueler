package io.github.viacheslavbondarchuk.domain

/**
 * author: viacheslavbondarchuk
 * date: 6/14/2022
 * time: 2:23 PM
 * */
object GlobalStateEnum extends Enumeration {
  type GlobalStateEnum = String

  val CREATED = "CREATED"
  val INIT = "INIT"
  val NON_SPECIFY = "NON_SPECIFY"
  val ADD_FUEL = "ADD_FUEL"
  val SHOW_COSTS_BY_PERIOD = "SHOW_COSTS_BETWEEN_PERIOD"
}
