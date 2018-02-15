package model

import kotlin.properties.Delegates


interface FrontConfiguration{
    val parentFurnitureName: String
    var columnOriented : Boolean
    val defaultElementBuilder: () -> Element
    fun getDefaultConfiguration(): ArrayList<ArrangementAggregate>
    fun getConfiguration(): List<ArrangementAggregate>
    fun removeElement(elementId: String)
    fun addElementNextTo(elementId:String)
    fun addElementBefore(elementId: String)
    fun addOneElementAggregateNextTo(elementId: String)
    fun addMultiElementAggragateNextTo(elementId: String)
    fun addOneElementAggregateBefore(elementId: String)
    fun addMultiElementAggregateBefore(elementId: String)
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
    abstract protected val aggregates: ArrayList<ArrangementAggregate>

    override var columnOriented: Boolean by Delegates.observable(true){property, oldValue, newValue -> parentProject.presenter?.onFrontConfigOrientationChanged(parentFurnitureName) }

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

    private fun fetchElementWithId(elementId: String) =
            aggregates.flatMap { it }.single { it.id == elementId }

    private fun indexOfElementWithId(elementId: String) =
            aggregateContainingElementWithId(elementId).indexOf(fetchElementWithId(elementId))

    override fun removeElement(elementId: String) {
        val elementToRemove = fetchElementWithId(elementId)
        val aggregate = aggregateContainingElement(elementToRemove)
        aggregate.remove(elementToRemove)
        if(aggregate.size == 0) aggregates.remove(aggregate)
        parentProject.presenter?.onFrontConfigurationChanged(parentFurnitureName)
    }
    override fun addElementNextTo(elementId:String){
        val indexOfBenchmarkElement = indexOfElementWithId(elementId)
        aggregateContainingElementWithId(elementId).add(indexOfBenchmarkElement + 1, defaultElementBuilder())
        parentProject.presenter?.onFrontConfigurationChanged(parentFurnitureName)

    }

    override fun addElementBefore(elementId: String){
        val indexOfBenchmarkElement: Int = indexOfElementWithId(elementId)
        aggregateContainingElementWithId(elementId).add(maxOf(indexOfBenchmarkElement -1, 0), defaultElementBuilder())
        println("adding element before ${fetchElementWithId(elementId)}")
        parentProject.presenter?.onFrontConfigurationChanged(parentFurnitureName)

    }

   override fun addOneElementAggregateNextTo(elementId: String){
       println("adding one-aggregate element next to ${fetchElementWithId(elementId)}, currently: ${this}")
        val indexOfAggregate: Int = aggregateIndexContainingElementWithId(elementId)
        val newAggregate = Aggregate(defaultElementBuilder())
        aggregates.add(indexOfAggregate + 1, newAggregate)
        println("after: ${this}")
       parentProject.presenter?.onFrontConfigurationChanged(parentFurnitureName)
    }


    override fun addMultiElementAggragateNextTo(elementId: String){
        val indexOfAggregate: Int = aggregateIndexContainingElementWithId(elementId)
        val newAggregate = Aggregate().filledWithRepeatingDefault(times = maxElementsNumberInAggregates(), builder = defaultElementBuilder)
        aggregates.add(indexOfAggregate + 1, newAggregate)
        println("adding multi-aggregate element next to ${fetchElementWithId(elementId)}")
        parentProject.presenter?.onFrontConfigurationChanged(parentFurnitureName)
    }

    override fun addMultiElementAggregateBefore(elementId: String){
        val indexOfAggregate: Int = aggregateIndexContainingElementWithId(elementId)
        val newAggregate = Aggregate().filledWithRepeatingDefault(times = maxElementsNumberInAggregates(), builder = defaultElementBuilder)
        aggregates.add(maxOf(indexOfAggregate - 1, 0), newAggregate)
        println("adding multi-aggregate element before ${fetchElementWithId(elementId)}")
        parentProject.presenter?.onFrontConfigurationChanged(parentFurnitureName)

    }

    override fun addOneElementAggregateBefore(elementId: String){
        val indexOfAggregate: Int = aggregateIndexContainingElementWithId(elementId)
        val newAggregate = Aggregate(defaultElementBuilder())
        aggregates.add(maxOf(indexOfAggregate - 1, 0), newAggregate)
        println("adding one-aggregate element before ${fetchElementWithId(elementId)}")
        parentProject.presenter?.onFrontConfigurationChanged(parentFurnitureName)
    }

    override fun toString(): String =
            aggregates.map { it.toString() }.reduce { acc, arrangementAggregateString -> acc + arrangementAggregateString + "\n"}

}

class UpperModuleFrontConfiguration(parentProject: Project, override val parentFurnitureName: String): DynProFrontConfiguration(parentProject){
    override val defaultElementBuilder = { Drawer() }

    override var aggregates: ArrayList<ArrangementAggregate> = getDefaultConfiguration()
    override fun getDefaultConfiguration(): ArrayList<ArrangementAggregate> = arrayListOf(Aggregate(Door()), Aggregate(Drawer()), Aggregate(Shelf()))

    constructor(parentProject: Project, frontConfiguration: FrontConfiguration) : this(parentProject, frontConfiguration.parentFurnitureName){
        aggregates = getDefaultConfiguration()
        columnOriented = frontConfiguration.columnOriented
    }
}

class BottomModuleFrontConfiguration(parentProject: Project, override val parentFurnitureName: String): DynProFrontConfiguration(parentProject){
    override val defaultElementBuilder = { Shelf() }

    override var aggregates: ArrayList<ArrangementAggregate> = getDefaultConfiguration()
    override fun getDefaultConfiguration(): ArrayList<ArrangementAggregate> = arrayListOf(Aggregate(Door()), Aggregate(Door()))
    constructor(parentProject: Project, frontConfiguration: FrontConfiguration) : this(parentProject, frontConfiguration.parentFurnitureName){
        aggregates = getDefaultConfiguration()
        columnOriented = frontConfiguration.columnOriented
    }
}