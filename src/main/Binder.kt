package main


abstract class Binder {

    interface OnChange{ fun onChange(value:Any)}
    private val registrars = mutableMapOf<String,OnChange>()
    private val orderedRegistrars = mutableListOf<String>()

    open fun registerSubscriber(id:String, onChange: OnChange) {
        registrars.put(id, onChange)
        if(id !in orderedRegistrars) orderedRegistrars.add(id)
    }

    fun unregisterSubscriber(id: String){
        registrars.remove(id)
        orderedRegistrars.remove(id)
    }

    fun notifyChange(value: Any) {
        orderedRegistrars.forEach{registrars.get(it)?.onChange(value)}
    }
}