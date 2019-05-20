package com.nacarseven.feelings.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FeelingResponse(

    @Json(name = "documentSentiment")
    val sentimentDoc: Sentiment,

    @Json(name = "sentences")
    val sentences: List<Sentence>

)

@JsonClass(generateAdapter = true)
data class Sentence(

    val sentiment: Sentiment,

    val text: Text
)

@JsonClass(generateAdapter = true)
data class Sentiment(

    val magnitude: Double,

    val score: Double

)

@JsonClass(generateAdapter = true)
data class Text(

    val content: String,

    val beginOffset: Double

)


@JsonClass(generateAdapter = true)
data class FeelingData(

    @Json(name = "document")
    val document: DocumentData,

    val encodingType: String

)

@JsonClass(generateAdapter = true)
data class DocumentData(

    val type: String,

    val language: String,

    val content: String

)

