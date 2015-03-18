package com.nabijaczleweli.fancymagicks.reference

import java.io.InputStream
import java.util.Scanner

import com.nabijaczleweli.fancymagicks.staves.{AbilityMissing, StaffType}
import cpw.mods.fml.common.Loader
import org.apache.commons.io.Charsets

import scala.collection.JavaConversions._
import scala.collection.mutable.{HashMap => mHashMap}

object StaffTypeRegistry extends mHashMap[String, StaffType] {
	def readAllStaffTypes() =
		Loader.instance.getModList map {_.getModId} map {m => s"/assets/$m/staves/staves.txt"} map {getClass.getResourceAsStream} foreach registerFrom

	def registerFrom(strm: InputStream) =
		if(strm != null) {
			val scanner = new Scanner(strm, Charsets.UTF_8.name)
			while(scanner.hasNextLine) {
				val line = scanner.nextLine().trim
				val sections = line split '|' map {_.trim} // name -> passive -> active -> (optional)texture(defaulted to name)
				if(line.nonEmpty && line(0) != '#' && sections.length >= 3) {
					def abilityOrError(idx: Int) =
						Container.abilityRegistry.getOrElse(sections(idx), new AbilityMissing(sections(idx)))
					+=(sections(0) -> new StaffType(if(sections.length > 3) sections(3) else sections(0), s"staff.${sections(0)}.name", abilityOrError(1), abilityOrError(2)))
				}
			}
			scanner.close()
		}
}
