package com.nabijaczleweli.fancymagicks.element

import com.nabijaczleweli.fancymagicks.reference.{Container, Reference}
import com.nabijaczleweli.fancymagicks.util.PacketUtil.BBOSUtil
import com.nabijaczleweli.fancymagicks.util.{IConfigurable, PacketUtil}
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.util.{ChatComponentTranslation, EntityDamageSource}
import net.minecraftforge.common.config.Configuration

class ElementalDamageSource(attacker: Entity, val elements: Seq[Element]) extends EntityDamageSource("elements", attacker) {
	private lazy val elementNames = elements.toSet map Element.name

	setMagicDamage()

	override def func_151519_b(killed: EntityLivingBase) =
		new ChatComponentTranslation(s"death.attack.${Reference.NAMESPACED_PREFIX}$getDamageType${if(getEntity == null) "" else ".player"}${if(ElementalDamageSource.listElements) ".listed" else ""}",
		                             killed.func_145748_c_, elementNames mkString ",", getEntity.func_145748_c_)
}

object ElementalDamageSource extends IConfigurable {
	var listElements = false

	def dispatchDamage(source: ElementalDamageSource, attackee: Entity, amount: Float) =
		if(attackee.worldObj.isRemote)
			Container.channel sendToServer (PacketUtil packet PacketUtil.stream << "deal-elemental-damage" << source << attackee << amount)
		else
			attackee.attackEntityFrom(source, amount)

	override def configure(config: Configuration) {
		listElements = config.getBoolean("listElementsElementalDeath", "combat", listElements, "Whether elements used to kill an entity should be listed upon its death")
	}
}
