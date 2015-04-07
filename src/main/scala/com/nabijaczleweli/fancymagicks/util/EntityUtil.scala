package com.nabijaczleweli.fancymagicks.util

import com.nabijaczleweli.fancymagicks.reference.Container
import com.nabijaczleweli.fancymagicks.util.PacketUtil.BBOSUtil
import net.minecraft.client.renderer.culling.{Frustrum, ICamera}
import net.minecraft.command.IEntitySelector
import net.minecraft.entity.{Entity, EntityLivingBase}
import net.minecraft.util.{AxisAlignedBB, MathHelper}
import net.minecraft.world.World

import scala.collection.JavaConversions._
import scala.util.Random

object EntityUtil {
	case class SimpleEntitySpawnData(entityClass: Class[_ <: Entity], world: World, x: Double, y: Double, z: Double) {
		def spawn() = {
			val entity = entityClass getConstructor classOf[World] newInstance world
			entity.setPosition(x, y, z)
			world spawnEntityInWorld entity
			entity
		}
	}

	def dispachSimpleSpawn(sesd: SimpleEntitySpawnData) =
		if(sesd.world.isRemote)
			Container.channel sendToServer (PacketUtil packet PacketUtil.stream << "spawn-entity-simple" << sesd)
		else
			sesd.spawn()

	def dispatchVelocityChange(e: Entity, xChange: Double, yChange: Double, zChange: Double) =
		if(e.worldObj.isRemote)
			Container.channel sendToServer (PacketUtil packet PacketUtil.stream << "change-velocity" << e << xChange << yChange << zChange)
		else
			e.addVelocity(xChange, yChange, zChange)


	def rayTraceCoords(from: EntityLivingBase, by: Double) = {
		val look = from getLook 1
		(from getPosition 1).addVector(look.xCoord * 10D, look.yCoord * 10D, look.zCoord * 10D)
	}

	def rayTrace(from: EntityLivingBase, by: Double) = {
		val look = from getLook 1
		val pos = from getPosition 1
		from.worldObj.func_147447_a(pos, pos.addVector(look.xCoord * 10D, look.yCoord * 10D, look.zCoord * 10D), false, false, false)
	}


	def entitiesInRadius[T : Manifest](entity: Entity, r: Double, includeSelf: Boolean = false): Seq[Entity with T] =
		entity.worldObj.selectEntitiesWithinAABB(classOf[Entity], entity.boundingBox.expand(r, r, r), new IEntitySelector {
			override def isEntityApplicable(e: Entity) =
				implicitly[Manifest[T]].runtimeClass isAssignableFrom e.getClass
		}) map {_.asInstanceOf[Entity with T]} filter {includeSelf || _ != entity}

	def entitiesAround[T : Manifest](world: World, xyz: (Double, Double, Double), r: Double): Seq[Entity with T] =
	world.selectEntitiesWithinAABB(classOf[Entity], AxisAlignedBB.getBoundingBox(xyz._1, xyz._2, xyz._3, xyz._1 + 1, xyz._2 + 1, xyz._3 + 1).expand(r, r, r), new IEntitySelector {
		override def isEntityApplicable(e: Entity) =
			implicitly[Manifest[T]].runtimeClass isAssignableFrom e.getClass
	}) map {_.asInstanceOf[Entity with T]}

	def filterForFrustrum[T](entities: Seq[Entity with T], camera: ICamera) =
		entities filter {camera isBoundingBoxInFrustum _.boundingBox} // Similiar to how RenderGlobal does it

	def frustrumFor(entity: Entity) = {
		val t: ICamera = new Frustrum
		t.setPosition(entity.posX, entity.posY, entity.posZ)
		t
	}


	def teleportTo(entity: EntityLivingBase, x: Double, y: Double, z: Double) { // Stolen from EntityEnderman, except Entity[LivingBase] and setPosition[AndUpdate]() for client -> server synchronization
		val prevX = entity.posX
		val prevY = entity.posY
		val prevZ = entity.posZ
		entity.posX = x
		entity.posY = y
		entity.posZ = z
		var teleported = false
		val floorX = MathHelper floor_double entity.posX
		var floorY = MathHelper floor_double entity.posY
		val floorZ = MathHelper floor_double entity.posZ

		if(entity.worldObj.blockExists(floorX, floorY, floorZ)) {
			var valid = false
			while(!valid && floorY > 0) {
				val block = entity.worldObj.getBlock(floorX, floorY - 1, floorZ)
				if(block.getMaterial.blocksMovement)
					valid = true
				else
					entity.posY -= 1
					floorY -= 1
			}
			if(valid) {
				entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ)
				if(entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty && !entity.worldObj.isAnyLiquid(entity.boundingBox))
					teleported = true
			}
		}

		if(!teleported)
			entity.setPositionAndUpdate(prevX, prevY, prevZ)
	}

	def teleportRandomly(entity: EntityLivingBase, rand: Random = new Random) { // Stolen from EntityEnderman
		val x = entity.posX + (rand.nextDouble() - .5D) * 64D
		val y = entity.posY + (rand.nextInt(64) - 32).toDouble
		val z = entity.posZ + (rand.nextDouble() - .5D) * 64D
		teleportTo(entity, x, y, z)
	}
}
