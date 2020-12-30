package com.sagar.logutil.checqk.master_controller.source

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sagar.logutil.checqk.model.ImageDTO

class ImageDTOConverter {

    /**
     * Convert a image object to a Json
     */
    @TypeConverter
    fun fromImageToString(image: ImageDTO?): String {
        if (image == null)
            return ""
        return Gson().toJson(image)
    }

    /**
     * Convert a json to a Image
     */
    @TypeConverter
    fun fromStringToImage(jsonImage: String): ImageDTO? {
        if (jsonImage == "")
            return null
        val notesType = object : TypeToken<ImageDTO>() {}.type
        return Gson().fromJson<ImageDTO>(jsonImage, notesType)
    }
}