package model

import config.Config
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
    var pedestalHeight: Int
}


class UpperModule(initialName: String, private val parentProject: Project): Furniture {
    override var pedestalHeight: Int = 0

    override val hasPedestal: Boolean = false

    override var roofInserted: Boolean = false

    override var backInserted: Boolean = false

    override var name: String by Delegates.observable(initialName){ _, oldValue, newValue ->  if(newValue != oldValue) parentProject.presenter?.onFurnitureNameChanged(newValue)}

    override var frontConfiguration: FrontConfiguration = UpperModuleFrontConfiguration(parentProject, this)

    override var type: String by Delegates.observable(Config.UPPER_MODULE){ _, _, _ -> parentProject.presenter?.onFurnitureTypeChanged(name) }

    override var frontUnitPrice: Int = 100

    override var elementUnitPrice: Int = 115

    override var height: Int by Delegates.observable(1000){ _, _, _ -> frontConfiguration.recalculateElementsDimens(); parentProject.presenter?.onFrontConfigDimensChanged(name)}

    override var width: Int by Delegates.observable(1500){ _, _, _ -> frontConfiguration.recalculateElementsDimens();  parentProject.presenter?.onFrontConfigDimensChanged(name)}

    override var depth: Int = 500


    override fun getElements(): List<Element> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init{
        frontConfiguration.recalculateElementsDimens()
    }

}

class BottomModule(initialName: String, private val parentProject: Project): Furniture{

    override val hasPedestal: Boolean = true

    override var roofInserted: Boolean = true

    override var backInserted: Boolean = true

    override var name: String by Delegates.observable(initialName){ _, oldValue, newValue ->  if(newValue != oldValue) parentProject.presenter?.onFurnitureNameChanged(newValue)}

    override var pedestalHeight: Int by Delegates.observable(50){_,_,_ -> frontConfiguration.recalculateElementsDimens(); parentProject.presenter?.onPedestalHeightChanged(name)}

    override var frontConfiguration: FrontConfiguration = BottomModuleFrontConfiguration(parentProject, this)

    override var type: String by Delegates.observable(Config.BOTTOM_MODULE){ _, _, _ -> parentProject.presenter?.onFurnitureTypeChanged(name) }

    override var frontUnitPrice : Int = 10

    override var elementUnitPrice : Int  = 10

    override var height : Int by Delegates.observable(1500){ _, _, _ -> frontConfiguration.recalculateElementsDimens(); parentProject.presenter?.onFrontConfigDimensChanged(name)}

    override var width : Int by Delegates.observable(1000){ _, _, _ -> frontConfiguration.recalculateElementsDimens(); parentProject.presenter?.onFrontConfigDimensChanged(name)}

    override var depth : Int = 500

    override fun getElements(): List<Element> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    init{
        frontConfiguration.recalculateElementsDimens()
    }
}