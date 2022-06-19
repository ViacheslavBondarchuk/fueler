package io.github.viacheslavbondarchuk.db.table

import io.github.viacheslavbondarchuk.domain.CarInfo
import io.github.viacheslavbondarchuk.domain.FuelSort.FuelSort
import io.github.viacheslavbondarchuk.mapper.PostgresColumnMapper._
import slick.jdbc.PostgresProfile.api._

/**
 * author: viacheslavbondarchuk
 * date: 6/19/2022
 * time: 5:10 PM
 * */
class CarInfoTable(tag: Tag) extends IndexedPostgresTable[CarInfo](tag, "car_info") {

  override def id(): Rep[Long] = column("id", O.Unique, O.PrimaryKey, O.AutoInc)

  def uuid(): Rep[Long] = column("uuid")

  def mark(): Rep[String] = column("mark")

  def model(): Rep[String] = column("model")

  def consumption(): Rep[Double] = column("consumption")

  def odometer(): Rep[Long] = column("odometer")

  def fuelSort: Rep[FuelSort] = column("fuel_sort")

  override def * = (id(), uuid(), mark(), model(), consumption(), odometer(), fuelSort) <> (CarInfo.tupled, CarInfo.unapply)
}
