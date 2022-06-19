package io.github.viacheslavbondarchuk.holder

import io.github.viacheslavbondarchuk.domain.InternalStateEnum.InternalStateEnum

import scala.collection.mutable

/**
 * author: viacheslavbondarchuk
 * date: 6/19/2022
 * time: 2:00 PM
 * */
object InternalStateHolder {
  private val states: mutable.Map[Long, InternalStateEnum] = mutable.Map()

  def getInternalState(uuid: Long, defaultState: InternalStateEnum): InternalStateEnum = states.getOrElse(uuid, defaultState)

  def updateInternalState(uuid: Long, state: InternalStateEnum): Unit = states.put(uuid, state)

}
