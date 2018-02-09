package presenter

import main.Binder
import model.Element
import model.FactoriesChain
import model.Furniture
import model.Project
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


interface Property<T>{
    fun addChangeListener(id: String, changeListener: ChangeListener)
    fun get():T
    fun set(value: T)
    fun notifyListeners()
}

open class GenericProperty<T>(private var property: T): Property<T>{

    override fun notifyListeners() {
        for(changeListener in listeners.values){
            changeListener.stateChanged(ChangeEvent(this))
        }
    }

    private val listeners: MutableMap<String, ChangeListener> = mutableMapOf()

    override fun addChangeListener(id: String, changeListener: ChangeListener) {
        listeners.put(id, changeListener)
    }

    override fun get():T = property

    override fun set(value: T) {
        if(value != property){
            property = value
            notifyListeners()
        }
    }
}


interface ListProperty<T, U: MutableList<T>> : Property<U>{
    fun add(value: T)
    fun remove(value: T)
}

class GenericListProperty<T, U: MutableList<T>>(private val list: U): GenericProperty<U>(list), MutableList<T> by list{
    override fun add(element: T) : Boolean{
        val success = list.add(element)
        if(success)notifyListeners()
        return success
    }

    override fun remove(element: T) : Boolean{
        val success = list.remove(element)
        if(success) notifyListeners()
        return success
    }

}


class ProjectProperty(project: Project): GenericProperty<Project>(project), Project{

    override val factoriesChain: FactoriesChain
        get() = super<GenericProperty>.get().factoriesChain


    override fun addChildFurniture(childName: String, childFurnitureType: String): Boolean {
        val success = super<GenericProperty>.get().addChildFurniture(childName, childFurnitureType)
        if(success) notifyListeners()
        return success
    }

    override fun addDefaultFurniture(): Boolean {
        val success = super<GenericProperty>.get().addDefaultFurniture()
        if(success) notifyListeners()
        return success
    }


    override fun removeChildFurniture(oldChildName: String) :Boolean{
        val success = super<GenericProperty>.get().removeChildFurniture(oldChildName)
        if(success)notifyListeners()
        return success
    }

    override fun rename(newProjectName: String) :Boolean{
        val success = super<GenericProperty>.get().rename(newProjectName)
        if(success)notifyListeners()
        return success
    }

    override fun renameChildFurniture(oldChildName: String, newChildName: String): Boolean {
        val success = super<GenericProperty>.get().renameChildFurniture(oldChildName, newChildName)
        if(success) notifyListeners()
        return success
    }

    override fun getDefaultFurniture(): FurnitureProperty  = super<GenericProperty>.get().getDefaultFurniture()

    override fun getFurnitureByName(name: String): FurnitureProperty? = super<GenericProperty>.get().getFurnitureByName(name)

    override fun getName(): GenericProperty<String> = super<GenericProperty>.get().getName()

    override fun isNameMine(potentialName: String): Boolean =
        super<GenericProperty>.get().isNameMine(potentialName)

    override val furnituresList: GenericListProperty<FurnitureProperty, MutableList<FurnitureProperty>>
        get() = super<GenericProperty>.get().furnituresList

}


class FurnitureProperty(private val furniture: Furniture): Furniture by furniture, GenericProperty<Furniture>(furniture)
class ElementProperty(private val element: Element) : Element by element, GenericProperty<Element>(element)