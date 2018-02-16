package contract

interface TypedFactory{
    fun typeCorrect(type : String): Boolean
}

interface FactoriesChain <T: TypedFactory>{
    fun getChain():List<T>
}


interface TypedFactoryChooser<T: TypedFactory>{
    fun chooseFactoryTo(type: String, factoryAction: (factory: T) -> Unit, factoriesChain: FactoriesChain<T>)
    fun <E>changeType(newType: String, list: MutableList<E>, index: Int, changeAction: (T, E) -> E, factoriesChain: FactoriesChain<T>) : E
}

class DefaultFactoryChooser<T : TypedFactory>: TypedFactoryChooser<T> {

    @Suppress("UNCHECKED_CAST")
    override fun <E> changeType(newType: String, list: MutableList<E>, index: Int, changeAction: (T, E)->E, factoriesChain: FactoriesChain<T>) : E {
        val oldEntity = list.removeAt(index)
        var newEntity : E? = null
        chooseFactoryTo(newType, {newEntity = changeAction(findFactory(newType, factoriesChain), oldEntity)}, factoriesChain)
        list.add(index, newEntity!!)
        return newEntity as E
    }

    private fun findFactory(newType: String, factoriesChain: FactoriesChain<T>): T = factoriesChain.getChain().find { it.typeCorrect(newType) }!!

    override fun chooseFactoryTo(type: String, factoryAction: (T) -> Unit, factoriesChain: FactoriesChain<T>) {

        for (factory in factoriesChain.getChain()){
            if (factory.typeCorrect(type))
                factoryAction(factory)
        }
    }
}