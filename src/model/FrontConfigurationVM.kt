package model

class FrontConfigurationVM(val furnitureName: String, val columnOriented: Boolean, frontConfiguration: List<ArrangementAggregate>, private val imgPathMapper: Map<String, String>){

    val columns: List<ConfigurationAggregateVM> = fillColumns(frontConfiguration)

    private fun fillColumns(configuration: List<ArrangementAggregate>): List<ConfigurationAggregateVM>
            = configuration.map {
                arrangementAggregate ->
                    ConfigurationAggregateVM().addAll_(
                            arrangementAggregate.map {
                                element -> ConfigurationElementVM(element.name, imgPathMapper[element.type]!!, element.id)
                            }
                    )
            }
}

class ConfigurationAggregateVM : MutableList<ConfigurationElementVM> by ArrayList<ConfigurationElementVM>(){
    fun addAll_(elements: Collection<ConfigurationElementVM>): ConfigurationAggregateVM {
        addAll(elements)
        return this
    }
}

class ConfigurationElementVM(val name: String, val imagePath: String, val modelElementKey: Any)