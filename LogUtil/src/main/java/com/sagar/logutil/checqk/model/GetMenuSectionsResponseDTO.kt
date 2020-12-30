package com.sagar.logutil.checqk.model

data class GetMenuSectionsResponseDTO(
    val success: SuccessDTO?,
    val error: ErrorDTO?,
    val inActiveCatalogueSectionDTOs :  ArrayList<CatalogueSectionDTO>,
    val activeCatalogueSectionDTOs :  ArrayList<CatalogueSectionDTO>
)