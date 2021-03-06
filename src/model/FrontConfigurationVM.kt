package model

import config.Config

class FrontConfigurationVM(val furnitureName: String, val columnOriented: Boolean, frontConfiguration: List<ArrangementAggregate>, private val imgPathMapper: Map<String, String>, val furnitureWidth: Int, val furnitureHeight: Int){

    val columns: List<ConfigurationAggregateVM> = fillColumns(frontConfiguration)

    private fun fillColumns(configuration: List<ArrangementAggregate>): List<ConfigurationAggregateVM>
            = configuration.map {
                arrangementAggregate ->
                    ConfigurationAggregateVM().addAll_(
                            arrangementAggregate.map {
                                element ->
                                    ConfigurationElementVM(
                                            element.name,
                                            imgPathMapper[element.type]!!,
                                            element.id,
                                            mountInitialTooltip(element),
                                            element.parentConfig.canBlockWidth(element.id),
                                            element.parentConfig.canBlockHeight(element.id),
                                            (element.width.toDouble()) / element.parentConfig.parentFurniture.width,
                                            (element.height.toDouble()) / element.parentConfig.parentFurniture.height
                                    )
                            }
                    )
            }

    val maxHeight: Double = 2500.0
    val maxWidth : Double = 2500.0
    val maxElementsAmount = columns.maxBy { it.size }?.size ?: 0

    private fun mountInitialTooltip(element: Element) : String =
            String.format(
                    Config.FRONT_CONFIG_ELEMENT_TIP_FORMAT,
                    element.name,
                    element.type,
                    element.width,
                    element.height,
                    if(element.blockedWidth) Config.YES_PL else Config.NO_PL,
                    if(element.blockedHeight) Config.YES_PL else Config.NO_PL,
                    if(element.growthRingVerticallyOriented) Config.YES_PL else Config.NO_PL,
                    element.shelvesNumber
            )
}



class ConfigurationAggregateVM : MutableList<ConfigurationElementVM> by ArrayList<ConfigurationElementVM>(){
    fun addAll_(elements: Collection<ConfigurationElementVM>): ConfigurationAggregateVM {
        addAll(elements)
        return this
    }
}

class ConfigurationElementVM(val name: String, val imagePath: String, val modelElementKey: Any, val tooltip: String, val canBlockWidth: Boolean, val canBlockHeight: Boolean, val widthX: Double, val widthY: Double)