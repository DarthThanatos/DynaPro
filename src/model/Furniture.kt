package model

import config.Config
import contract.DynProContract
import kotlin.properties.Delegates


interface Furniture{
    var type: String
    var name: String
    var height: Int
    var width: Int
    var depth: Int
    var frontUnitPrice: Int
    var elementUnitPrice: Int
    val frontConfiguration: FrontConfiguration
    var roofInserted : Boolean
    var backInserted: Boolean
    val hasPedestal: Boolean
    fun getElements(): List<Element>
}


class UpperModule(initialName: String, private val presenter: DynProContract.Presenter): Furniture {
    override val hasPedestal: Boolean = false

    override var roofInserted: Boolean = false

    override var backInserted: Boolean = false

    override var frontConfiguration: FrontConfiguration = UpperModuleFrontConfiguration(presenter)

    override var name: String by Delegates.observable(initialName){ property, oldValue, newValue ->  if(newValue != oldValue) presenter.onFurnitureRenamed(oldValue, newValue)}

    override var type: String by Delegates.observable(Config.UPPER_MODULE){property, oldValue, newValue -> presenter.onFurnitureTypeChanged(name) }

    override var frontUnitPrice: Int = 100

    override var elementUnitPrice: Int = 115

    override var height: Int = 200

    override var width: Int = 50

    override var depth: Int = 75


    override fun getElements(): List<Element> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class BottomModule(initialName: String, private val presenter: DynProContract.Presenter): Furniture{
    override val hasPedestal: Boolean = true

    override var roofInserted: Boolean = true

    override var backInserted: Boolean = true

    override var frontConfiguration: FrontConfiguration = BottomModuleFrontConfiguration(presenter)

    override var name: String by Delegates.observable(initialName){ property, oldValue, newValue ->  if(newValue != oldValue) presenter.onFurnitureRenamed(oldValue, newValue)}

    override var type: String by Delegates.observable(Config.BOTTOM_MODULE){property, oldValue, newValue -> presenter.onFurnitureTypeChanged(name) }

    override var frontUnitPrice : Int = 10

    override var elementUnitPrice : Int  = 10

    override var height : Int = 10

    override var width : Int = 50

    override var depth : Int = 10

    override fun getElements(): List<Element> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}