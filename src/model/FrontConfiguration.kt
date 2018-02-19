package model

import config.Config
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
    fun recalculateElementsDimens()
    fun getRemainingColumnOrientedHeight(column: ArrangementAggregate): Int
    fun getRemainingRowOrientedWidth(row: ArrangementAggregate): Int
    fun propagateAggragateSpecificDimen(elementId: String? = null)
    fun propagateAggregateSpecificBlock(elementId: String, propertyBlocked: Boolean)
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

abstract class DynProFrontConfiguration(private val parentProject: Project, override var parentFurniture: Furniture): FrontConfiguration, TypedFactoryChooser<FrontElemFactory> by DefaultFactoryChooser(){
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

    override fun recalculateElementsDimens(){
        if(columnOriented) recalculateColumnOrientedConfig()
        else recalculateRowOrientedConfig()
    }

    private fun propagate(element: Element, propagation: (Element) -> Unit){
        aggregateContainingElementWithId(element.id).forEach { propagation(it) }
    }

    private fun propagateColumnWidth(){
        val remainingWidth = getRemainingColumnOrientedWidth()
        val (blockedWidthColumns, nonBlockedWidthColumns) = aggregates.partition { it[0].blockedWidth }
        blockedWidthColumns.forEach { agg -> propagate(agg[0]){it.width = agg[0].width} }
        nonBlockedWidthColumns.forEach { propagate(it[0]) { it.width = remainingWidth / nonBlockedWidthColumns.size} }
    }

    private fun propagateRowHeight(){
//        propagate(aggregates[0][0]) { it.height = aggregates[0][0].height }
        val remainingHeight = getRemainingRowOrientedHeight()
        val (blockedHeightRows, nonBlockedHeightRows) = aggregates.partition { it[0].blockedHeight }
        blockedHeightRows.forEach { agg -> propagate(agg[0]){it.height = agg[0].height} }
        nonBlockedHeightRows.forEach { propagate(it[0]){it.height = remainingHeight / nonBlockedHeightRows.size} }
    }

    private fun propagateColumnWidthHavingelemId(elementId: String){
        val element = fetchElementWithId(elementId)
        propagate(element) { it.width = element.width }
    }

    private fun propagateRowHeightHavingElemId(elementId: String){
        val element = fetchElementWithId(elementId)
        propagate(element) { it.height = element.height }

    }

    private fun getRemainingColumnOrientedWidth(): Int{
        val blockedWidthColumns = aggregates.filter { it[0].blockedWidth }
        return parentFurniture.width - (aggregates.size + 1) * Config.BETWEEN_ELEMENTS_VERTICAL_GAP - blockedWidthColumns.sumBy { it[0].width }
    }

    private fun getRemainingRowOrientedHeight(): Int{
        val blockedHeightRows = aggregates.filter { it[0].blockedHeight }
        return parentFurniture.height - (aggregates.size + 1) * Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP - blockedHeightRows.sumBy { it[0].height } - parentFurniture.pedestalHeight
    }

    override fun getRemainingColumnOrientedHeight(column: ArrangementAggregate) : Int{
        val blockedHeightElements = column.filter { it.blockedHeight }
        return parentFurniture.height - (column.size + 1) * Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP - blockedHeightElements.sumBy { it.height } - parentFurniture.pedestalHeight

    }

    override fun getRemainingRowOrientedWidth(row: ArrangementAggregate): Int {
        val blockedWidthElements = row.filter { it.blockedWidth }
        return parentFurniture.width - (row.size + 1) * Config.BETWEEN_ELEMENTS_VERTICAL_GAP - blockedWidthElements.sumBy { it.width }
    }

    private fun recalculateColumnOrientedConfig(){
        for(column in aggregates){
            val nonBlockedHeightElements = column.filter { !it.blockedHeight }
            val remainingHeight = getRemainingColumnOrientedHeight(column)
            nonBlockedHeightElements.forEach{ it -> it.height = remainingHeight / nonBlockedHeightElements.size}
        }
    }

    private fun recalculateRowOrientedConfig(){
        for (row in aggregates){
            val nonBlockedWidthElements = row.filter { !it.blockedWidth }
            val remainingWidth = getRemainingRowOrientedWidth(row)
            nonBlockedWidthElements.forEach { it -> it.width = remainingWidth / nonBlockedWidthElements.size }
        }
    }

    override fun propagateAggragateSpecificDimen(elementId:String?){
        if(elementId == null){
            if(columnOriented) propagateColumnWidth()
            else propagateRowHeight()
        }
        else{
            if(columnOriented) propagateColumnWidthHavingelemId(elementId)
            else propagateRowHeightHavingElemId(elementId)
        }
    }

    private fun propagateBlockedWidth(elementId: String, widthBlocked: Boolean){
        val element = fetchElementWithId(elementId)
        propagate(element){it.blockedWidth = widthBlocked}
    }

    private fun propagateBlockedHeight(elementId: String, heightBlocked: Boolean){
        val element = fetchElementWithId(elementId)
        propagate(element){it.blockedHeight = heightBlocked}
    }

    override fun propagateAggregateSpecificBlock(elementId: String, propertyBlocked: Boolean){
        if(columnOriented) propagateBlockedWidth(elementId, propertyBlocked)
        else propagateBlockedHeight(elementId, propertyBlocked)
    }
}

class UpperModuleFrontConfiguration(parentProject: Project, parentFurniture: Furniture): DynProFrontConfiguration(parentProject, parentFurniture){

    override val defaultElementBuilder = { Drawer() }

    override var aggregates: ArrayList<ArrangementAggregate> by Delegates.observable(getDefaultAggregates()){
        _, _, _ -> parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun getDefaultAggregates(): ArrayList<ArrangementAggregate> = arrayListOf(Aggregate(LeftDoor()), Aggregate(Drawer()), Aggregate(Shelf()))

}

class BottomModuleFrontConfiguration(parentProject: Project, parentFurniture: Furniture): DynProFrontConfiguration(parentProject, parentFurniture){

    override val defaultElementBuilder = { Shelf() }

    override var aggregates: ArrayList<ArrangementAggregate> by Delegates.observable(getDefaultAggregates()){
        _, _, _ -> parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun getDefaultAggregates(): ArrayList<ArrangementAggregate> = arrayListOf(Aggregate(LeftDoor()), Aggregate(LeftDoor()))

}