package com.nabijaczleweli.fancymagicks.potion

import java.lang.reflect.{Field, Modifier}
import java.util.{Arrays => jArrays}

import com.nabijaczleweli.fancymagicks.reference.Container
import com.nabijaczleweli.fancymagicks.util.PacketUtil
import com.nabijaczleweli.fancymagicks.util.PacketUtil.BBOSUtil
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.{Potion => mPotion, PotionEffect}

class Potion(bad: Boolean, color: Int, effect: (EntityLivingBase, Int) => Unit = {(_, _) => ()}, id: Int = Potion.nextAvailableId) extends mPotion(Potion resizeIfNeeded id, bad, color) {
	override def setIconIndex(x: Int, y: Int) =
		super.setIconIndex(x, y)

	override def setEffectiveness(effectiveness: Double) =
		super.setEffectiveness(effectiveness)

	override def performEffect(entity: EntityLivingBase, amplifier: Int) =
		effect(entity, amplifier)

	override def isReady(duration: Int, amplifier: Int) =
		true

	def setIconIndex(from: => Int) = {
		try
			statusIconIndex = from
		catch {
			case t: Throwable => // getStatusIconIndex() on server-side
		}
		this
	}
}

object Potion {
	private lazy val modifiersField = {
		val t = classOf[Field] getDeclaredField "modifiers"
		t setAccessible true
		t
	}

	private lazy val potionTypesField =
		classOf[mPotion].getDeclaredFields find {classOf[Array[mPotion]] isAssignableFrom _.getType} match {
			case Some(f) =>
				f setAccessible true
				if((f.getModifiers & Modifier.FINAL) != 0)
					modifiersField.setInt(f, f.getModifiers & ~Modifier.FINAL)
				f
			case None =>
				throw new ReflectiveOperationException(s"No field of type `${classOf[Array[mPotion]].getName}` in ${classOf[mPotion].getName}! This indicates critical tampering!")
		}

	private def resizeIfNeeded(id: Int) = {
		if(mPotion.potionTypes.length <= id)
			potionTypesField.set(null, jArrays.copyOf(mPotion.potionTypes, id + 1))
		id
	}


	def nextAvailableId =
		mPotion.potionTypes.indexOf(null, 1) match { // mPotion.potionTypes[0] is unused, and therefore always null
			case -1 =>
				mPotion.potionTypes.length
			case i =>
				i
		}

	def applyEffect(pot: mPotion, amplifier: Int = 0, duration: Int = 1)(entity: EntityLivingBase) =
		dispatchPotionEffect(new PotionEffect(pot.getId, duration, amplifier, false), entity)

	def dispatchPotionEffect(effect: PotionEffect, on: EntityLivingBase) =
		if(on != null)
			if(on.worldObj.isRemote)
				Container.channel sendToServer (PacketUtil packet PacketUtil.stream << "apply-potion-effect" << on << effect)
			else
				on addPotionEffect effect
}
