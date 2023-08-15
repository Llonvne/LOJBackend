package cn.llonvne.lojbackend

/**
 * LojBackend 内部 API
 * 该注解用于标记一个属于 LOJBackend 内部的 API 类,函数，在正常情况下，请勿使用任何被注解的元素
 * 该方法可能被不经任何通知的修改，删除
 *
 * 如果你确认你需要使用，请使用 OptIn 注解来局部使用，方式如下
 * ```kotlin
 * @OptIn(LojInternalApi::class)
 * fun useInternalApi(){
 *  internalApi()
 * }
 * ```
 * @see OptIn
 */
@RequiresOptIn(
    "这是一个属于 LOJBackend 内部的 API 类,函数，在正常情况下，请勿使用任何被注解的元素,该方法可能被不经任何通知的修改，删除",
    RequiresOptIn.Level.ERROR
)
annotation class LojInternalApi()

