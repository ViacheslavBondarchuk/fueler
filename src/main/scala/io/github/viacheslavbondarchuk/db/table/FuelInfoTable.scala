package io.github.viacheslavbondarchuk.db.table

import io.github.viacheslavbondarchuk.domain.FuelInfo
import slick.jdbc.PostgresProfile.api._

import java.time.LocalDate

/**
 * author: viacheslavbondarchuk
 * date: 6/19/2022
 * time: 8:33 PM
 * */
class FuelInfoTable(tag: Tag) extends IndexedPostgresTable[FuelInfo](tag, "fuel_info") {

  override def id(): Rep[Long] = column("id", O.AutoInc, O.Unique, O.PrimaryKey)

  def uuid(): Rep[Long] = column("uuid")

  def quantity(): Rep[Double] = column("quantity")

  def price(): Rep[Double] = column("price")

  def odometer(): Rep[Long] = column("odometer")

  def date(): Rep[LocalDate] = column("local_date_time")


  override def * = (id(), uuid(), quantity(), price(), odometer(), date()) <> (FuelInfo.tupled, FuelInfo.unapply)
}
