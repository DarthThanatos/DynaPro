package model

import javafx.beans.property.SimpleListProperty
import javafx.beans.value.ChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableListBase
import javafx.collections.transformation.SortedList
import presenter.ElementProperty
import presenter.FurnitureProperty
import presenter.GenericListProperty
import presenter.GenericProperty
import kotlin.properties.Delegates

interface Project{
    fun getName(): GenericProperty<String>
    fun rename(newProjectName: String) : Boolean
    val factoriesChain: FactoriesChain
    val furnituresList: GenericListProperty<FurnitureProperty, MutableList<FurnitureProperty>>
    fun addDefaultFurniture() : Boolean
    fun addChildFurniture(childName: String, childFurnitureType: String) : Boolean
    fun removeChildFurniture(oldChildName: String) : Boolean
    fun renameChildFurniture(oldChildName: String, newChildName: String) : Boolean
    fun getFurnitureByName(name: String) : FurnitureProperty?
    fun isNameMine(potentialName: String): Boolean
    fun getDefaultFurniture(): FurnitureProperty
}

class DynProject(private var name: GenericProperty<String> = GenericProperty(Config.NEW_PROJECT_PL)) : Project {

    override fun getDefaultFurniture(): FurnitureProperty =  furnituresList.get(0)

    override fun isNameMine(potentialName: String): Boolean = name.get() == potentialName

    override fun getName(): GenericProperty<String> = name

    override fun rename(newProjectName: String) : Boolean {
        val oldProjectName = name
        val newNameCorrect = (newProjectName != "") and (getFurnitureByName(newProjectName)  == null)
        name = if(newNameCorrect) GenericProperty(newProjectName) else oldProjectName
        return newNameCorrect
    }

    val l = SimpleListProperty<Furniture>()
    var d = Delegates.observable(""){
        property, oldValue, newValue ->
    }
    var s: String by d
    
//    Delegates.observable("<not set>") {
//               prop, old, new -> println("Old value: $old, New value: $new")
//            }
    
    override val furnituresList: GenericListProperty<FurnitureProperty, MutableList<FurnitureProperty>> = GenericListProperty(mutableListOf())

    override val factoriesChain: FactoriesChain = AllFurnitureTypesChain()

    override fun getFurnitureByName(name: String) :FurnitureProperty? = furnituresList.singleOrNull() { it.name.get() == name }
    
    private fun pickDefaultName(): String{
        l.addListener(ChangeListener{observable, oldValue, newValue ->  })
        d = Delegates.observable(""){
            property, oldValue, newValue ->
        }
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
            return true
        }
        return false
    }

    override fun removeChildFurniture(oldChildName: String) : Boolean{
        val success = furnituresList.removeIf { it.name.get() == oldChildName }
        if(success) keepAtLeastOneFurniture()
        return success
    }


    private fun furnitureNotExist(furnitureName: String) : Boolean = furnituresList.count { it.name.get() == furnitureName } == 0

    override fun renameChildFurniture(oldChildName: String, newChildName: String) :Boolean{
        if (furnitureNotExist(newChildName) and (newChildName != name.get()) and (newChildName != "")){
            furnituresList.single { it.name.get() == oldChildName }.name.set(newChildName)
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

class AllFurnitureTypesChain: FactoriesChain{
    override fun getChain(): List<FurnitureFactory> = listOf(UpperModulesFactory(), BottomModulesFactory())

}

interface FurnitureFactory{
    fun typeCorrect(type: String): Boolean
    fun  createFurnitureChild(name:String): FurnitureProperty
}

class UpperModulesFactory: FurnitureFactory{
    override fun typeCorrect(type: String): Boolean = type == Config.UPPER_MODULE

    override fun createFurnitureChild(name: String): FurnitureProperty = FurnitureProperty(UpperModule(GenericProperty(name)))

}

class BottomModulesFactory: FurnitureFactory {
    override fun typeCorrect(type: String): Boolean = type == Config.BOTTOM_MODULE

    override fun createFurnitureChild(name: String): FurnitureProperty = FurnitureProperty(BottomModule(GenericProperty(name)))

}

interface Furniture{
    var type: GenericProperty<String>
    var name: GenericProperty<String>
    var height: GenericProperty<Int>
    var width: GenericProperty<Int>
    var depth: GenericProperty<Int>
    var frontUnitPrice: GenericProperty<Int>
    var elementUnitPrice: GenericProperty<Int>
    fun getElements(): GenericListProperty<ElementProperty, MutableList<ElementProperty>>
}


class UpperModule(override var name: GenericProperty<String>): Furniture {

    override var type: GenericProperty<String> = GenericProperty(Config.UPPER_MODULE)

    override var frontUnitPrice: GenericProperty<Int> = GenericProperty(100)

    override var elementUnitPrice: GenericProperty<Int> = GenericProperty(115)


    override var height: GenericProperty<Int> = GenericProperty(200)

    override var width: GenericProperty<Int> = GenericProperty(50)

    override var depth: GenericProperty<Int> = GenericProperty(75)


    override fun getElements(): GenericListProperty<ElementProperty, MutableList<ElementProperty>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class BottomModule(override var name: GenericProperty<String>): Furniture{


    override var type:  GenericProperty<String> = GenericProperty(Config.BOTTOM_MODULE)

    override var frontUnitPrice: GenericProperty<Int> = GenericProperty(10)

    override var elementUnitPrice: GenericProperty<Int> = GenericProperty(10)


    override var height: GenericProperty<Int> = GenericProperty(10)

    override var width: GenericProperty<Int> = GenericProperty(10)

    override var depth: GenericProperty<Int> = GenericProperty(10)


    override fun getElements(): GenericListProperty<ElementProperty, MutableList<ElementProperty>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

interface Element

