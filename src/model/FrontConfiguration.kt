package model

import kotlin.properties.Delegates


interface FrontConfiguration{
    var parentFurniture: Furniture
    var columnOriented : Boolean
    val defaultElementBuilder: () -> Element
    fun getDefaultAggregates(): ArrayList<ArrangementAggregate>
    fun getConfiguration(): List<ArrangementAggregate>
    fun removeElement(elementId: String)
    fun addElementNextTo(elementId:String)
    fun addElementBefore(elementId: String)
    fun addOneElementAggregateNextTo(elementId: String)
    fun addMultiElementAggragateNextTo(elementId: String)
    fun addOneElementAggregateBefore(elementId: String)
    fun addMultiElementAggregateBefore(elementId: String)
    fun updateOrientation(columnOriented: Boolean)
    fun fetchElementWithId(elementId: String): Element
}

interface ArrangementAggregate : MutableList<Element>

class Aggregate(vararg elements: Element): ArrangementAggregate, MutableList<Element> by ArrayList(){
    init{
        addAll(elements)
    }

    override fun toString(): String = this.map { it.toString() }.reduce { acc, s -> acc + s + ", " }

    fun filledWithRepeatingDefault(times: Int, builder: ()->Element) : Aggregate{
        for(i in 1..times) add(builder())
        return this
    }
}

abstract class DynProFrontConfiguration(protected val parentProject: Project): FrontConfiguration{
    abstract protected var aggregates: ArrayList<ArrangementAggregate>

    override var columnOriented: Boolean by Delegates.observable(true){property, oldValue, newValue -> parentProject.presenter?.onFrontConfigOrientationChanged(parentFurniture.name) }

    override fun getConfiguration(): List<ArrangementAggregate> = ArrayList<ArrangementAggregate>(aggregates)

    private fun maxElementsNumberInAggregates(): Int = aggregates.maxBy { arrangementAggregate -> arrangementAggregate.size }!!.size

    private fun aggregateContainingElementWithId(elementId: String) : ArrangementAggregate =
            aggregateContainingElement(fetchElementWithId(elementId))

    private fun aggregateContainingElement(element: Element): ArrangementAggregate =
            aggregates.find { arrangementAggregate  : ArrangementAggregate ->  arrangementAggregate.contains(element) }!!

    private fun aggregateIndexContainingElementWithId(elementId: String) =
            aggregateIndexContainingElement(fetchElementWithId(elementId))

    private fun aggregateIndexContainingElement(element: Element) : Int =
            aggregates.indexOf( aggregateContainingElement(element))

    override fun fetchElementWithId(elementId: String) =
            aggregates.flatMap { it }.single { it.id == elementId }

    private fun indexOfElementWithId(elementId: String) =
            aggregateContainingElementWithId(elementId).indexOf(fetchElementWithId(elementId))

    override fun removeElement(elementId: String) {
        val elementToRemove = fetchElementWithId(elementId)
        val aggregate = aggregateContainingElement(elementToRemove)
        aggregate.remove(elementToRemove)
        if(aggregate.size == 0) aggregates.remove(aggregate)
        if(aggregates.size == 0) aggregates.add(Aggregate(defaultElementBuilder()))
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun addElementNextTo(elementId:String){
        val indexOfBenchmarkElement = indexOfElementWithId(elementId)
        aggregateContainingElementWithId(elementId).add(indexOfBenchmarkElement + 1, defaultElementBuilder())
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)

    }

    override fun addElementBefore(elementId: String){
        val indexOfBenchmarkElement: Int = indexOfElementWithId(elementId)
        aggregateContainingElementWithId(elementId).add(indexOfBenchmarkElement, defaultElementBuilder())
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)

    }

   override fun addOneElementAggregateNextTo(elementId: String){
       val indexOfAggregate: Int = aggregateIndexContainingElementWithId(elementId)
       val newAggregate = Aggregate(defaultElementBuilder())
       aggregates.add(indexOfAggregate + 1, newAggregate)
       parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }


    override fun addMultiElementAggragateNextTo(elementId: String){
        val indexOfAggregate: Int = aggregateIndexContainingElementWithId(elementId)
        val newAggregate = Aggregate().filledWithRepeatingDefault(times = maxElementsNumberInAggregates(), builder = defaultElementBuilder)
        aggregates.add(indexOfAggregate + 1, newAggregate)
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun addMultiElementAggregateBefore(elementId: String){
        val indexOfAggregate: Int = aggregateIndexContainingElementWithId(elementId)
        val newAggregate = Aggregate().filledWithRepeatingDefault(times = maxElementsNumberInAggregates(), builder = defaultElementBuilder)
        aggregates.add(indexOfAggregate, newAggregate)
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)

    }

    override fun addOneElementAggregateBefore(elementId: String){
        val indexOfAggregate: Int = aggregateIndexContainingElementWithId(elementId)
        val newAggregate = Aggregate(defaultElementBuilder())
        aggregates.add(indexOfAggregate, newAggregate)
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun toString(): String =
        aggregates.map { it.toString() }.reduce { acc, arrangementAggregateString -> acc + arrangementAggregateString + "\n"}

    override fun updateOrientation(columnOriented: Boolean) {
        aggregates = getDefaultAggregates()
        this.columnOriented = columnOriented
    }

}

class UpperModuleFrontConfiguration(parentProject: Project, override var parentFurniture: Furniture): DynProFrontConfiguration(parentProject){

    override val defaultElementBuilder = { Drawer() }

    override var aggregates: ArrayList<ArrangementAggregate> by Delegates.observable(getDefaultAggregates()){ property, oldValue, newValue -> parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name) }

    override fun getDefaultAggregates(): ArrayList<ArrangementAggregate> = arrayListOf(Aggregate(Door()), Aggregate(Drawer()), Aggregate(Shelf()))

}

class BottomModuleFrontConfiguration(parentProject: Project, override var parentFurniture: Furniture): DynProFrontConfiguration(parentProject){

    override val defaultElementBuilder = { Shelf() }

    override var aggregates: ArrayList<ArrangementAggregate> by Delegates.observable(getDefaultAggregates()){ property, oldValue, newValue -> parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name) }

    override fun getDefaultAggregates(): ArrayList<ArrangementAggregate> = arrayListOf(Aggregate(Door()), Aggregate(Door()))

}