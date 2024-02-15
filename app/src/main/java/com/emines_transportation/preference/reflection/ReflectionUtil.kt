package com.emines_transportation.preference.reflection

import com.google.gson.Gson
import org.json.JSONObject

/*Reflection util class contains util method for api*/
data class ReflectionUtil(
    private val gson: Gson
) {

    fun <T> parseJson(json: String, classType: Class<T>): T {
        return gson.fromJson(json, classType)
    }

    fun parseClassToJson(classType: Any): String {
        return gson.toJson(classType)
    }

    fun <T> parseJsonToClass(json: String, classType: Class<T>): T {
        return gson.fromJson(json, classType)
    }

    fun getErrorMsgFromJsonObject(string: String): String {
        val jObjError = JSONObject(
            string
        )
        return jObjError.getString("message")
    }

}

