package com.nabijaczleweli.fancymagicks.element

import com.nabijaczleweli.fancymagicks.reference.Reference
import com.nabijaczleweli.fancymagicks.util.IConfigurable
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.util.{ChatComponentTranslation, EntityDamageSource}
import net.minecraftforge.common.config.Configuration

class ElementalDamageSource(attacker: Entity, elements: Seq[Element]) extends EntityDamageSource("elements", attacker) {
	private lazy val elementNames = elements.toSet map Element.name

	setMagicDamage()

	override def func_151519_b(killed: EntityLivingBase) =
		new ChatComponentTranslation(s"death.attack.${Reference.NAMESPACED_PREFIX}$getDamageType${if(getEntity == null) "" else ".player"}${if(ElementalDamageSource.listElements) ".listed" else ""}",
		                             killed.func_145748_c_, elementNames mkString ",", getEntity.func_145748_c_)
}

object ElementalDamageSource extends IConfigurable {
	var listElements = false

	override def configure(config: Configuration) {
		listElements = config.getBoolean("listElementsElementalDeath", "combat", listElements, "Whether elements used to kill an entity should be listed upon its death")
	}
}
