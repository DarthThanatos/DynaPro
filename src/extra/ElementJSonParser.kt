package extra

import com.beust.klaxon.JsonObject
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import org.json.JSONObject
import java.io.File
import java.io.StringReader

data class ObjString(val name : String?, val age: Int?, val flag: Boolean?, val array:List<Any>, val jsobj: JsonObject?)

fun jsonParse1(){
    val objectString = """{
         "name" : "Joe",
         "age" : 23,
         "flag" : true,
         "array" : [1, 3],
         "obj1" : { "a" : 1, "b" : 2 }
    }"""
    var yo: ObjString? = null
    val jr = JsonReader(StringReader(objectString))
    jr.use { reader ->
        reader.beginObject() {
            var name: String? = null
            var age: Int? = null
            var flag: Boolean? = null
            var array: List<Any> = arrayListOf<Any>()
            var obj1: JsonObject? = null
            while (reader.hasNext()) {
                val readName = reader.nextName()
                when (readName) {
                    "name" -> name = reader.nextString()
                    "age" -> age = reader.nextInt()
                    "flag" -> flag = reader.nextBoolean()
                    "array" -> array = reader.nextArray()
                    "obj1" -> obj1 = reader.nextObject()
                    else -> null
                }
            }
            yo = ObjString(name, age,flag, array, obj1)
        }
    }
    println(yo)
}

data class Book(val title: String)
data class Person(val name: String, val age: Int, val books: List<Book>)

fun jsonParse2(){
    val result = Klaxon().parse<Person>(file = File("src//extra//person.json"))
    println(result)
    val tofile = JSONObject(Klaxon().toJsonString(result!!)).toString(4)
    println(tofile)
    File("src//extra//out.json").writeText(tofile)

}

fun retNul(): Any?{
    return null
}

fun main(args:Array<String>){
    jsonParse2()
    if(retNul() is String) println("String") else println("null")
}
