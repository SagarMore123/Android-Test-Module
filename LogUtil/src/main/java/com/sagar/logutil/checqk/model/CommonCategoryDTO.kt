package com.sagar.logutil.checqk.model

import android.net.Uri
import androidx.databinding.ObservableBoolean

data class CommonCategoryDTO(
    val galleryImageCategoryList: ArrayList<GalleryImageCategory>,
    val catalogueImageCategoryList: ArrayList<CatalogueImageCategory>,
    val search: ArrayList<CommonSearchDTO>,
    val success: Success,
    var sort: ArrayList<CommonSortDTO>? = null,
    val totalCount: Int,
    val error: Error
)

class GalleryImageCategory {
    var galleryImageCategoryId: Long = 0
    var galleryImageCategoryName: String? = null
    var outletId: Long = 0
    var id: Long = 0
    var active: Boolean? = true
    var uriImagesList: ArrayList<Uri>? = null
    var documentGetDtos: ArrayList<ImageDTO>? = null
    var showChecked = ObservableBoolean(false)
}

class CatalogueImageCategory {
    var catalogueImageCategoryId: Long = 0
    var catalogueImageCategoryName: String ?= null
    var outletId: Long = 0
    var id:Long = 0
    var active: Boolean? = true
    var uriImagesList : ArrayList<Uri> ?= null
    var documentGetDtos:ArrayList<ImageDTO> ?= null
    var showChecked = ObservableBoolean(false)
}

