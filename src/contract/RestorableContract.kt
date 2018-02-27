package contract

import model.*


data class ProjectSave(val projectName: String, val furnitureList: List<FurnitureSave>)

interface RestorableProject{
    fun restore(projectSave: ProjectSave)
    fun saveState(project: Project): ProjectSave
}

class DefaultRestorableProject: RestorableProject{
    override fun restore(projectSave: ProjectSave) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
    fun restore(furnitureSave: FurnitureSave)
    fun saveState(furniture: Furniture): FurnitureSave
}


class DefaultRestorableFurniture: RestorableFurniture{
    override fun restore(furnitureSave: FurnitureSave) {
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
    fun restore(frontConfigSave: FrontConfigSave)
    fun saveState(frontConfiguration: FrontConfiguration, aggregates: List<ArrangementAggregate>): FrontConfigSave
}

class DefaultRestorableFrontConfig: RestorableFrontConfig {
    override fun restore(frontConfigSave: FrontConfigSave) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveState(frontConfiguration: FrontConfiguration, aggregates: List<ArrangementAggregate>): FrontConfigSave =
            FrontConfigSave(
                    frontConfiguration.columnOriented,
                    aggregates.map{ it.savedState() }
            )
}


data class AggregateSave(val id: String, val elements: List<ElementSave>)

interface RestorableAggregate{
    fun restore(aggregateSave: AggregateSave)
    fun saveState(arrangementAggregate: ArrangementAggregate):AggregateSave
}

class DefaultRestorableAggregate: RestorableAggregate {
    override fun restore(aggregateSave: AggregateSave) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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


