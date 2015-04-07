package com.nabijaczleweli.fancymagicks.element

import com.nabijaczleweli.fancymagicks.reference.Reference._
import com.nabijaczleweli.fancymagicks.util.EntityUtil
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.Vec3

class ElementBeamAOECaster(who: Entity, elems: Seq[Element]) extends ElementCaster {
	private lazy val force = elems count {classOf[ElementBeam] isAssignableFrom _.getClass}

	var timeLeft: Int = _

	override def start() =
		timeLeft = force * 50

	override def continue() {
		timeLeft = math.max(timeLeft - 1, 0)
		if(timeLeft > 0) {
			val ents = EntityUtil.entitiesInRadius[Entity](who, force * 2.5D)
			/*{
				(0D until 500D by .25D) map {d =>
					val coords = EntityUtil.rayTraceCoords(who.asInstanceOf[EntityLivingBase], d)
					EntityUtil.entitiesAround[EntityLivingBase](who.worldObj, (coords.xCoord, coords.yCoord, coords.zCoord), 1)
				}
			}.flatten.toSet*/ // <-- Forward
			for(e <- ents)
				ElementalDamageSource.dispatchDamage(new ElementalDamageSource(who, elems), e, ) //TODO
		}
	}

	override def end() =
		timeLeft = 0

	override def loadNBTData(tag: NBTTagCompound) =
		timeLeft = tag getInteger s"${NAMESPACED_PREFIX}timeLeft"

	override def saveNBTData(tag: NBTTagCompound) =
		tag.setInteger(s"${NAMESPACED_PREFIX}timeLeft", timeLeft)
}

class NoElementAOECaster(who: Entity) extends ElementCaster {
	def this(who: Entity, elems: Seq[Element]) =
		this(who)

	var chargeup: Int = _

	override def start() =
		chargeup = 0

	override def continue() =
		chargeup = math.min(chargeup + 1, 250)

	override def end() {
		val casterPosVec = Vec3.createVectorHelper(who.posX, who.posY, who.posZ)
		for(e <- EntityUtil.entitiesInRadius[Entity](who, chargeup / 10D)) {
			val direction = (Vec3.createVectorHelper(e.posX, e.posY, e.posZ) subtract casterPosVec).normalize
			val mul = -(chargeup / 50D)
			EntityUtil.dispatchVelocityChange(e, direction.xCoord * mul, direction.yCoord * mul, direction.zCoord * mul)
		}
	}

	override def loadNBTData(tag: NBTTagCompound) =
		chargeup = tag getInteger s"${NAMESPACED_PREFIX}chargeupTime"

	override def saveNBTData(tag: NBTTagCompound) =
		tag.setInteger(s"${NAMESPACED_PREFIX}chargeupTime", chargeup)
}

object ElementAOECaster {
	private def simplyConstruct[T <: ElementCaster : Manifest](who: Entity, elems: Seq[Element]) =
		implicitly[Manifest[T]].runtimeClass.asInstanceOf[Class[T]].getConstructor(classOf[Entity], classOf[Seq[Element]]).newInstance(who, elems)

	private val leads = Map[Class[_ <: Element], (Entity, Seq[Element]) => ElementCaster](classOf[ElementBeam] -> simplyConstruct[ElementBeamAOECaster])
	println(leads)

	def apply(who: Entity, elems: Seq[Element]) =
		elems.sorted match {
			case Nil =>
				new NoElementAOECaster(who)
			case selems =>
				(leads find {_._1 isAssignableFrom elems.head.getClass} getOrElse classOf[ElementCaster] -> (simplyConstruct[NoElementAOECaster](_, _)))._2(who, selems)
		}
}
