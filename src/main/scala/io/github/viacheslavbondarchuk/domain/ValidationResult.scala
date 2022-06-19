package io.github.viacheslavbondarchuk.domain

/**
 * author: viacheslavbondarchuk
 * date: 6/14/2022
 * time: 5:37 PM
 * */
trait ValidationResult[A]

case class Valid[A](value: A) extends ValidationResult[A]

case class Invalid[A]() extends ValidationResult[A]
