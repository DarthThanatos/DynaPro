package model

import contract.DynProContract

interface FrontConfiguration{
    fun getDefaultConfiguration(): ArrayList<ArrangementColumn>
}

interface ArrangementColumn : MutableList<Element>{

}

class Column(vararg elements: Element):ArrangementColumn, MutableList<Element> by ArrayList<Element>()

abstract class DynProFrontConfiguration(protected val presenter: DynProContract.Presenter): FrontConfiguration{
    protected val columns: ArrayList<ArrangementColumn> = arrayListOf()

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
}

class UpperModuleFrontConfiguration(presenter: DynProContract.Presenter): DynProFrontConfiguration(presenter){
    override fun getDefaultConfiguration(): ArrayList<ArrangementColumn> = arrayListOf(Column(Shelf()), Column(Drawer()))
}

class BottomModuleFrontConfiguration(presenter: DynProContract.Presenter): DynProFrontConfiguration(presenter){
    override fun getDefaultConfiguration(): ArrayList<ArrangementColumn> = arrayListOf(Column(Door()), Column(Door()))

}