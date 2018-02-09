package model

import contract.DynProContract
import kotlin.properties.Delegates

interface Project {
    fun getName(): String
    fun rename(newProjectName: String) : Boolean
    val factoriesChain: FactoriesChain
    val furnituresList: MutableList<Furniture>
    fun addDefaultFurniture() : Boolean
    fun addChildFurniture(childName: String, childFurnitureType: String) : Boolean
    fun removeChildFurniture(oldChildName: String) : Boolean
    fun renameChildFurniture(oldChildName: String, newChildName: String) : Boolean
    fun getFurnitureByName(name: String) : Furniture?
    fun isNameMine(potentialName: String): Boolean
    fun getDefaultFurniture(): Furniture
}

class DynProject(private val presenter: DynProContract.Presenter,  initialName: String = Config.NEW_PROJECT_PL) : Project{

    private var projectName: String by Delegates.observable(initialName){property, oldValue, newValue -> presenter.onProjectNameChanged() }

    override fun getDefaultFurniture(): Furniture =  furnituresList.get(0)

    override fun isNameMine(potentialName: String): Boolean = projectName == potentialName

    override fun getName(): String = projectName

    override fun rename(newProjectName: String) : Boolean {
        val oldProjectName = projectName
        val newNameCorrect = (newProjectName != "") and (getFurnitureByName(newProjectName)  == null)
        projectName = if(newNameCorrect) newProjectName else oldProjectName
        return newNameCorrect
    }
    
    override val furnituresList = mutableListOf<Furniture>()
//    init{
//        furnituresList.addListener { observable, oldValue, newValue -> presenter.onFurnituresListChange() }
//    }

    override val factoriesChain: FactoriesChain = AllFurnitureTypesChain(presenter)

    override fun getFurnitureByName(name: String) : Furniture? = furnituresList.singleOrNull() { it.name == name }

    private fun pickDefaultName(): String{
        for (i in 1..Config.MAX_DEFAULT_FURNITURE_NAMES_NUMBER){
            if (furnitureNotExist(Config.DEFAULT_FURNITURE_NAME_PREFIX_PL + i))
                return Config.DEFAULT_FURNITURE_NAME_PREFIX_PL + i
        }
        return ""
    }

    private fun keepAtLeastOneFurniture(){
        if(furnituresList.size == 0)
            addDefaultFurniture()
    }

    override fun addDefaultFurniture(): Boolean =
        addChildFurniture(
                childName = pickDefaultName(),
                childFurnitureType = Config.UPPER_MODULE
        )


    override fun addChildFurniture(childName: String, childFurnitureType: String) : Boolean{
        if(furnitureNotExist( childName)){
            for (factory: FurnitureFactory in factoriesChain.getChain()){
                if (factory.typeCorrect(childFurnitureType))
                    furnituresList.add(factory.createFurnitureChild(childName))
            }
            presenter.onFurnituresListChange()
            return true
        }
        return false
    }

    override fun removeChildFurniture(oldChildName: String) : Boolean{
        val success = furnituresList.removeIf { it.name == oldChildName }
        if(success) {
            keepAtLeastOneFurniture()
            presenter.onFurnituresListChange()
        }
        return success
    }


    private fun furnitureNotExist(furnitureName: String) : Boolean = furnituresList.count { it.name == furnitureName } == 0

    override fun renameChildFurniture(oldChildName: String, newChildName: String) :Boolean{
        if (furnitureNotExist(newChildName) and (newChildName != projectName) and (newChildName != "")){
            furnituresList.single { it.name == oldChildName }.name=newChildName
            presenter.onFurnituresListChange()
            return true
        }
        return false
    }


    init {
        addChildFurniture(childName = Config.NEW_UPPER_MODULE_PL, childFurnitureType = Config.UPPER_MODULE)
    }

}

interface FactoriesChain{
    fun getChain():List<FurnitureFactory>
}

class AllFurnitureTypesChain(private val presenter: DynProContract.Presenter): FactoriesChain{
    override fun getChain(): List<FurnitureFactory> = listOf(UpperModulesFactory(presenter), BottomModulesFactory(presenter))

}

interface FurnitureFactory{
    fun typeCorrect(type: String): Boolean
    fun  createFurnitureChild(name:String): Furniture
}

class UpperModulesFactory(private val presenter: DynProContract.Presenter): FurnitureFactory{
    override fun typeCorrect(type: String): Boolean = type == Config.UPPER_MODULE

    override fun createFurnitureChild(name: String): Furniture = UpperModule(name, presenter)

}

class BottomModulesFactory(private val presenter: DynProContract.Presenter): FurnitureFactory {
    override fun typeCorrect(type: String): Boolean = type == Config.BOTTOM_MODULE

    override fun createFurnitureChild(name: String): Furniture = BottomModule(name, presenter)

}

interface Furniture{
    var type: String
    var name: String
    var height: Int
    var width: Int
    var depth: Int
    var frontUnitPrice: Int
    var elementUnitPrice: Int
    fun getElements(): List<Element>
}


class UpperModule(initialName: String, private val presenter: DynProContract.Presenter): Furniture {

    override var name: String by Delegates.observable(initialName){property, oldValue, newValue ->  if(newValue != oldValue) presenter.onFurnitureNameChanged(oldValue, newValue)}

    override var type: String by Delegates.observable(Config.UPPER_MODULE){property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureTypeChanged(newValue) }

    override var frontUnitPrice: Int by Delegates.observable(100){property, oldValue, newValue -> if(oldValue != newValue) presenter.onFrontUnitPriceChanged(newValue) }

    override var elementUnitPrice: Int  by Delegates.observable( 115) {property, oldValue, newValue -> if(oldValue != newValue) presenter.onElementUnitPriceChanged(newValue) }

    override var height: Int by Delegates.observable(200){property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureHeightChanged(newValue) }

    override var width: Int by Delegates.observable(50) {property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureWidthChanged(newValue) }

    override var depth: Int by Delegates.observable(75){property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureDepthChanged(newValue) }


    override fun getElements(): List<Element> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class BottomModule(initialName: String, private val presenter: DynProContract.Presenter): Furniture{

    override var name: String by Delegates.observable(initialName){property, oldValue, newValue ->  if(newValue != oldValue) presenter.onFurnitureNameChanged(oldValue, newValue)}

    override var type: String by Delegates.observable(Config.BOTTOM_MODULE){property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureTypeChanged(newValue) }

    override var frontUnitPrice : Int by Delegates.observable(10){property, oldValue, newValue -> if(oldValue != newValue) presenter.onFrontUnitPriceChanged(newValue) }

    override var elementUnitPrice : Int  by Delegates.observable( 10) {property, oldValue, newValue -> if(oldValue != newValue) presenter.onElementUnitPriceChanged(newValue) }

    override var height : Int by Delegates.observable(10){property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureHeightChanged(newValue) }

    override var width : Int by Delegates.observable(10) {property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureWidthChanged(newValue) }

    override var depth : Int by Delegates.observable(10){property, oldValue, newValue -> if(oldValue != newValue) presenter.onFurnitureDepthChanged(newValue) }


    override fun getElements(): List<Element> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

interface Element
