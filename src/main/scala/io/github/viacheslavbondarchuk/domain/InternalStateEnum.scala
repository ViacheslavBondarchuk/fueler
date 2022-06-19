package io.github.viacheslavbondarchuk.domain

/**
 * author: viacheslavbondarchuk
 * date: 6/19/2022
 * time: 12:09 PM
 * */
object InternalStateEnum extends Enumeration {
  type InternalStateEnum = Value

  val MARK, MODEL, CONSUMPTION, ODOMETER, FUEL_SORT, NON_SPECIFY, FUEL_QUANTITY, PRICE, FUEL_ODOMETER, FUEL_INFO_BY_PERIOD = Value
}
