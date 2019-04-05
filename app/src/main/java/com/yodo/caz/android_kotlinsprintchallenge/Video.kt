package com.yodo.caz.android_kotlinsprintchallenge

import kotlinx.serialization.Serializable

@Serializable
data class VideoFile(
    val file_url: String
)

@Serializable
data class VideoStream(
    val video_files: List<VideoFile>
)

{
    fun getUrl():String {
        return video_files.first().file_url
    }
}