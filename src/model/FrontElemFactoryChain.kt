package model

import config.Config
import contract.FactoriesChain
import contract.TypedFactory

class AllFrontElementFactoriesChain: FactoriesChain<FrontElemFactory>{
    override fun getChain(): List<FrontElemFactory> = listOf(LeftDoorFactory(), RightDoorFactory(),DrawerFactory(), EmptySpaceFactory())

}

interface FrontElemFactory: TypedFactory{
    fun createFrontElem(parentConfig: FrontConfiguration): Element
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

    override fun createFrontElem(parentConfig : FrontConfiguration): Element = LeftDoor(parentConfig = parentConfig)

    override fun createFrontElem(oldElem: Element): Element = copied(LeftDoor(parentConfig = oldElem.parentConfig), oldElem)

}

class RightDoorFactory: FrontElemFactory, FrontElemPropertiesSetter by DefaultFrontElemPropertiesSetter() {

    override fun typeCorrect(type: String): Boolean = type == Config.RIGHT_DOOR_PL

    override fun createFrontElem(parentConfig : FrontConfiguration): Element  = RightDoor(parentConfig = parentConfig)

    override fun createFrontElem(oldElem: Element): Element = copied(RightDoor(parentConfig = oldElem.parentConfig), oldElem)
}

class EmptySpaceFactory : FrontElemFactory, FrontElemPropertiesSetter by DefaultFrontElemPropertiesSetter() {

    override fun typeCorrect(type: String): Boolean = type == Config.EMPTY_SPACE

    override fun createFrontElem(parentConfig : FrontConfiguration): Element = EmptySpace(parentConfig = parentConfig)

    override fun createFrontElem(oldElem: Element): Element = copied(EmptySpace(parentConfig = oldElem.parentConfig), oldElem)
}

class DrawerFactory : FrontElemFactory, FrontElemPropertiesSetter by DefaultFrontElemPropertiesSetter() {

    override fun typeCorrect(type: String): Boolean = type == Config.DRAWER_PL

    override fun createFrontElem(parentConfig : FrontConfiguration): Element = Drawer(parentConfig = parentConfig)

    override fun createFrontElem(oldElem: Element): Element = copied(Drawer(parentConfig = oldElem.parentConfig), oldElem)

}