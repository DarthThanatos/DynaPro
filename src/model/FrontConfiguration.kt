package model

import contract.DefaultFactoryChooser
import contract.TypedFactoryChooser
import kotlin.properties.Delegates


interface FrontConfiguration: TypedFactoryChooser<FrontElemFactory>{
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
    fun changeTypeOfElem(elementId: String, newType: String): Element
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

abstract class DynProFrontConfiguration(private val parentProject: Project): FrontConfiguration, TypedFactoryChooser<FrontElemFactory> by DefaultFactoryChooser(){
    abstract protected var aggregates: ArrayList<ArrangementAggregate>
    private val factoriesChain = AllFrontElementFactoriesChain()

    override var columnOriented: Boolean by Delegates.observable(true){
        _, _, _ -> parentProject.presenter?.onFrontConfigOrientationChanged(parentFurniture.name)
    }

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

    private fun onAddOneElement(elementId:String, indexModifier: (Int) -> Int){
        val indexOfBenchmarkElement = indexOfElementWithId(elementId)
        aggregateContainingElementWithId(elementId).add(indexModifier(indexOfBenchmarkElement), defaultElementBuilder())
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    private fun onAddAggregate(elementId: String, repeated: Boolean, indexModifier: (Int) -> Int){
        val indexOfAggregate: Int = aggregateIndexContainingElementWithId(elementId)
        val newAggregate =
                if(!repeated) Aggregate(defaultElementBuilder())
                else Aggregate().filledWithRepeatingDefault(
                        times = maxElementsNumberInAggregates(),
                        builder = defaultElementBuilder
                )
        aggregates.add(indexModifier(indexOfAggregate), newAggregate)
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun addElementNextTo(elementId:String){
        onAddOneElement(elementId, {i -> i + 1 })
    }

    override fun addElementBefore(elementId: String){
        onAddOneElement(elementId, { i -> i })
    }

   override fun addOneElementAggregateNextTo(elementId: String){
       onAddAggregate(elementId, repeated = false, indexModifier = {i -> i + 1})
    }


    override fun addMultiElementAggragateNextTo(elementId: String){
        onAddAggregate(elementId, repeated = true, indexModifier = { i -> i + 1 })
    }

    override fun addMultiElementAggregateBefore(elementId: String){
        onAddAggregate(elementId, repeated = true, indexModifier = { i -> i })

    }

    override fun addOneElementAggregateBefore(elementId: String){
        onAddAggregate(elementId, repeated = false, indexModifier = { i -> i })
    }

    override fun toString(): String =
        aggregates.map { it.toString() }.reduce { acc, arrangementAggregateString -> acc + arrangementAggregateString + "\n"}

    override fun updateOrientation(columnOriented: Boolean) {
        aggregates = getDefaultAggregates()
        this.columnOriented = columnOriented
    }

    override fun changeTypeOfElem(elementId: String, newType: String): Element{
        val frontElemIndex = indexOfElementWithId(elementId)
        val aggregate = aggregateContainingElementWithId(elementId)
        return changeType(newType, aggregate, frontElemIndex, {frontElemFactory, element -> frontElemFactory.createFrontElem(element)}, factoriesChain)
    }
}

class UpperModuleFrontConfiguration(parentProject: Project, override var parentFurniture: Furniture): DynProFrontConfiguration(parentProject){

    override val defaultElementBuilder = { Drawer() }

    override var aggregates: ArrayList<ArrangementAggregate> by Delegates.observable(getDefaultAggregates()){
        _, _, _ -> parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun getDefaultAggregates(): ArrayList<ArrangementAggregate> = arrayListOf(Aggregate(LeftDoor()), Aggregate(Drawer()), Aggregate(Shelf()))

}

class BottomModuleFrontConfiguration(parentProject: Project, override var parentFurniture: Furniture): DynProFrontConfiguration(parentProject){

    override val defaultElementBuilder = { Shelf() }

    override var aggregates: ArrayList<ArrangementAggregate> by Delegates.observable(getDefaultAggregates()){
        _, _, _ -> parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun getDefaultAggregates(): ArrayList<ArrangementAggregate> = arrayListOf(Aggregate(LeftDoor()), Aggregate(LeftDoor()))

}