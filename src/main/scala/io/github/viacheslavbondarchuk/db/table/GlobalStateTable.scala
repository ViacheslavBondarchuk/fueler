package io.github.viacheslavbondarchuk.db.table

import io.github.viacheslavbondarchuk.domain.GlobalState
import slick.jdbc.PostgresProfile.api._

/**
 * author: viacheslavbondarchuk
 * date: 6/12/2022
 * time: 2:48 PM
 * */

class GlobalStateTable(tag: Tag) extends IndexedPostgresTable[GlobalState](tag, "global_states") {

  override def id(): Rep[Long] = column[Long]("id", O.Unique, O.AutoInc, O.PrimaryKey)

  def uuid(): Rep[Long] = column("uuid")

  def state(): Rep[String] = column("state")

  override def * = (id(), uuid(), state()) <> (GlobalState.tupled, GlobalState.unapply)
}