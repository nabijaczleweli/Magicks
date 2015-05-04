package com.nabijaczleweli.fancymagicks.element

import java.lang.{Float => jFloat}

import com.nabijaczleweli.fancymagicks.element.elements._
import com.nabijaczleweli.fancymagicks.reference.{Container, Reference}
import com.nabijaczleweli.fancymagicks.util.PacketUtil.BBOSUtil
import com.nabijaczleweli.fancymagicks.util.{IConfigurable, PacketUtil}
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.util.{ChatComponentTranslation, EntityDamageSource}
import net.minecraftforge.common.config.Configuration

import scala.collection.immutable.HashMap

class ElementalDamageSource(attacker: Entity, val elements: Seq[Element]) extends EntityDamageSource("elements", attacker) {
	private lazy val elementNames = elements.toSet map Element.name

	setMagicDamage()

	override def func_151519_b(killed: EntityLivingBase) =
		new ChatComponentTranslation(s"death.attack.${Reference.NAMESPACED_PREFIX}$getDamageType${if(getEntity == null) "" else ".player"}${if(ElementalDamageSource.listElements) ".listed" else ""}",
		                             killed.func_145748_c_, elementNames mkString ",", getEntity.func_145748_c_)
}

object ElementalDamageSource extends IConfigurable {
	/** Without explicit type arguments it pulls up `Element{def opposites: List[Element]}` */
	private val elementsDamage: Map[Element, Float] = HashMap(
		ElementArcane -> 3f,
		ElementLife -> -2f,
		ElementCold -> 1.2f,
		ElementFire -> 1.25f,
		ElementShield -> 0f,
		ElementEarth -> 2.75f,
		ElementLightning -> 1.7f,
		ElementWater -> 1.15f,
		ElementIce -> 2.5f,
		ElementSteam -> 1.35f
	)

	var listElements = false
	var damageMultiplierAOE = 2f
	var damageMultiplierForward = .2f

	private def damages(elements: Seq[Element]) =
		(elements groupBy identity).values map {s => s.head -> s.size} map {p => elementsDamage(p._1) * p._2.toFloat}

	def dispatchDamage(source: ElementalDamageSource, attackee: Entity, amount: jFloat) =
		if(attackee != null)
			if(attackee.worldObj.isRemote)
				Container.channel sendToServer (PacketUtil packet PacketUtil.stream << "deal-elemental-damage" << source << attackee << amount)
			else
				attackee.attackEntityFrom(source, amount)

	def damageAOE(elements: Seq[Element]) =
		damages(elements).sum * damageMultiplierAOE

	def damageForward(elements: Seq[Element]) =
		damages(elements).sum * damageMultiplierForward

	override def configure(config: Configuration) {
		val category = category"combat:elemental damage source"

		listElements = config.getBoolean("List elements upon death", category, listElements, "Whether elements used to kill an entity should be listed upon its death")
		damageMultiplierAOE = config.getFloat("Damage multiplier for AOE casting", category, damageMultiplierAOE, 0f, Float.MaxValue, "Damage multiplier applied to hardcoded base damage")
		damageMultiplierForward = config.getFloat("Damage multiplier for Forward casting", category, damageMultiplierForward, 0f, Float.MaxValue, "Damage multiplier applied to hardcoded base damage")
	}
}
