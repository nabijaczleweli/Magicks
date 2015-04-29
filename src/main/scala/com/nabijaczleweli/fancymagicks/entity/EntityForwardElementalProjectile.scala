package com.nabijaczleweli.fancymagicks.entity

import com.nabijaczleweli.fancymagicks.element.ElementalDamageSource
import com.nabijaczleweli.fancymagicks.element.elements.Element
import com.nabijaczleweli.fancymagicks.potion.PotionDamageAura
import com.nabijaczleweli.fancymagicks.util.PacketUtil.{BBISUtil, BBOSUtil}
import com.nabijaczleweli.fancymagicks.util.{EntityUtil, IElemental}
import cpw.mods.fml.common.registry.{IEntityAdditionalSpawnData, IThrowableEntity}
import cpw.mods.fml.relauncher.Side
import io.netty.buffer.{ByteBuf, ByteBufInputStream, ByteBufOutputStream}
import net.minecraft.block.Block
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.potion.PotionEffect
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.World

import scala.util.Random

abstract class EntityForwardElementalProjectile(world: World) extends EntityThrowable(world) with IEntityAdditionalSpawnData with IThrowableEntity with IElemental {
	protected def particleBlock: Block
	protected def particleColor: Int
	protected def primaryElement: Element
	protected def baseWidth: Float
	protected def baseHeight: Float


	var force: Int = _
	private var _elems = Seq.empty[Element]

	def elems =
		_elems

	def elems_=(elems: Seq[Element]) {
		_elems = elems
		force = _elems count {_ == primaryElement}
		setSize(force * baseWidth, force * baseHeight)
		println(s"$this ${if(worldObj.isRemote) Side.CLIENT else Side.SERVER}: $force: $elems")
	}

	/** To satisfy `EntitySpawnHadler` */
	def this(world: World, elems: Seq[Element]) {
		this(world)
		init(elems)
	}

	/** Slightly modified constructors from EntityThrowable, since scala can't call different constructors */
	def this(world: World, elb: EntityLivingBase, elems: Seq[Element]) {
		this(world)
		init(elb, elems)
	}

	/** Slightly modified constructors from EntityThrowable, since scala can't call different constructors */
	def this(world: World, xPos: Double, yPos: Double, zPos: Double, elems: Seq[Element]) {
		this(world)
		init(xPos, yPos, zPos, elems)
	}

	/** For easy subclass support */
	protected def init(elems: Seq[Element]) =
		this.elems = elems

	/** For easy subclass support */
	protected def init(elb: EntityLivingBase, elems: Seq[Element]) {
		init(elems)
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

	/** For easy subclass support */
	protected def init(xPos: Double, yPos: Double, zPos: Double, elems: Seq[Element]) {
		init(elems)
		ticksInGround = 0
		setPosition(xPos, yPos, zPos)
		yOffset = 0
	}


	override def onImpact(mop: MovingObjectPosition) {
		if(mop.entityHit != null)
			mop.entityHit.attackEntityFrom(new ElementalDamageSource(getThrower, elems), ElementalDamageSource damageForward elems)

		for(_ <- 0 until (EntityForwardElementalProjectile.rand nextInt 50) * force)
			worldObj.spawnParticle(s"blockcrack_${Block getIdFromBlock particleBlock}_$particleColor", posX, posY, posZ, 0, 0, 0)

		val entitiesAround = EntityUtil.entitiesInRadius[EntityLivingBase](this, PotionDamageAura.blocksPerLevel * force) filterNot {_ == thrower}
		elems groupBy identity filter {pr => pr._1 != primaryElement} map {pr => pr._1 -> pr._2.size} map
		                              {pr => new PotionEffect(PotionDamageAura(pr._1).getId, 1, pr._2, false)} foreach
		                              {pef => entitiesAround foreach {elb => 0 until pef.getAmplifier foreach {_ => pef performEffect elb}}}

		if(!worldObj.isRemote)
			setDead()
	}

	override val getGravityVelocity = super.getGravityVelocity / 10

	override def setThrower(entity: Entity) =
		thrower = entity.asInstanceOf[EntityLivingBase]

	override def readSpawnData(additionalData: ByteBuf) {
		val bbis = new ByteBufInputStream(additionalData)

		val newelems = new Array[Seq[Element]](1)
		bbis >> newelems
		elems = newelems.head
	}

	override def writeSpawnData(buffer: ByteBuf) {
		val bbos = new ByteBufOutputStream(buffer)

		bbos << elems
	}
}

object EntityForwardElementalProjectile {
	protected val rand = new Random
}
