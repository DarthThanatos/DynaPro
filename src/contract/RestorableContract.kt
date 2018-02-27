package contract

import config.Config
import model.*


data class ProjectSave(val projectName: String, val furnitureList: List<FurnitureSave>)

interface RestorableProject{
    fun restore(projectSave: ProjectSave, project: Project)
    fun saveState(project: Project): ProjectSave
}

class DefaultRestorableProject: RestorableProject, TypedFactoryChooser<FurnitureFactory> by DefaultFactoryChooser(){
    override fun restore(projectSave: ProjectSave, project: Project) {
        project.reset(Config.NEW_PROJECT_PL, project.presenter!!)
        project.addChildrenFurnitures(projectSave.furnitureList.map {
            furnitureSave ->
                var res : Furniture? = null
                chooseFactoryTo(
                        furnitureSave.type,
                        {
                            res = it.createFurnitureChild(furnitureSave)

                        },
                        project.factoriesChain
                )
                res!!
        })
    }

    override fun saveState(project: Project): ProjectSave =
            ProjectSave(
                    project.getName(),
                    project.furnituresList.map { it.savedState() }
            )
}


data class FurnitureSave(
        val name: String,
        val type: String,
        val frontElemUnitPrice: Int,
        val elemUnitPrice : Int,
        val height: Int,
        val width: Int,
        val depth: Int,
        val backInserted: Boolean,
        val roofInserted: Boolean,
        val hasPedestal: Boolean,
        val pedestalHeight: Int,
        val frontConfig:  FrontConfigSave
)


interface RestorableFurniture{
    fun restore(furnitureSave: FurnitureSave) : Furniture
    fun saveState(furniture: Furniture): FurnitureSave
}


class DefaultRestorableFurniture: RestorableFurniture{
    override fun restore(furnitureSave: FurnitureSave) : Furniture {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveState(furniture: Furniture): FurnitureSave =
            FurnitureSave(
                    furniture.name,
                    furniture.type,
                    furniture.frontUnitPrice,
                    furniture.elementUnitPrice,
                    furniture.height,
                    furniture.width,
                    furniture.depth,
                    furniture.backInserted,
                    furniture.roofInserted,
                    furniture.hasPedestal,
                    furniture.pedestalHeight,
                    furniture.frontConfiguration.savedState()
            )

}


data class FrontConfigSave(val columnOriented: Boolean, val aggregates: List<AggregateSave>)

interface RestorableFrontConfig{
    fun restore(frontConfigSave: FrontConfigSave, frontConfiguration: FrontConfiguration)
    fun saveState(frontConfiguration: FrontConfiguration, aggregates: List<ArrangementAggregate>): FrontConfigSave
}

class DefaultRestorableFrontConfig: RestorableFrontConfig {
    override fun restore(frontConfigSave: FrontConfigSave, frontConfiguration: FrontConfiguration) {
        val aggregates : ArrayList<ArrangementAggregate> = arrayListOf()
        frontConfigSave.aggregates.forEach{
                val res = Aggregate(parentConfiguration = frontConfiguration)
                res.restoreState(it)
                aggregates.add(res)
        }
        frontConfiguration.setAggregatesTo(aggregates)
        frontConfiguration.columnOriented = frontConfigSave.columnOriented
        frontConfiguration.recalculateElementsDimens()
    }

    override fun saveState(frontConfiguration: FrontConfiguration, aggregates: List<ArrangementAggregate>): FrontConfigSave =
            FrontConfigSave(
                    frontConfiguration.columnOriented,
                    aggregates.map{ it.savedState() }
            )
}


data class AggregateSave(val id: String, val elements: List<ElementSave>)

interface RestorableAggregate{
    fun restore(aggregateSave: AggregateSave, aggregate: ArrangementAggregate)
    fun saveState(arrangementAggregate: ArrangementAggregate):AggregateSave
}

class DefaultRestorableAggregate: RestorableAggregate, TypedFactoryChooser<FrontElemFactory> by DefaultFactoryChooser() {
    override fun restore(aggregateSave: AggregateSave, aggregate: ArrangementAggregate) {
        aggregate.setElementsTo(
                aggregateSave.elements.map{
                    elementSave ->
                    var element: Element? = null
                    chooseFactoryTo(elementSave.type, {element = it.createFrontElem(elementSave, aggregate.parentConfiguration)}, aggregate.parentConfiguration.factoriesChain)
                    element!!
                }
        )
    }

    override fun saveState(arrangementAggregate: ArrangementAggregate): AggregateSave =
            AggregateSave(
                    arrangementAggregate.id,
                    arrangementAggregate.map { it.savedState() }
            )
}

data class ElementSave(
        val name: String,
        val id : String,
        val type: String,
        val height: Int,
        val width: Int,
        val blockedWidth: Boolean,
        val blockedHeight: Boolean,
        val growthRingVerticallyOriented: Boolean,
        val shelvesNumber: Int
)

interface RestorableElement{
    fun restore(elementSave: ElementSave)
    fun saveState(element: Element): ElementSave
}

class DefaultRestorableElement: RestorableElement {
    override fun restore(elementSave: ElementSave) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveState(element: Element): ElementSave =
            ElementSave(
                    element.name,
                    element.id,
                    element.type,
                    element.height,
                    element.width,
                    element.blockedWidth,
                    element.blockedHeight,
                    element.growthRingVerticallyOriented,
                    element.shelvesNumber
            )
}


