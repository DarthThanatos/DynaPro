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
    fun getElements(): List<Element>
}


class UpperModule(initialName: String, private val presenter: DynProContract.Presenter): Furniture {

    override val frontConfiguration: FrontConfiguration = UpperModuleFrontConfiguration(presenter)

    override var name: String by Delegates.observable(initialName){ property, oldValue, newValue ->  if(newValue != oldValue) presenter.onFurnitureRenamed(oldValue, newValue)}

    override var type: String by Delegates.observable(Config.UPPER_MODULE){ property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureTypeChanged(newValue) }

    override var frontUnitPrice: Int by Delegates.observable(100){ property, oldValue, newValue -> if(oldValue != newValue) presenter.onFrontUnitPriceChanged(newValue) }

    override var elementUnitPrice: Int  by Delegates.observable( 115) { property, oldValue, newValue -> if(oldValue != newValue) presenter.onElementUnitPriceChanged(newValue) }

    override var height: Int by Delegates.observable(200){ property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureHeightChanged(newValue) }

    override var width: Int by Delegates.observable(50) { property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureWidthChanged(newValue) }

    override var depth: Int by Delegates.observable(75){ property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureDepthChanged(newValue) }


    override fun getElements(): List<Element> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class BottomModule(initialName: String, private val presenter: DynProContract.Presenter): Furniture{

    override val frontConfiguration: FrontConfiguration = BottomModuleFrontConfiguration(presenter)

    override var name: String by Delegates.observable(initialName){ property, oldValue, newValue ->  if(newValue != oldValue) presenter.onFurnitureRenamed(oldValue, newValue)}

    override var type: String by Delegates.observable(Config.BOTTOM_MODULE){ property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureTypeChanged(newValue) }

    override var frontUnitPrice : Int by Delegates.observable(10){ property, oldValue, newValue -> if(oldValue != newValue) presenter.onFrontUnitPriceChanged(newValue) }

    override var elementUnitPrice : Int  by Delegates.observable( 10) { property, oldValue, newValue -> if(oldValue != newValue) presenter.onElementUnitPriceChanged(newValue) }

    override var height : Int by Delegates.observable(10){ property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureHeightChanged(newValue) }

    override var width : Int by Delegates.observable(10) { property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureWidthChanged(newValue) }

    override var depth : Int by Delegates.observable(10){ property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureDepthChanged(newValue) }


    override fun getElements(): List<Element> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}