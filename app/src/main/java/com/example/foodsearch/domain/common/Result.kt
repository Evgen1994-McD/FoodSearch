package com.example.foodsearch.domain.common

/**
 * Результат операции, который может быть успешным или содержать ошибку
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    
    inline fun <R> map(transform: (T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this
        }
    }
    
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }
    
    inline fun onError(action: (Throwable) -> Unit): Result<T> {
        if (this is Error) action(exception)
        return this
    }
}

/**
 * Типы ошибок приложения
 */
sealed class AppError : Throwable() {
    object NetworkError : AppError()
    object DatabaseError : AppError()
    object NotFoundError : AppError()
    object ValidationError : AppError()
    data class UnknownError(val originalCause: Throwable) : AppError()
}

/**
 * Обертка для безопасного выполнения операций
 */
inline fun <T> safeCall(call: () -> T): Result<T> {
    return try {
        Result.Success(call())
    } catch (e: Exception) {
        Result.Error(e)
    }
}

/**
 * Обертка для безопасного выполнения suspend операций
 */
suspend inline fun <T> safeSuspendCall(crossinline call: suspend () -> T): Result<T> {
    return try {
        Result.Success(call())
    } catch (e: Exception) {
        Result.Error(e)
    }
}
