package model

import config.Config
import contract.DefaultFactoryChooser
import contract.DefaultSlabTree
import contract.SlabTree
import contract.TypedFactoryChooser
import model.slab.*
import java.awt.Dimension
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


interface FrontConfiguration: TypedFactoryChooser<FrontElemFactory>, SlabTree{
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
    fun propagateAggragateSpecificDimen(elementId: String)
    fun propagateAggregateSpecificBlock(elementId: String, propertyBlocked: Boolean)
    fun getMaxDimensionOf(elementId: String): Dimension
    fun canBlockWidth(elementId: String): Boolean
    fun canBlockHeight(elementId: String): Boolean
    fun getBlockedHeight(): Int
    fun getBlockedWidth(): Int
    fun isElemWithIdLastToTheLeft(elementId: String): Boolean
    fun isElemWithIdLastToTheRight(elementId: String): Boolean
    fun isElemWithIdLastToTheTop(elementId: String): Boolean
    fun isElemWithIdLastToTheBottom(elementId: String): Boolean
}

interface ArrangementAggregate : MutableList<Element>, SlabTree{
    val id : String
    fun getAggregateSeparatorFirstDimension(): Int
    val parentConfiguration: FrontConfiguration
    fun getAggregateHeightWithGaps(columnOriented: Boolean): Int
    fun getAggregateWidthWithGaps(columnOriented: Boolean): Int
    fun getAggregateSeparatorSecondDimension(): Int
    fun getElementSeparatorFirstDimension(): Int
    fun getElementSeparatorSecondDimension(): Int
}

class Aggregate(vararg elements: Element, override val parentConfiguration: FrontConfiguration, private val arrayDefaultDelegate: ArrayList<Element>  = ArrayList(), private val slabTreeDefaultDelegate: SlabTree = DefaultSlabTree()):
        ArrangementAggregate, MutableList<Element> by arrayDefaultDelegate, SlabTree by slabTreeDefaultDelegate{

    override fun getAggregateSeparatorFirstDimension(): Int {
        if(parentConfiguration.columnOriented) return ColumnOrientedAggregateSeparatorSlab(this, 0).firstDimension
        else return RowOrientedAggregateSeparatorSlab(this, 0).firstDimension
    }

    override fun getAggregateSeparatorSecondDimension():Int{
        if(parentConfiguration.columnOriented) return ColumnOrientedAggregateSeparatorSlab(this, 0).secondDimension
        else return RowOrientedAggregateSeparatorSlab(this, 0).secondDimension
    }

    override fun getElementSeparatorFirstDimension(): Int{
        if(parentConfiguration.columnOriented) return ColumnOrientedElementSeparatorSlab(this, 0).firstDimension
        else return RowOrientedElementSeparatorSlab(this, 0).firstDimension
    }

    override fun getElementSeparatorSecondDimension(): Int{
        if(parentConfiguration.columnOriented) return ColumnOrientedElementSeparatorSlab(this, 0).secondDimension
        else return RowOrientedElementSeparatorSlab(this, 0).secondDimension
    }

    override val id: String = UUID.randomUUID().toString()

    override fun getAggregateHeightWithGaps(columnOriented: Boolean): Int =
        if(columnOriented) this.sumBy { it.height } + this.size * Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP
        else get(0).height + Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP

    override fun getAggregateWidthWithGaps(columnOriented: Boolean): Int =
        if(columnOriented) get(0).width + 2 * Config.BETWEEN_ELEMENTS_VERTICAL_GAP
        else  this.sumBy { it.width } + (this.size + 1) * Config.BETWEEN_ELEMENTS_VERTICAL_GAP

    override fun add(element: Element): Boolean {
        addChild(element.id, element)
        return arrayDefaultDelegate.add(element)
    }

    override fun addAll(elements: Collection<Element>): Boolean {
        elements.forEach { this.addChild(it.id, it) }
        return arrayDefaultDelegate.addAll(elements)
    }

    override fun add(index: Int, element: Element) {
        addChild(element.id, element)
        return arrayDefaultDelegate.add(index, element)
    }

    override fun remove(element: Element): Boolean {
        removeChild(element.id)
        return arrayDefaultDelegate.remove(element)
    }

    override fun listOfSlabs(): List<Slab> {
        val separators = mutableListOf<Slab>()
        for( i in 0 until size - 1) separators.add(
                if(parentConfiguration.columnOriented) ColumnOrientedElementSeparatorSlab(this, i)
                else RowOrientedElementSeparatorSlab(this, i)
        )
        return separators
    }


    init{
        addAll(elements)
        slabTreeDefaultDelegate.actualSlabTree = this
    }

    override fun toString(): String = this.map { it.toString() }.reduce { acc, s -> acc + s + ", " }

    fun filledWithRepeatingDefault(times: Int, builder: ()->Element) : Aggregate{
        for(i in 1..times) add(builder())
        return this
    }
}

