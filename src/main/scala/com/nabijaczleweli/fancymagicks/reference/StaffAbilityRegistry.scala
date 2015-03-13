package com.nabijaczleweli.fancymagicks.reference

import com.nabijaczleweli.fancymagicks.staves.StaffAbility

import scala.collection.mutable.{HashMap => mHashMap, Map => mMap}

object StaffAbilityRegistry {
	private val abilities: mMap[String, StaffAbility] = new mHashMap

	def register(key: String, value: StaffAbility) {
		abilities += key -> value
	}

	def get(key: String) =
		abilities get key
}
