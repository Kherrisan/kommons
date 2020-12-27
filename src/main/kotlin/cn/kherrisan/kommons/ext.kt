package cn.kherrisan.kommons

import com.google.gson.*
import org.dom4j.Document
import org.dom4j.DocumentHelper
import java.lang.Exception
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*


/**
 * 20 % { print ("你有20%的概率看到这条信息") }
 */
inline operator fun Int.rem(randomBlock: () -> Unit) {
    if (Random(System.currentTimeMillis()).nextInt(100) < this) randomBlock()
}

/**
 * xxxx.xx() shouldBe 666
 */
infix fun Any?.shouldBe(expected: Any?) = assert(this?.equals(expected) ?: false)

/**
 * 在测试时，需要打印出某个对象
 * obj.println()
 * 而不是
 * println(obj)
 */
fun <T> T?.println() = println(this)

/**
 * json["data"]["user"]["username"]
 */
operator fun JsonElement.get(key: String): JsonElement = asJsonObject[key]

/**
 * json["data"][id-1]["username"]
 */
operator fun JsonElement.get(index: Int): JsonElement = asJsonArray[index]

operator fun JsonElement.set(key: String, value: Number) {
    asJsonObject.addProperty(key, value)
}

operator fun JsonElement.set(key: String, value: String) {
    asJsonObject.addProperty(key, value)
}

operator fun JsonElement.set(key: String, value: Boolean) {
    asJsonObject.addProperty(key, value)
}

operator fun JsonElement.set(key: String, value: Char) {
    asJsonObject.addProperty(key, value)
}

fun String.toJson() = JsonParser.parseString(this)

fun Map<Any, Any>.toJson() = Gson().toJson(this)

//fun List<Any>.toPropertyMap(): List<Any> {
//
//}

class DateAdapter : JsonSerializer<Date>, JsonDeserializer<Date> {
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(sdf.format(src))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date {
        return sdf.parse(json!!.asString)
    }
}

fun Any.toJson(): String {
    val gb = GsonBuilder()
//    gb.registerTypeAdapter(Date::class.java, DateAdapter())
    return gb.create().toJson(this)
}

fun JsonElement.toDoc(): Any? {
    val map = mutableMapOf<String, Any?>()
    return when {
        isJsonObject -> {
            asJsonObject.keySet().forEach { key ->
                map[key] = (asJsonObject[key] as JsonElement).toDoc()
            }
            map
        }
        isJsonArray -> {
            asJsonArray.map { (it as JsonElement).toDoc() }
        }
        isJsonPrimitive -> {
            val p = asJsonPrimitive
            when {
                p.isBoolean -> asBoolean
                p.isString -> asString
                p.isNumber -> asNumber
                else -> null
            }
        }
        isJsonNull -> null
        else -> null
    }
}

fun Any.toDoc(): Any? {
    val gb = GsonBuilder()
    gb.registerTypeAdapter(Date::class.java, DateAdapter())
    return gb.create().toJsonTree(this).toDoc()
}

class B {
    val b = "111"
}

class C {
    val b = B()
    val mmm = "MMM"
}

class A {
    val a = 1
    val b = "Hello"
    val oo = B()
    val c = listOf<Any>(B(), C())
}

fun main() {
    println(A().toJson())
    println(A().toDoc())
}

fun String.xml(): Document {
    return DocumentHelper.parseText(this)
}

fun String.date(format: String): Date {
    return SimpleDateFormat(format, Locale.ENGLISH).parse(this)
}