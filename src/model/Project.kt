package model



interface Project{
    fun getName(): String
    fun rename(newProjectName: String)
    val factoriesChain: FactoriesChain
    val furnituresList: List<Furniture>
    fun addDefaultFurniture() : Boolean
    fun addChildFurniture(childName: String, childFurnitureType: String) : Boolean
    fun removeChildFurniture(oldChildName: String)
    fun renameChildFurniture(oldChildName: String, newChildName: String) : Boolean
    fun getFurnitureByName(name: String) : Furniture?
    fun isNameMine(potentialName: String): Boolean
}

class DynProject(private var name: String = Config.NEW_PROJECT_PL) : Project {

    override fun isNameMine(potentialName: String): Boolean = name == potentialName

    override fun getName(): String = name

    override fun rename(newProjectName: String) {
        val oldProjectName = name
        name = if((newProjectName != "") and (getFurnitureByName(newProjectName)  == null)) newProjectName else oldProjectName
    }

    override val furnituresList: MutableList<Furniture> = mutableListOf()

    override val factoriesChain: FactoriesChain = AllFurnitureTypesChain()

    override fun getFurnitureByName(name: String) :Furniture? = furnituresList.singleOrNull() { it.name == name }

    private fun pickDefaultName(): String{
        for (i in 1..Config.MAX_DEFAULT_FURNITURE_NAMES_NUMBER){
            if (furnitureNotExist(Config.DEFAULT_FURNITURE_NAME_PREFIX_PL + i))
                return Config.DEFAULT_FURNITURE_NAME_PREFIX_PL + i
        }
        return ""
    }

    private fun keepAtLeastOneFurniture(){
        if(furnituresList.size == 0)
            addDefaultFurniture()
    }

    override fun addDefaultFurniture(): Boolean =
        addChildFurniture(
                childName = pickDefaultName(),
                childFurnitureType = Config.UPPER_MODULE
        )


    override fun addChildFurniture(childName: String, childFurnitureType: String) : Boolean{
        if(furnitureNotExist( childName)){
            for (factory: FurnitureFactory in factoriesChain.getChain()){
                if (factory.typeCorrect(childFurnitureType))
                    furnituresList.add(factory.createFurnitureChild(childName))
            }
            return true
        }
        return false
    }

    override fun removeChildFurniture(oldChildName: String) {
        furnituresList.removeIf{it.name == oldChildName}
        keepAtLeastOneFurniture()
    }


    private fun furnitureNotExist(furnitureName: String) = furnituresList.count { it.name == furnitureName } == 0

    override fun renameChildFurniture(oldChildName: String, newChildName: String) :Boolean{
        if (furnitureNotExist(newChildName) and (newChildName != name) and (newChildName != "")){
            furnituresList.single { it.name == oldChildName }.name = newChildName
            return true
        }
        return false
    }


    init {
        addChildFurniture(childName = Config.NEW_UPPER_MODULE_PL, childFurnitureType = Config.UPPER_MODULE)
    }

}

interface FactoriesChain{
    fun getChain():List<FurnitureFactory>
}

class AllFurnitureTypesChain: FactoriesChain{
    override fun getChain(): List<FurnitureFactory> = listOf(UpperModulesFactory(), BottomModulesFactory())

}

interface FurnitureFactory{
    fun typeCorrect(type: String): Boolean
    fun  createFurnitureChild(name:String): Furniture
}

class UpperModulesFactory: FurnitureFactory{
    override fun typeCorrect(type: String): Boolean = type == Config.UPPER_MODULE

    override fun createFurnitureChild(name: String): Furniture = UpperModule(name)

}

class BottomModulesFactory: FurnitureFactory {
    override fun typeCorrect(type: String): Boolean = type == Config.BOTTOM_MODULE

    override fun createFurnitureChild(name: String): Furniture = BottomModule(name)

}

interface Furniture{
    val type: String
    var name: String
    val height: Int
    val width: Int
    val depth: Int
    val frontUnitPrice: Int
    val elementUnitPrice: Int
    fun getElements(): List<Element>
}


class UpperModule(override var name: String): Furniture {

    override val type: String = Config.UPPER_MODULE

    override val frontUnitPrice: Int = 100

    override val elementUnitPrice: Int = 115


    override val height: Int = 200

    override val width: Int = 50

    override val depth: Int = 75


    override fun getElements(): List<Element> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class BottomModule(override var name:String): Furniture{


    override val type: String = Config.BOTTOM_MODULE

    override val frontUnitPrice: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val elementUnitPrice: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.


    override val height: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val width: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val depth: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.


    override fun getElements(): List<Element> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

interface Element

