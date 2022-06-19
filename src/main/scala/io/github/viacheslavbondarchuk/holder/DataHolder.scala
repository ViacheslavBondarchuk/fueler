package io.github.viacheslavbondarchuk.holder

import io.github.viacheslavbondarchuk.domain.FuelSort.FuelSort
import io.github.viacheslavbondarchuk.domain.{CarInfo, FuelInfo, FuelSort, IndexedEntity}

import java.time.LocalDate
import scala.collection.mutable

/**
 * author: viacheslavbondarchuk
 * date: 6/19/2022
 * time: 2:45 PM
 * */
object DataHolder {
  private val map: mutable.Map[Long, IndexedEntity] = mutable.Map()

  private def update[A](uuid: Long, indexedEntity: IndexedEntity, updater: A => IndexedEntity): Unit = {
    map.update(uuid, updater(indexedEntity.asInstanceOf[A]))
  }

  def updateMark(uuid: Long, mark: String): Unit = {
    update[CarInfo](uuid, map.getOrElseUpdate(uuid, CarInfo(0, uuid, mark, "", 0, 0, FuelSort.A98)), _.copy(mark = mark))
  }

  def updateModel(uuid: Long, model: String): Unit = {
    update[CarInfo](uuid, map.getOrElseUpdate(uuid, CarInfo(0, uuid, "", model, 0, 0, FuelSort.A98)), _.copy(model = model))
  }

  def updateConsumption(uuid: Long, consumption: Double): Unit = {
    update[CarInfo](uuid, map.getOrElseUpdate(uuid, CarInfo(0, uuid, "", "", consumption, 0, FuelSort.A98)), _.copy(consumption = consumption))
  }

  def updateOdometer(uuid: Long, odometer: Long): Unit = {
    update[CarInfo](uuid, map.getOrElseUpdate(uuid, CarInfo(0, uuid, "", "", 0, odometer, FuelSort.A98)), _.copy(odometer = odometer))
  }

  def updateFuelSort(uuid: Long, fuelSort: FuelSort): Unit = {
    update[CarInfo](uuid, map.getOrElseUpdate(uuid, CarInfo(0, uuid, "", "", 0, 0, fuelSort)), _.copy(fuelSort = fuelSort))
  }

  def updateFuelQuantity(uuid: Long, quantity: Double): Unit = {
    map.remove(uuid)
    update[FuelInfo](uuid, map.getOrElseUpdate(uuid, FuelInfo(0, uuid, quantity, 0.0, 0, null)), _.copy(quantity = quantity))
  }

  def updateFuelPrice(uuid: Long, price: Double): Unit = {
    update[FuelInfo](uuid, map.getOrElseUpdate(uuid, FuelInfo(0, uuid, 0.0, price, 0, null)), _.copy(price = price))
  }

  def updateFuelOdometer(uuid: Long, odometer: Long): Unit = {
    update[FuelInfo](uuid, map.getOrElseUpdate(uuid, FuelInfo(0, uuid, 0.0, 0.0, odometer, LocalDate.now())), _.copy(odometer = odometer, date = LocalDate.now()))
  }

  def getDataByUuid[A <: IndexedEntity](uuid: Long): A = map.getOrElse(uuid, CarInfo(0, 0, "", "", 0.0D, 0, FuelSort.A98)).asInstanceOf[A]

}
