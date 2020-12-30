package com.sagar.logutil.checqk.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sagar.logutil.databinding.BasicInfoFacilityMastersItemCellLayoutBinding

class FacilityBindingConverter {

    /**
     * Convert a image object to a Json
     */
    @TypeConverter
    fun fromImageSubCategoryJson(binding: BasicInfoFacilityMastersItemCellLayoutBinding?): String {
        if (binding == null)
            return ""
        return Gson().toJson(binding)
    }

    /**
     * Convert a json to a Image
     */
    @TypeConverter
    fun toImageSubCategory(jsonBinding: String): BasicInfoFacilityMastersItemCellLayoutBinding? {
        if (jsonBinding == "")
            return null
        val notesType = object : TypeToken<BasicInfoFacilityMastersItemCellLayoutBinding>() {}.type
        return Gson().fromJson<BasicInfoFacilityMastersItemCellLayoutBinding>(jsonBinding, notesType)
    }
}