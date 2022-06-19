package io.github.viacheslavbondarchuk.domain

import io.github.viacheslavbondarchuk.domain.FuelSort.FuelSort
import io.github.viacheslavbondarchuk.domain.GlobalStateEnum.GlobalStateEnum

import java.time.LocalDate

/**
 * author: viacheslavbondarchuk
 * date: 6/12/2022
 * time: 2:54 PM
 * */
sealed trait IndexedEntity {
  def id(): Long
}

case class GlobalState(id: Long, uuid: Long, state: GlobalStateEnum) extends IndexedEntity

case class User(id: Long, uuid: Long, username: String, firstname: String, lastname: String) extends IndexedEntity

case class CarInfo(id: Long, uuid: Long, mark: String, model: String, consumption: Double, odometer: Long, fuelSort: FuelSort) extends IndexedEntity

case class FuelInfo(id: Long, uuid: Long, quantity: Double, price: Double, odometer: Long, date: LocalDate) extends IndexedEntity


