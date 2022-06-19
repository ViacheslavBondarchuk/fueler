package io.github.viacheslavbondarchuk.db.table

import io.github.viacheslavbondarchuk.db.query.PostgresQuery
import io.github.viacheslavbondarchuk.domain.{FuelInfo, GlobalState, User}
import slick.jdbc.PostgresProfile.api._
import slick.lifted.ForeignKeyQuery


/**
 * author: viacheslavbondarchuk
 * date: 6/14/2022
 * time: 11:46 AM
 * */
class UserTable(tag: Tag) extends IndexedPostgresTable[User](tag, "users") {

  override def id(): Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey, O.Unique)

  def uuid(): Rep[Long] = column[Long]("uuid", O.Unique)

  def username(): Rep[String] = column[String]("username")

  def firstname(): Rep[String] = column[String]("firstname")

  def lastname(): Rep[String] = column[String]("lastname")

  def fkGlobalState(): ForeignKeyQuery[GlobalStateTable, GlobalState] = foreignKey("gs_fk", uuid(), PostgresQuery.globalStateTableQuery)(_.uuid(), ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)

  def fkFuelInfo(): ForeignKeyQuery[FuelInfoTable, FuelInfo] = foreignKey("fi_fk", uuid(), PostgresQuery.fuelInfoTableQuery)(_.uuid(), ForeignKeyAction.Cascade, ForeignKeyAction.Cascade)

  override def * = (id(), uuid(), username(), firstname(), lastname()) <> (User.tupled, User.unapply)
}
