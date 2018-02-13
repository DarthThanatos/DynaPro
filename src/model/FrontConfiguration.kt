package model

import contract.DynProContract
import kotlin.properties.Delegates

interface FrontConfiguration{
    var columnOriented : Boolean
    fun getDefaultConfiguration(): ArrayList<ArrangementColumn>
    fun getConfiguration(): List<ArrangementColumn>
}

interface ArrangementColumn : MutableList<Element>

class Column(vararg elements: Element):ArrangementColumn, MutableList<Element> by ArrayList<Element>(){
    init{
        addAll(elements)
    }
}

abstract class DynProFrontConfiguration(protected val presenter: DynProContract.Presenter): FrontConfiguration{
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

}

class UpperModuleFrontConfiguration(presenter: DynProContract.Presenter): DynProFrontConfiguration(presenter){
    override var columns: ArrayList<ArrangementColumn> = getDefaultConfiguration()
    override fun getDefaultConfiguration(): ArrayList<ArrangementColumn> = arrayListOf(Column(Drawer()), Column(Drawer()))

    constructor(presenter: DynProContract.Presenter, frontConfiguration: FrontConfiguration) : this(presenter){
        columns = getDefaultConfiguration()
        columnOriented = frontConfiguration.columnOriented
    }
}

class BottomModuleFrontConfiguration(presenter: DynProContract.Presenter): DynProFrontConfiguration(presenter){
    override var columns: ArrayList<ArrangementColumn> = getDefaultConfiguration()
    override fun getDefaultConfiguration(): ArrayList<ArrangementColumn> = arrayListOf(Column(Door()), Column(Door()))
    constructor(presenter: DynProContract.Presenter, frontConfiguration: FrontConfiguration) : this(presenter){
        columns = getDefaultConfiguration()
        columnOriented = frontConfiguration.columnOriented
    }
}