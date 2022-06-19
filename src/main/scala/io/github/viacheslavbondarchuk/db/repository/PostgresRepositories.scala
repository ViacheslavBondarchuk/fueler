package io.github.viacheslavbondarchuk.db.repository

import io.github.viacheslavbondarchuk.db.query.PostgresQuery
import io.github.viacheslavbondarchuk.db.table.{CarInfoTable, FuelInfoTable, GlobalStateTable, UserTable}
import io.github.viacheslavbondarchuk.domain.{CarInfo, FuelInfo, GlobalState, User}
import slick.jdbc.PostgresProfile.api._

import java.time.LocalDate
import scala.concurrent.Future

/**
 * author: viacheslavbondarchuk
 * date: 6/13/2022
 * time: 5:19 PM
 * */
object PostgresRepositories {
  case class GlobalStateRepository() extends AbstractPostgresRepository[GlobalState, GlobalStateTable](PostgresQuery.globalStateTableQuery) {
    def findByUuid(uuid: Long): Future[Option[GlobalState]] = database.run(tableQuery.filter(_.uuid() === uuid).result.headOption)
  }

  case class UserRepository() extends AbstractPostgresRepository[User, UserTable](PostgresQuery.userStateTableQuery) {
    def findByUuid(uuid: Long): Future[Option[User]] = database.run(tableQuery.filter(_.uuid() === uuid).result.headOption)

    def findByUuidInnerJoin(uuid: Long): Future[Option[(User, GlobalState)]] = database.run(tableQuery.join(PostgresQuery.globalStateTableQuery).on(_.uuid() === _.uuid()).filter(_._1.uuid() === uuid).result.headOption)
  }

  case class CarInfoRepository() extends AbstractPostgresRepository[CarInfo, CarInfoTable](PostgresQuery.carInfoTableQuery) {
    def findByUuid(uuid: Long): Future[Option[CarInfo]] = database.run(tableQuery.filter(_.uuid() === uuid).result.headOption)
  }

  case class FuelInfoRepository() extends AbstractPostgresRepository[FuelInfo, FuelInfoTable](PostgresQuery.fuelInfoTableQuery) {
    def findByUuid(uuid: Long): Future[Option[FuelInfo]] = database.run(tableQuery.filter(_.uuid() === uuid).result.headOption)

    def findByUuidAndDate(uuid: Long, start: LocalDate, end: LocalDate): Future[Seq[FuelInfo]] = database.run(tableQuery.filter(_.uuid() === uuid)
      .filter(fi => fi.date() >= start && fi.date() <= end)
      .result)
  }
}
