package com.nabijaczleweli.fancymagicks.entity

import com.nabijaczleweli.fancymagicks.util.EntityUtil
import com.nabijaczleweli.fancymagicks.util.EntityUtil.SimpleEntitySpawnData
import net.minecraft.entity.ai.{EntityAINearestAttackableTarget, EntityAIWander}
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.passive.EntityAnimal
import net.minecraft.entity.{SharedMonsterAttributes, Entity, EntityAgeable, EntityLivingBase}
import net.minecraft.util.DamageSource
import net.minecraft.world.World

class EntitySpiritTree(world: World) extends EntityAnimal(world) {
	setSize(2.4f, 7.2f)
	tasks.addTask(1, new EntityAIWander(this, .6D))
	targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, classOf[EntityLivingBase], 0, false, false, IMob.mobSelector))

	override def createChild(entity: EntityAgeable) =
		null

	override val isAIEnabled = true

	override def attackEntity(entity: Entity, distance: Float) =
		if(attackTime <= 0 && distance < 2F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY) {
			attackTime = 30
			attackEntityAsMob(entity)
		}

	override def getBlockPathWeight(x: Int, y: Int, z: Int) =
		worldObj.getLightBrightness(x, y, z) - .5F

	override def applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.maxHealth) setBaseValue 40D
		getEntityAttribute(SharedMonsterAttributes.followRange) setBaseValue 100D
		getEntityAttribute(SharedMonsterAttributes.movementSpeed) setBaseValue .23000000417232513D
		getAttributeMap registerAttribute SharedMonsterAttributes.attackDamage setBaseValue 4D
	}

	override def attackEntityAsMob(entity: Entity) =
		entity.attackEntityFrom(DamageSource causeMobDamage this, getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue.toFloat)
}

object EntitySpiritTree {
	def defaultSummon(inFrontOf: EntityLivingBase) {
		val pos = EntityUtil.rayTraceCoords(inFrontOf, 10)
		val data = SimpleEntitySpawnData(classOf[EntitySpiritTree], inFrontOf.worldObj, pos.xCoord, pos.yCoord, pos.zCoord)
		EntityUtil dispatchSimpleSpawn data
	}
}
