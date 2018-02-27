package model

import config.Config
import contract.*
import kotlin.properties.Delegates

interface Project: TypedFactoryChooser<FurnitureFactory>, SlabTree{
    var presenter: DynProContract.Presenter?
    fun savedState(): ProjectSave
    fun reset(name: String, presenter: DynProContract.Presenter)
    fun getName(): String
    fun rename(newProjectName: String) : Boolean
    val factoriesChain: FactoriesChain<FurnitureFactory>
    val furnituresList: MutableList<Furniture>
    fun addDefaultFurniture() : Boolean
    fun addChildFurniture(type: String): Boolean
    fun addChildFurniture(childName: String, childFurnitureType: String) : Boolean
    fun removeFurniture(oldChildName: String) : Boolean
    fun renameFurniture(oldName: String, newName: String) : Boolean
    fun getFurnitureByName(name: String) : Furniture?
    fun isNameMine(potentialName: String): Boolean
    fun getDefaultFurniture(): Furniture
    fun getFurnitureWithChangedType(name: String, newType: String) : Furniture
    fun removeFrontElementFromFurniture(furnitureName: String, elementId: String)

    fun addFrontConfigElementNextTo(furnitureName: String, elementId: String)
    fun addFrontConfigElementBefore(furnitureName: String, elementId: String)
    fun addOneAggregateFrontConfigElemenetNextTo(furnitureName: String, elementId: String)
    fun addMultiFrontConfigElementAggregateNextTo(furnitureName: String, elementId: String)
    fun addOneFrontConfigElementAggregateBefore(furnitureName: String, elementId: String)
    fun addMultiFrontConfigElementAggregateBefore(furnitureName: String, elementId: String)
    fun getProjectAssessment() : Int
}

class DynProject(initialName: String = Config.NEW_PROJECT_PL, private val defaultSlabTree: DefaultSlabTree = DefaultSlabTree(), private val restorableProject: RestorableProject = DefaultRestorableProject())
    : Project, TypedFactoryChooser<FurnitureFactory> by DefaultFactoryChooser(), SlabTree by defaultSlabTree, RestorableProject by restorableProject{


    override val factoriesChain: FactoriesChain<FurnitureFactory> = AllFurnitureTypesChain(this)

    override fun reset(name: String, presenter: DynProContract.Presenter) {
        this.projectName = name
        resetChildren()
        this.furnituresList.removeAll { true }
        this.presenter = presenter
    }

    private var projectName: String by Delegates.observable(initialName){property, oldValue, newValue -> presenter?.onProjectRenamed() }

    override var presenter: DynProContract.Presenter? = null

     set(value) {
        field = value
        addChildFurniture(childName = Config.NEW_UPPER_MODULE_PL, childFurnitureType = Config.UPPER_MODULE)
    }

    override val furnituresList = mutableListOf<Furniture>()

    override fun addChildFurniture(childName: String, childFurnitureType: String) : Boolean{
        if(furnitureNotExist( childName)){
            chooseFactoryTo(childFurnitureType, {val furniture = it.createFurnitureChild(childName); furnituresList.add(furniture); addChild(childName, furniture)}, factoriesChain)
            presenter?.onFurnitureAdded(childName)
            return true
        }
        return false
    }


    override fun getFurnitureWithChangedType(name: String, newType: String) : Furniture{
        val furnitureIndex = furnituresList.indexOf(furnituresList.filter { it.name == name }.single())
        return changeType(newType,furnituresList,furnitureIndex, {furnitureFactory, furniture -> val res = furnitureFactory.createFurnitureChild(furniture); addChild(res.name, res) ;res}, factoriesChain)
    }

    override fun getDefaultFurniture(): Furniture =  furnituresList.last()

    override fun isNameMine(potentialName: String): Boolean = projectName == potentialName

    override fun getName(): String = projectName

    override fun rename(newProjectName: String) : Boolean {
        val oldProjectName = projectName
        val newNameCorrect = (newProjectName != "") and (getFurnitureByName(newProjectName)  == null)
        projectName = if(newNameCorrect) newProjectName else oldProjectName
        return newNameCorrect
    }

    override fun getFurnitureByName(name: String) : Furniture? = furnituresList.singleOrNull() { it.name == name }

    override fun removeFrontElementFromFurniture(furnitureName: String, elementId: String) {
        getFurnitureByName(furnitureName)?.frontConfiguration?.removeElement(elementId)
    }

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

    override fun addChildFurniture(type: String): Boolean = addChildFurniture(pickDefaultName(), type)

    override fun removeFurniture(oldChildName: String) : Boolean{
        val success = furnituresList.removeIf { it.name == oldChildName }
        if(success) {
            removeChild(oldChildName)
            keepAtLeastOneFurniture()
            presenter?.onFurnitureRemoved(oldChildName)
        }
        return success
    }


    private fun furnitureNotExist(furnitureName: String) : Boolean = furnituresList.count { it.name == furnitureName } == 0

    override fun renameFurniture(oldName: String, newName: String) :Boolean{
        if (furnitureNotExist(newName) and (newName != projectName) and (newName != "")){
            furnituresList.single { it.name == oldName }.name= newName
            changeChildId(oldName, newName)
            presenter?.onFurnitureNameChanged(newName)
            return true
        }
        return false
    }



    override fun addFrontConfigElementNextTo(furnitureName: String, elementId: String) {
        getFurnitureByName(furnitureName)?.frontConfiguration?.addElementNextTo(elementId)
    }

    override fun addFrontConfigElementBefore(furnitureName: String, elementId: String) {
        getFurnitureByName(furnitureName)?.frontConfiguration?.addElementBefore(elementId)
    }

    override fun addOneAggregateFrontConfigElemenetNextTo(furnitureName: String, elementId: String) {
        getFurnitureByName(furnitureName)?.frontConfiguration?.addOneElementAggregateNextTo(elementId)
    }

    override fun addMultiFrontConfigElementAggregateNextTo(furnitureName: String, elementId: String) {
        getFurnitureByName(furnitureName)?.frontConfiguration?.addMultiElementAggragateNextTo(elementId)
    }

    override fun addOneFrontConfigElementAggregateBefore(furnitureName: String, elementId: String) {
        getFurnitureByName(furnitureName)?.frontConfiguration?.addOneElementAggregateBefore(elementId)
    }

    override fun addMultiFrontConfigElementAggregateBefore(furnitureName: String, elementId: String) {
        getFurnitureByName(furnitureName)?.frontConfiguration?.addMultiElementAggregateBefore(elementId)
    }

    override fun getProjectAssessment() : Int =
            furnituresList.sumBy { it.getAssessment(it.frontUnitPrice, it.elementUnitPrice, it.getTreeSlabList()) }


    override fun savedState(): ProjectSave = restorableProject.saveState(this)

    init {
        defaultSlabTree.actualSlabTree = this
    }
}

