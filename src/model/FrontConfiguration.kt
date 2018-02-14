package model

import contract.DynProContract
import kotlin.properties.Delegates

interface FrontConfiguration{
    var columnOriented : Boolean
    fun getDefaultConfiguration(): ArrayList<ArrangementColumn>
    fun getConfiguration(): List<ArrangementColumn>
    fun removeElement(elementId: String)
    fun addElementNextTo(elementId:String)
    fun addElementBefore(elementId: String)
    fun addOneElementAggregateNextTo(elementId: String)
    fun addMultiElementAggragateNextTo(elementId: String)
    fun addOneElementAggregateBefore(elementId: String)
    fun addMultiElementAggregateBefore(elementId: String)
}

interface ArrangementColumn : MutableList<Element>

class Column(vararg elements: Element):ArrangementColumn, MutableList<Element> by ArrayList<Element>(){
    init{
        addAll(elements)
    }
}

abstract class DynProFrontConfiguration(protected val parentProject: Project): FrontConfiguration{
    abstract protected val columns: ArrayList<ArrangementColumn>

    override var columnOriented: Boolean by Delegates.observable(true){property, oldValue, newValue -> }

    fun addElementToColumn(element: Element, index: Int){
        if (columns.size == 0){
            columns.addAll(getDefaultConfiguration())
        }
        columns.get(index).add(element)
    }

    fun removeElementAt(columnIndex: Int, elementIndex: Int){
        columns.get(columnIndex).removeAt(elementIndex)
        if(columns.get(columnIndex).isEmpty()) columns.removeAt(columnIndex)
        if(columns.isEmpty()) columns.addAll(getDefaultConfiguration())
    }

    override fun getConfiguration(): List<ArrangementColumn> = ArrayList<ArrangementColumn>(columns)


    protected fun fetchElementWithId(elementId: String) =
            columns.flatMap { it }.filter { it.id == elementId }.single()

    override fun removeElement(elementId: String) {
        println("removing ${fetchElementWithId(elementId).name}")
    }
    override fun addElementNextTo(elementId:String){
        println("adding element next to ${fetchElementWithId(elementId)}")
    }

    override fun addElementBefore(elementId: String){
        println("adding element before ${fetchElementWithId(elementId)}")

    }

    override  fun addOneElementAggregateNextTo(elementId: String){
        println("adding one-aggregate element next to ${fetchElementWithId(elementId)}")
    }

    override fun addMultiElementAggragateNextTo(elementId: String){
        println("adding multi-aggregate element next to ${fetchElementWithId(elementId)}")
    }

    override fun addMultiElementAggregateBefore(elementId: String){
        println("adding multi-aggregate element before ${fetchElementWithId(elementId)}")

    }

    override fun addOneElementAggregateBefore(elementId: String){
        println("adding one-aggregate element before ${fetchElementWithId(elementId)}")
    }

}

class UpperModuleFrontConfiguration(parentProject: Project): DynProFrontConfiguration(parentProject){

    override var columns: ArrayList<ArrangementColumn> = getDefaultConfiguration()
    override fun getDefaultConfiguration(): ArrayList<ArrangementColumn> = arrayListOf(Column(Door()), Column(Drawer()), Column(Shelf()))

    constructor(parentProject: Project, frontConfiguration: FrontConfiguration) : this(parentProject){
        columns = getDefaultConfiguration()
        columnOriented = frontConfiguration.columnOriented
    }
}

class BottomModuleFrontConfiguration(parentProject: Project): DynProFrontConfiguration(parentProject){
    override var columns: ArrayList<ArrangementColumn> = getDefaultConfiguration()
    override fun getDefaultConfiguration(): ArrayList<ArrangementColumn> = arrayListOf(Column(Door()), Column(Door()))
    constructor(parentProject: Project, frontConfiguration: FrontConfiguration) : this(parentProject){
        columns = getDefaultConfiguration()
        columnOriented = frontConfiguration.columnOriented
    }
}