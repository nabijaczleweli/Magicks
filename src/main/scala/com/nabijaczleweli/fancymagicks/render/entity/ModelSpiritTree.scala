package com.nabijaczleweli.fancymagicks.render.entity

import net.minecraft.client.model.{ModelBase, ModelRenderer}
import net.minecraft.entity.Entity

class ModelSpiritTree extends ModelBase { // Don't laugh at it! It was born like that!
	textureWidth = 32
	textureHeight = 32

	val bodyRenderer = new ModelRenderer(this).addBox(-2, 0, -2, 4, 12, 4) ensuring {mr => mr.setRotationPoint(-1.9F, 12, 0); true}

	override def render(entity: Entity, f0: Float, f1: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
		super.render(entity, f0, f1, f2, f3, f4, f5)
		bodyRenderer render f5
	}
}
