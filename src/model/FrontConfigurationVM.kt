package model

class FrontConfigurationVM(val furnitureName: String, frontConfiguration: List<ArrangementColumn>, private val imgPathMapper: Map<String, String>){

    val columns: List<ConfigurationColumnVM> = fillColumns(frontConfiguration)

    private fun fillColumns(configuration: List<ArrangementColumn>): List<ConfigurationColumnVM>
            = configuration.map {
                arrangementColumn ->
                    ConfigurationColumnVM().addAll_(
                            arrangementColumn.map {
                                element -> ConfigurationElementVM(element.name, imgPathMapper[element.type]!!, element.id)
                            }
                    )
            }
}

class ConfigurationColumnVM : MutableList<ConfigurationElementVM> by ArrayList<ConfigurationElementVM>(){
    fun addAll_(elements: Collection<ConfigurationElementVM>): ConfigurationColumnVM {
        addAll(elements)
        return this
    }
}

class ConfigurationElementVM(val name: String, val imagePath: String, val modelElementKey: Any)