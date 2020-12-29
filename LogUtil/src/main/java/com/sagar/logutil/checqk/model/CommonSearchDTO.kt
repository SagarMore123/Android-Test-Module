package com.astrika.checqk.model


class CommonListingDTO {

    var start: Int = 0
//    var length: Int = Constants.PAGINATION_PAGE_DATA_COUNT
    var length: Int = 0
    var addtionalSearch = ArrayList<CommonSearchDTO>()
    var search = ArrayList<CommonSearchDTO>()
    var status = true
    var sort: ArrayList<CommonSortDTO>? = null
    var defaultSort = CommonSortDTO()
    var statusCode = 0
    var requestType = 0
}

class CommonSearchDTO {
    var search = ""
    var searchCol = ""
}

class CommonSortDTO {
    var sortField = ""
    var sortOrder = ""
}

