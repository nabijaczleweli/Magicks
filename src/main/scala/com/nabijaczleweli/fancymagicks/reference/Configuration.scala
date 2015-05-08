package com.nabijaczleweli.fancymagicks.reference

import java.io.File

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.caster.aoe.{ElementShieldAOECaster, ElementEarthAOECaster}
import com.nabijaczleweli.fancymagicks.entity.EntityAOEIceSpike
import com.nabijaczleweli.fancymagicks.entity.properties.ExtendedPropertyElements
import com.nabijaczleweli.fancymagicks.item.ItemStaff
import com.nabijaczleweli.fancymagicks.potion.{PotionDamageAura, PotionDeflectAura, PotionImmunityAura}
import com.nabijaczleweli.fancymagicks.render.entity.ModelBugs
import com.nabijaczleweli.fancymagicks.util.IConfigurable
import net.minecraftforge.common.config.Configuration

object Configuration {
	val configurables = c(ItemStaff) :: c(ExtendedPropertyElements) :: c(ModelBugs) :: c(PotionImmunityAura) :: c(PotionDamageAura) :: c(PotionDeflectAura) :: c(ElementalDamageSource) ::
	                    c(EntityAOEIceSpike) :: c(ElementEarthAOECaster) :: c(ElementShieldAOECaster) :: Nil withFilter {_ != null}
	var config: Configuration = _

	def load(file: File) {
		config = new Configuration(file)
		configurables foreach {_ configure config}
		saveIfNeeded()
	}

	def saveIfNeeded() =
		if(config.hasChanged)
			config.save()

	private def c(ic: => IConfigurable) =
		try
			ic
		catch {
			case ncdfe: NoClassDefFoundError => // class/object @SideOnly'd for the other side
				null
			case t: Throwable =>
				throw new RuntimeException(t)
		}
}
