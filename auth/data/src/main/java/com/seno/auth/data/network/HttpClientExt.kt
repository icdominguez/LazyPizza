package com.seno.auth.data.network

import com.seno.auth.data.BuildConfig
import com.seno.core.domain.DataError
import com.seno.core.domain.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import java.net.SocketException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified Response : Any> HttpClient.get(
    route: String,
    queryParameter: Map<String, Any?> = mapOf()
): Result<Response, DataError.Network> =
    safeCall {
        get {
            url(constructRoute(route))
            queryParameter.forEach { (key, value) ->
                parameter(key, value)
            }
        }
    }

suspend inline fun <reified Request, reified Response : Any> HttpClient.post(
    route: String,
    body: Request,
    configure: HttpRequestBuilder.() -> Unit = {}
): Result<Response, DataError.Network> =
    safeCall {
        post {
            url(constructRoute(route))
            setBody(body)
            configure()
        }
    }

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Network> {
    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: UnknownHostException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.SERIALIZATION)
    } catch (e: SocketException) {
        e.printStackTrace()
        return Result.Error(DataError.Network.NO_INTERNET)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        e.printStackTrace()
        return Result.Error(DataError.Network.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Network> =
    when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(DataError.Network.SERIALIZATION)
            }
        }

        400 -> Result.Error(DataError.Network.BAD_REQUEST)
        401 -> Result.Error(DataError.Network.UNAUTHORIZED)
        403 -> Result.Error(DataError.Network.FORBIDDEN)
        404 -> Result.Error(DataError.Network.NOT_FOUND)
        408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
        409 -> Result.Error(DataError.Network.CONFLICT)
        413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
        429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
        503 -> Result.Error(DataError.Network.SERVICE_UNAVAILABLE)
        in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
        else -> Result.Error(DataError.Network.UNKNOWN)
    }

fun constructRoute(route: String): String =
    when {
        route.contains(BuildConfig.TELEGRAM_BASE_URL) -> route
        route.startsWith("/") -> BuildConfig.TELEGRAM_BASE_URL + route
        else -> BuildConfig.TELEGRAM_BASE_URL + "/$route"
    }
