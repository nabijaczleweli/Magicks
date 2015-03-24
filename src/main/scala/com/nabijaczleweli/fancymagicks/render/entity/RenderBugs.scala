package com.nabijaczleweli.fancymagicks.render.entity

import com.nabijaczleweli.fancymagicks.entity.EntityBugs
import com.nabijaczleweli.fancymagicks.util.ResourceLocationFancyMagicks
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.renderer.entity.RenderLiving
import net.minecraft.entity.{EntityLivingBase, Entity}

import scala.collection.immutable.HashMap

@SideOnly(Side.CLIENT)
object RenderBugs extends RenderLiving(new ModelBugs, 0) {
	private var entityToModel: Map[Int, ModelBugs] = new HashMap

	setRenderPassModel(new ModelBugs)

	override def getEntityTexture(entity: Entity) =
		new ResourceLocationFancyMagicks("textures/entity/bug.png")

	override def doRender(entity: Entity, d0: Double, d1: Double, d2: Double, f0: Float, f1: Float) {
		entityToModel get entity.getEntityId match {
			case Some(model) =>
				setRenderPassModel(model)
			case None =>
				entityToModel += entity.getEntityId -> new ModelBugs
		}
		super.doRender(entity.asInstanceOf[EntityBugs], d0, d1, d2, f0, f1)
	}

	override def shouldRenderPass(entity: EntityLivingBase, pass: Int, renderPartialTicks: Float) =
		pass match {
			case 0 =>
				1
			case _ =>
				-1
		}

}
