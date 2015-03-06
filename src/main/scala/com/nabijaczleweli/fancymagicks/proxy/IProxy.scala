package com.nabijaczleweli.fancymagicks.proxy

trait IProxy {
	def registerItemsAndBlocks(): Unit

	def registerKeyBindings(): Unit

	def registerHandlers(): Unit
}