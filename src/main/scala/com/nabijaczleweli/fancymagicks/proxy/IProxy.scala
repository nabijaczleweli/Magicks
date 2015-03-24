package com.nabijaczleweli.fancymagicks.proxy

trait IProxy {
	def registerItemsAndBlocks(): Unit

	def registerEntities() : Unit

	def registerKeyBindings(): Unit

	def registerHandlers(): Unit

	def registerRenderers(): Unit

	def registerStaffAbilities(): Unit
}
