package com.sagar.logutil.checqk.model

data class SearchSortDTO (
    var addtionalSearch : ArrayList<SearchField>?,
    var search : ArrayList<SearchField>?,
    var defaultSort : SortField?,
    var sort : ArrayList<SortField>?,
    var length : Long?,
    var start : Long?,
    var status : Boolean?,
    var statusCode : Long?
)