package io.github.viacheslavbondarchuk.mapper

import io.github.viacheslavbondarchuk.domain.FuelSort
import io.github.viacheslavbondarchuk.domain.FuelSort.FuelSort
import slick.jdbc.PostgresProfile.api._

/**
 * author: viacheslavbondarchuk
 * date: 6/19/2022
 * time: 5:19 PM
 * */
object PostgresColumnMapper {
  implicit val fuelSortColumnMapper = MappedColumnType.base[FuelSort, String](_.toString, FuelSort.withName)


}
