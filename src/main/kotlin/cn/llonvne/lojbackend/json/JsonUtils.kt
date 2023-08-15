package cn.llonvne.lojbackend.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

sealed interface JsonScope

object JsonScopeImpl : JsonScope

inline fun <reified Type> json(block: JsonScope.() -> Type): Type = JsonScopeImpl.block()

context(JsonScope)
val Any.jsonValue: String
    get() = jacksonObjectMapper().writeValueAsString(this)

context (JsonScope)
inline fun <reified Type> String.type(): Type =
    jacksonObjectMapper().readValue(this, Type::class.java)



