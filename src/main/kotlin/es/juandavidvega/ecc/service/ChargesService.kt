package es.juandavidvega.ecc.service

import es.juandavidvega.ecc.service.Result
import es.juandavidvega.ecc.storage.ChargesStorage
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.io.File
import java.util.*

class ChargesService (private val chargesStorage: ChargesStorage) {
    suspend fun import(multipartData: MultiPartData): Result {
        var fileDescription = ""
        var fileName = "${Date().time}-"
        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    fileDescription = part.value
                }
                is PartData.FileItem -> {
                    fileName += part.originalFileName as String
                    val fileBytes = part.streamProvider().readBytes()
                    File(fileName).writeBytes(fileBytes)
                }
                else -> {}
            }
        }




        return Result.SUCCESS
    }
}
