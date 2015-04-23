package com.nabijaczleweli.fancymagicks.entity

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.elements.{ElementEarth, Element}
import com.nabijaczleweli.fancymagicks.util.IElemental
import net.minecraft.block.Block
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.init.Blocks
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World

import scala.util.Random

class EntityEarthBall(world: World, var elems: Seq[Element]) extends EntityThrowable(world) with IElemental {
	/** `def` here, because `elems` will be set at seemingly random times */
	private def force =
		elems count {_ == ElementEarth}

	/** To satisfy `EntitySpawnHadler` */
	def this(world: World) =
		this(world, Nil)

	/** Slightly modified constructors from EntityThrowable, since scala can't call different constructors */
	def this(world: World, elb: EntityLivingBase, elems: Seq[Element]) {
		this(world, elems)
		thrower = elb
		setLocationAndAngles(elb.posX, elb.posY + elb.getEyeHeight.toDouble, elb.posZ, elb.rotationYaw, elb.rotationPitch)
		posX -= math.cos(rotationYaw / 180 * math.Pi) * .16
		posY -= .10000000149011612D
		posZ -= math.sin(rotationYaw / 180 * math.Pi) * .16
		setPosition(posX, posY, posZ)
		yOffset = 0
		motionX = -math.sin(rotationYaw / 180 * math.Pi) * math.cos(rotationPitch / 180 * math.Pi) * .4
		motionZ = math.cos(rotationYaw / 180 * math.Pi) * math.cos(rotationPitch / 180 * math.Pi) * .4
		motionY = -math.sin((rotationPitch + func_70183_g) / 180 * math.Pi.toFloat) * .4
		setThrowableHeading(motionX, motionY, motionZ, func_70182_d, 1)
	}

	/** Slightly modified constructors from EntityThrowable, since scala can't call different constructors */
	def this(world: World, xPos: Double, yPos: Double, zPos: Double, elems: Seq[Element]) {
		this(world, elems)
		ticksInGround = 0
		setPosition(xPos, yPos, zPos)
		yOffset = 0
	}


	override def onEntityUpdate() {
		setSize(force * width, force * height)
		super.onEntityUpdate()
	}

	override def onImpact(mop: MovingObjectPosition) {
		if(mop.entityHit != null)
			mop.entityHit.attackEntityFrom(new ElementalDamageSource(getThrower, elems), ElementalDamageSource damageForward elems)

		for(_ <- 0 until (EntityEarthBall.rand nextInt 50) * force)
			worldObj.spawnParticle(s"blockcrack_${Block getIdFromBlock Blocks.dirt}_16777215", posX, posY, posZ, 0, 0, 0)

		if(!worldObj.isRemote)
			setDead()
	}

	override val getGravityVelocity = super.getGravityVelocity / 10
}

object EntityEarthBall {
	private val rand = new Random
}
