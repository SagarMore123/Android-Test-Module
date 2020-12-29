package com.astrika.checqk.model

data class GetCatalogueCategoriesResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val activeCatalogueCategoryDTOs :  ArrayList<CategoryCatalogueDTO>,
    val inActiveCatalogueCategoryDTOs :  ArrayList<CategoryCatalogueDTO>
)