package io.github.viacheslavbondarchuk.db.query

import io.github.viacheslavbondarchuk.db.table.{CarInfoTable, FuelInfoTable, GlobalStateTable, UserTable}
import slick.jdbc.PostgresProfile.api._

/**
 * author: viacheslavbondarchuk
 * date: 6/14/2022
 * time: 3:15 PM
 * */
object PostgresQuery {
  lazy val globalStateTableQuery: TableQuery[GlobalStateTable] = TableQuery[GlobalStateTable]
  lazy val userStateTableQuery: TableQuery[UserTable] = TableQuery[UserTable]
  lazy val carInfoTableQuery: TableQuery[CarInfoTable] = TableQuery[CarInfoTable]
  lazy val fuelInfoTableQuery: TableQuery[FuelInfoTable] = TableQuery[FuelInfoTable]

}