abstract class DynProFrontConfiguration(private val parentProject: Project, override var parentFurniture: Furniture, private val slabTreeDefaultDelegate: SlabTree = DefaultSlabTree()):
        FrontConfiguration, TypedFactoryChooser<FrontElemFactory> by DefaultFactoryChooser(), SlabTree by slabTreeDefaultDelegate {

    abstract protected var aggregates: ArrayList<ArrangementAggregate>

    private val factoriesChain = AllFrontElementFactoriesChain()

    override fun listOfSlabs(): List<Slab>{
        val separators = mutableListOf<Slab>()
        for( i in 0 until aggregates.size - 1) separators.add(
                if(columnOriented) ColumnOrientedAggregateSeparatorSlab(aggregates[i], i)
                else RowOrientedAggregateSeparatorSlab(aggregates[i], i)
        )
        return separators
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
        removeAggregateIfEmpty(aggregate)
        keepOneNonFixedElemInAggregate(aggregate)
        keepOneNonBlockedOneElemAggregate()
        if(aggregates.size == 0) addToAggregates(Aggregate(defaultElementBuilder(), parentConfiguration = this))
        recalculateElementsDimens()
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    private fun addToAggregates(aggregate: ArrangementAggregate){
        aggregates.add(aggregate)
        addChild(aggregate.id, aggregate)
    }

    private fun addToAggregates(index: Int, aggregate: ArrangementAggregate){
        aggregates.add(index, aggregate)
        addChild(aggregate.id, aggregate)
    }

    private fun removeAggregate(aggregate: ArrangementAggregate){
        aggregates.remove(aggregate)
        removeChild(aggregate.id)
    }

    override fun getBlockedHeight(): Int {
            return if (columnOriented){
                (aggregates.map {
                    val (blocked, nonBlocked) = it.partition{ it.blockedHeight }
                    blocked .sumBy { it.height + Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP } + nonBlocked.size * Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP + nonBlocked.size
                }.max()?:0)
            }else {
                val( blocked, nonBlocked) = aggregates.partition{ it[0].blockedHeight}
               blocked.map { it[0].height + Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP}.sum() + nonBlocked.size * Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP + nonBlocked.size
            }
    }

    override fun getBlockedWidth(): Int{
        return if(columnOriented){
            val(blocked, nonBlocked) = aggregates.partition{ it[0]. blockedWidth}
            blocked.map { it[0].width + Config.BETWEEN_ELEMENTS_VERTICAL_GAP}.sum()  + nonBlocked.size * Config.BETWEEN_ELEMENTS_VERTICAL_GAP + nonBlocked.size  + Config.BETWEEN_ELEMENTS_VERTICAL_GAP
        }
        else{
            (aggregates.map{
                val (blocked, nonBlocked) = it.partition{it.blockedWidth}
                blocked.sumBy { it.width + Config.BETWEEN_ELEMENTS_VERTICAL_GAP}   + nonBlocked.size * Config.BETWEEN_ELEMENTS_VERTICAL_GAP + nonBlocked.size
            }.max() ?: 0) + Config.BETWEEN_ELEMENTS_VERTICAL_GAP
        }
    }

    private fun removeAggregateIfEmpty(aggregate: ArrangementAggregate){
        if(aggregate.size == 0)
            removeAggregate(aggregate)
    }

    private fun keepOneNonBlockedOneElemAggregate(){
        if ((aggregates.size == 1) ){
            val aggregate = aggregates[0]
            if(aggregate.size == 1) {
                aggregate[0].blockedHeight = false
                aggregate[0].blockedWidth = false
            }
        }
    }

    private fun keepOneNonFixedElemInAggregate(aggregate: ArrangementAggregate){
        if(aggregate.size == 1) {
            if(!columnOriented) aggregate[0].blockedWidth = false
            else aggregate[0].blockedHeight = false
        }
    }

    private fun newElemWithCopiedBlocks(aggregate: ArrangementAggregate): Element{
        val newElem = defaultElementBuilder()
        if(columnOriented)newElem.blockedWidth = aggregate[0].blockedWidth
        else newElem.blockedHeight = aggregate[0].blockedHeight // responsibility of a front configuration object, since it knows how to set blocking properties of its children
        if(columnOriented) newElem.width = aggregate[0].width
        else newElem.height = aggregate[0].height
        return newElem
    }

    private fun onAddOneElement(elementId:String, indexModifier: (Int) -> Int){
        val aggregate = aggregateContainingElementWithId(elementId)
        val indexOfBenchmarkElement = indexOfElementWithId(elementId)
        val newElem = newElemWithCopiedBlocks(aggregate)
        aggregate.add(indexModifier(indexOfBenchmarkElement), newElem)
        recalculateElementsDimens()
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    private fun onAddAggregate(elementId: String, repeated: Boolean, indexModifier: (Int) -> Int){
        val indexOfAggregate: Int = aggregateIndexContainingElementWithId(elementId)
        val newAggregate =
                if(!repeated) Aggregate(defaultElementBuilder(), parentConfiguration =  this)
                else Aggregate(parentConfiguration = this).filledWithRepeatingDefault(
                        times = maxElementsNumberInAggregates(),
                        builder = defaultElementBuilder
                )
        addToAggregates(indexModifier(indexOfAggregate), newAggregate)
        recalculateElementsDimens()
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
        aggregates = defaultsAddedToChildren()
        this.columnOriented = columnOriented
        recalculateElementsDimens()
        parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun changeTypeOfElem(elementId: String, newType: String): Element{
        val frontElemIndex = indexOfElementWithId(elementId)
        val aggregate = aggregateContainingElementWithId(elementId)
        return changeType(newType, aggregate, frontElemIndex, {frontElemFactory, element -> frontElemFactory.createFrontElem(element)}, factoriesChain)
    }

    private fun propagate(element: Element, propagation: (Element) -> Unit){
        aggregateContainingElementWithId(element.id).forEach { propagation(it) }
    }

    override fun recalculateElementsDimens(){
        if(columnOriented) recalculateColumnOrientedConfig()
        else recalculateRowOrientedConfig()
    }

    private fun recalculateColumnOrientedConfig(){
        for(column in aggregates){
            val nonBlockedHeightElements = column.filter { !it.blockedHeight }
            val remainingHeight = getRemainingColumnOrientedHeight(column)
            nonBlockedHeightElements.forEach{ it -> it.height = remainingHeight / nonBlockedHeightElements.size}
        }
        refreshAllColumnsWidth()
    }

    private fun recalculateRowOrientedConfig(){
        for (row in aggregates){
            val nonBlockedWidthElements = row.filter { !it.blockedWidth }
            val remainingWidth = getRemainingRowOrientedWidth(row)
            nonBlockedWidthElements.forEach { it -> it.width = remainingWidth / nonBlockedWidthElements.size }
        }
        refreshAllRowsHeight()
    }

    private fun refreshAllColumnsWidth(){
        val remainingWidth = getRemainingColumnOrientedWidth()
        val (blockedWidthColumns, nonBlockedWidthColumns) = aggregates.partition { it[0].blockedWidth }
        blockedWidthColumns.forEach { agg -> propagate(agg[0]){it.width = agg[0].width} }
        nonBlockedWidthColumns.forEach { propagate(it[0]) { it.width = remainingWidth / nonBlockedWidthColumns.size} }
    }

    private fun refreshAllRowsHeight(){
        val remainingHeight = getRemainingRowOrientedHeight()
        val (blockedHeightRows, nonBlockedHeightRows) = aggregates.partition { it[0].blockedHeight }
        blockedHeightRows.forEach { agg -> propagate(agg[0]){it.height = agg[0].height} }
        nonBlockedHeightRows.forEach { propagate(it[0]){it.height = remainingHeight / nonBlockedHeightRows.size} }
    }

    override fun propagateAggragateSpecificDimen(elementId:String){
        if(columnOriented) propagateColumnWidthHavingelemId(elementId)
        else propagateRowHeightHavingElemId(elementId)
    }

    private fun propagateColumnWidthHavingelemId(elementId: String){
        val element = fetchElementWithId(elementId)
        propagate(element) { it.width = element.width }
    }

    private fun propagateRowHeightHavingElemId(elementId: String){
        val element = fetchElementWithId(elementId)
        propagate(element) { it.height = element.height }

    }

    override fun canBlockWidth(elementId: String) : Boolean{
        return if(columnOriented) aggregates.filter { !it[0].blockedWidth and (!(fetchElementWithId(elementId) in (it)))}.size >= 1
        else aggregateContainingElementWithId(elementId).filter { !it.blockedWidth and (it.id != elementId) }. size >= 1
    }

    override fun canBlockHeight(elementId: String): Boolean{
        return if (columnOriented) aggregateContainingElementWithId(elementId).filter { !it.blockedHeight and (it.id != elementId) }.size >= 1
        else aggregates.filter { !it[0].blockedHeight and (!(fetchElementWithId(elementId) in (it)))}.size >= 1
    }

    override fun getMaxDimensionOf(elementId: String): Dimension {
        val element = fetchElementWithId(elementId)
        val currentWidth = if (element.blockedWidth) element.width else 0
        val currentHeight= if(element.blockedHeight)element.height else 0
        val dw = if(columnOriented) getRemainingColumnOrientedWidth() else getRemainingRowOrientedWidth(aggregateContainingElementWithId(elementId))
        val dh = if(columnOriented) getRemainingColumnOrientedHeight(aggregateContainingElementWithId(elementId)) else getRemainingRowOrientedHeight()
        return Dimension(currentWidth + dw, currentHeight + dh)

    }

    private fun getRemainingRowOrientedHeight(): Int{
        val blockedHeightRows = aggregates.filter { it[0].blockedHeight }
        return parentFurniture.height - (aggregates.size) * Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP - blockedHeightRows.sumBy { it[0].height } - parentFurniture.pedestalHeight
    }


    private fun getRemainingColumnOrientedWidth(): Int{
        val blockedWidthColumns = aggregates.filter { it[0].blockedWidth }
        return parentFurniture.width - (aggregates.size + 1) * Config.BETWEEN_ELEMENTS_VERTICAL_GAP - blockedWidthColumns.sumBy { it[0].width }
    }


    private fun getRemainingColumnOrientedHeight(column: ArrangementAggregate) : Int{
        val blockedHeightElements = column.filter { it.blockedHeight }
        return parentFurniture.height - (column.size) * Config.BETWEEN_ELEMENTS_HORIZONTAL_GAP - blockedHeightElements.sumBy { it.height } - parentFurniture.pedestalHeight

    }

    private fun getRemainingRowOrientedWidth(row: ArrangementAggregate): Int {
        val blockedWidthElements = row.filter { it.blockedWidth }
        return parentFurniture.width - (row.size + 1) * Config.BETWEEN_ELEMENTS_VERTICAL_GAP - blockedWidthElements.sumBy { it.width }
    }


    override fun propagateAggregateSpecificBlock(elementId: String, propertyBlocked: Boolean){
        if(columnOriented) propagateBlockedWidth(elementId, propertyBlocked)
        else propagateBlockedHeight(elementId, propertyBlocked)
    }

    private fun propagateBlockedWidth(elementId: String, widthBlocked: Boolean){
        val element = fetchElementWithId(elementId)
        propagate(element){it.blockedWidth = widthBlocked}
    }

    private fun propagateBlockedHeight(elementId: String, heightBlocked: Boolean){
        val element = fetchElementWithId(elementId)
        propagate(element){it.blockedHeight = heightBlocked}
    }

    override fun isElemWithIdLastToTheLeft(elementId: String) : Boolean =
            if(columnOriented) aggregateIndexContainingElementWithId(elementId) == 0
            else indexOfElementWithId(elementId) == 0

    override fun isElemWithIdLastToTheRight(elementId: String) : Boolean =
            if(columnOriented) aggregateIndexContainingElementWithId(elementId) == aggregates.size - 1
            else indexOfElementWithId(elementId) == aggregateContainingElementWithId(elementId).size - 1

    override fun isElemWithIdLastToTheTop(elementId: String) : Boolean =
        if(columnOriented) indexOfElementWithId(elementId) == 0
        else aggregateIndexContainingElementWithId(elementId) == 0

    override fun isElemWithIdLastToTheBottom(elementId: String) : Boolean =
            if(columnOriented) indexOfElementWithId(elementId) == aggregateContainingElementWithId(elementId).size - 1
            else aggregateIndexContainingElementWithId(elementId) == aggregates.size - 1

    protected fun defaultsAddedToChildren() : ArrayList<ArrangementAggregate>{
        resetChildren()
        val res = getDefaultAggregates()
        res.forEach{addChild(it.id, it)}
        return res

    }
    init {
        slabTreeDefaultDelegate.actualSlabTree = this
    }
}

class UpperModuleFrontConfiguration(parentProject: Project, parentFurniture: Furniture): DynProFrontConfiguration(parentProject, parentFurniture){

    override var columnOriented: Boolean = true
    override val defaultElementBuilder = { Drawer(parentConfig = this) }

    override var aggregates: ArrayList<ArrangementAggregate> by Delegates.observable(defaultsAddedToChildren()){
        _, _, _ -> parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun getDefaultAggregates(): ArrayList<ArrangementAggregate> = arrayListOf(Aggregate(LeftDoor(parentConfig = this), parentConfiguration = this), Aggregate(RightDoor(parentConfig = this), parentConfiguration = this))

}

class BottomModuleFrontConfiguration(parentProject: Project, parentFurniture: Furniture): DynProFrontConfiguration(parentProject, parentFurniture){

    override var columnOriented: Boolean = false
    override val defaultElementBuilder = { Drawer(parentConfig = this) }

    override var aggregates: ArrayList<ArrangementAggregate> by Delegates.observable(defaultsAddedToChildren()){
        _, _, _ -> parentProject.presenter?.onFrontConfigurationChanged(parentFurniture.name)
    }

    override fun getDefaultAggregates(): ArrayList<ArrangementAggregate> = arrayListOf(Aggregate(Drawer(parentConfig = this), parentConfiguration =  this), Aggregate(Drawer(parentConfig = this), parentConfiguration = this))
}