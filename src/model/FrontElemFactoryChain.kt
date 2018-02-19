package model

import config.Config
import contract.FactoriesChain
import contract.TypedFactory

class AllFrontElementFactoriesChain: FactoriesChain<FrontElemFactory>{
    override fun getChain(): List<FrontElemFactory> = listOf(LeftDoorFactory(), RightDoorFactory(),DrawerFactory(), ShelfFactory())

}

interface FrontElemFactory: TypedFactory{
    fun createFrontElem(): Element
    fun createFrontElem(oldElem: Element) : Element
}


interface FrontElemPropertiesSetter{
    fun copied(newFrontElem: Element, oldFrontElem: Element) : Element
}

class DefaultFrontElemPropertiesSetter: FrontElemPropertiesSetter {
    override fun copied(newFrontElem: Element, oldFrontElem: Element): Element {
        newFrontElem.name = oldFrontElem.name
        newFrontElem.width = oldFrontElem.width
        newFrontElem.height = oldFrontElem.height
        newFrontElem.id = oldFrontElem.id
        newFrontElem.blockedWidth = oldFrontElem.blockedWidth
        newFrontElem.blockedHeight = oldFrontElem.blockedHeight
        newFrontElem.growthRingVerticallyOriented = oldFrontElem.growthRingVerticallyOriented
        newFrontElem.shelvesNumber = oldFrontElem.shelvesNumber
        return newFrontElem
    }
}

class LeftDoorFactory: FrontElemFactory, FrontElemPropertiesSetter by DefaultFrontElemPropertiesSetter(){

    override fun typeCorrect(type: String): Boolean = type == Config.LEFT_DOOR_PL

    override fun createFrontElem(): Element = LeftDoor()

    override fun createFrontElem(oldElem: Element): Element = copied(LeftDoor(), oldElem)

}

class RightDoorFactory: FrontElemFactory, FrontElemPropertiesSetter by DefaultFrontElemPropertiesSetter() {

    override fun typeCorrect(type: String): Boolean = type == Config.RIGHT_DOOR_PL

    override fun createFrontElem(): Element  = RightDoor()

    override fun createFrontElem(oldElem: Element): Element = copied(RightDoor(), oldElem)
}

class ShelfFactory : FrontElemFactory, FrontElemPropertiesSetter by DefaultFrontElemPropertiesSetter() {

    override fun typeCorrect(type: String): Boolean = type == Config.SHELF_PL

    override fun createFrontElem(): Element = Shelf()

    override fun createFrontElem(oldElem: Element): Element = copied(Shelf(), oldElem)
}

class DrawerFactory : FrontElemFactory, FrontElemPropertiesSetter by DefaultFrontElemPropertiesSetter() {

    override fun typeCorrect(type: String): Boolean = type == Config.DRAWER_PL

    override fun createFrontElem(): Element = Drawer()

    override fun createFrontElem(oldElem: Element): Element = copied(Drawer(), oldElem)

}