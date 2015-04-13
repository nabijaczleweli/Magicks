package com.nabijaczleweli.fancymagicks.render.entity

import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelRenderer
import net.minecraft.entity.Entity

/** Converted to `scala` version of model exported from `models/entity/aoe_spike_ice.tcn`,
  * since MCF `TechneModel` is (as of 13.4.15) not working.
  *
  * THIS CLASS IS GENERATED SEMI-AUTOMATICALLY! DO NOT TOUCH!
  *
  * @see net.minecraftforge.client.model.techne.TechneModel
  * @see net.minecraftforge.client.model.techne.TechneModelLoader
  */
@SideOnly(Side.CLIENT)
object ModelAOEIceSpike extends ModelBase {
	textureWidth = 64
	textureHeight = 64

	val layer6Res2 = {
		val t = new ModelRenderer(this, 56, 50)
		t.addBox(0, 0, 0, 2, 6, 2)
		t.setRotationPoint(-1, -42, -1)
		t.mirror = true
		t
	}

	val layer5Res4 = {
		val t = new ModelRenderer(this, 6, 48)
		t.addBox(0, 0, 0, 4, 6, 4)
		t.setRotationPoint(-2, -36, -2)
		t.mirror = false
		t
	}

	val layer4Res6 = {
		val t = new ModelRenderer(this, 4, 26)
		t.addBox(0, 0, 0, 6, 6, 6)
		t.setRotationPoint(-3, -30, -3)
		t.mirror = false
		t
	}

	val layer3Res8 = {
		val t = new ModelRenderer(this, 2, 6)
		t.addBox(0, 0, 0, 8, 6, 8)
		t.setRotationPoint(-4, -24, -4)
		t.mirror = false
		t
	}

	val layer2Res10 = {
		val t = new ModelRenderer(this, 24, 10)
		t.addBox(0, 0, 0, 10, 6, 10)
		t.setRotationPoint(-5, -18, -5)
		t.mirror = false
		t
	}

	val layer1Res12 = {
		val t = new ModelRenderer(this, 16, 26)
		t.addBox(0, 0, 0, 12, 6, 12)
		t.setRotationPoint(-6, -12, -6)
		t.mirror = false
		t
	}

	val layer0Res14 = {
		val t = new ModelRenderer(this, 8, 44)
		t.addBox(0, 0, 0, 14, 6, 14)
		t.setRotationPoint(-7, -6, -7)
		t.mirror = false
		t
	}

	override def render(entity: Entity, f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		super.render(entity, f, f1, f2, f3, f4, f5)
		setRotationAngles(f, f1, f2, f3, f4, f5, entity)
		layer6Res2.render(f5)
		layer5Res4.render(f5)
		layer4Res6.render(f5)
		layer3Res8.render(f5)
		layer2Res10.render(f5)
		layer1Res12.render(f5)
		layer0Res14.render(f5)
	}

	override def setRotationAngles(f: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float, entity: Entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity)
	}
}
