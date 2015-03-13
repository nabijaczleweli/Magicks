package com.nabijaczleweli.fancymagicks.reference

import java.io.InputStream
import java.util.Scanner

import com.nabijaczleweli.fancymagicks.staves.{StaffAbility, StaffType}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.StatCollector
import org.apache.commons.io.Charsets

import scala.collection.mutable.{HashMap => mHashMap, Map => mMap}

object StaffTypeRegistry {
	class AbilityMissing(which: String) extends StaffAbility {
		override def apply(player: EntityPlayer) {}

		override def description =
			s"tooltip.${Reference.NAMESPACED_PREFIX}staffAbilityErrorMissing"

		override def displayDescription =
			StatCollector.translateToLocalFormatted(description, which)
	}

	private val types: mMap[String, StaffType] = new mHashMap

	def register(key: String, value: StaffType) {
		types += key -> value
	}

	def get(key: String) =
		types get key

	def registerFrom(strm: InputStream) {
		val scanner = new Scanner(strm, Charsets.UTF_8.name)
		while(scanner.hasNextLine) {
			val line = scanner.nextLine().trim
			val sections = line split "|" map {_.trim} // name -> passive -> active -> (optional)texture(defaulted to name)
			if(line.nonEmpty && line(0) != '#' && sections.length >= 3) {
				def abilityOrError(idx: Int) =
					StaffAbilityRegistry get sections(idx) getOrElse new AbilityMissing(sections(idx))
				register(sections(0), new StaffType(if(sections.length > 3) sections(3) else sections(0), s"staff.${sections(0)}.name", abilityOrError(1), abilityOrError(2)))
			}
		}
	}
}
