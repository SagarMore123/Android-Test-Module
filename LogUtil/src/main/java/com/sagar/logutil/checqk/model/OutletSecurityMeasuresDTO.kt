package com.astrika.checqk.model

import android.net.Uri

class OutletSecurityMeasuresDTO {
    var outletImageDTO: OutletImageDTO? = null
    var secMeasuresDTOs: ArrayList<SafetyMeasuresDetailsDTO>? = null
}

class OutletImageDTO {
    var outletId: Long = 0
    var documentGetDto = ImageDTO()
}

class SafetyMeasuresDetailsDTO {
    var outletId: Long = 0
    var outletSecMeasuresDetailImage = ImageDTO()
    var outletSecurityMeasuresId: Long = 0
    var title = ""
    var imageUri: Uri? = null

    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass) {
            return false
        }

        other as SafetyMeasuresDetailsDTO

        if (outletSecurityMeasuresId != other.outletSecurityMeasuresId) {
            return false
        }

        if (outletId != other.outletId) {
            return false
        }

        if (outletSecMeasuresDetailImage != other.outletSecMeasuresDetailImage) {
            return false
        }

        if (title != other.title) {
            return false
        }

        if (imageUri != other.imageUri) {
            return false
        }

        return true
    }

    override fun hashCode(): Int {
        var result = outletId.hashCode()
        result = 31 * result + (outletSecMeasuresDetailImage?.hashCode() ?: 0)
        result = 31 * result + outletSecurityMeasuresId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (imageUri?.hashCode() ?: 0)
        return result
    }
}


