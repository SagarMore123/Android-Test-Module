package com.sagar.logutil.checqk.source

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.reflect.TypeToken
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.network.NetworkController
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.network.network_utils.NetworkResponseCallback
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomGsonBuilder
import org.json.JSONObject

class DashboardRemoteDataSource : DashboardDataSource {

    companion object {
        private var instance: DashboardRemoteDataSource? = null
        private var networkController: NetworkController? = null
        private var mContext: Context? = null
        private lateinit var sharedPreferences: SharedPreferences

        @JvmStatic
        fun getInstance(context: Context?): DashboardDataSource? {
            mContext = context
            networkController = NetworkController.getInstance(mContext!!)
            sharedPreferences =
                Constants.getSharedPreferences(context!!.applicationContext)
            if (instance == null) {
                instance = DashboardRemoteDataSource()
            }
            return instance
        }

    }


    override fun getAllMenuCategories(outletId: Long,callback: IDataSourceCallback<GetCatalogueCategoriesResponseDTO>) {
        networkController?.getAllMenuCategories(outletId, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response : GetCatalogueCategoriesResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            GetCatalogueCategoriesResponseDTO::class.java
                        )

                        if (response.success != null) {
                            callback.onDataFound(response)
                           /* if (response.activemenuCategoryDTOs.isNotEmpty()) {
                            } else {
                                callback.onDataNotFound()
                            }*/
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }

                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }


            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveMenuCategories(
        menuCategory: CategoryCatalogueDTO,
        callback: IDataSourceCallback<Long>
    ) {
        networkController?.saveMenuCategory(menuCategory, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if(data.isNotBlank()){
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if (response.success != null) {
                            callback.onDataFound(response.catalogueCategoryID?:0)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }else{
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }

            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveMenuCategoriesSequence(
        menuCategoryList: ArrayList<CategoryCatalogueDTO>,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveMenuCategoriesSequence(menuCategoryList, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                networkController?.commonResponseString(data, callback)
               /* try {
                    if(data.isNotBlank()){
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if (response.success != null) {
                            callback.onDataFound(response.success.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }else{
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }*/

            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun changeMenuCategoryStatus(catalogueCategoryId: Long, status: Boolean, callback: IDataSourceCallback<String>) {
        networkController?.changeMenuCategoryStatus(catalogueCategoryId, status, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                networkController?.commonResponseString(data, callback)
                /*try {
                    if(data.isNotBlank()){
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if (response.success != null) {
                            callback.onDataFound(response.success.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }else{
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }*/

            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getAllMenuSections(catalogueCategoryId: Long, outletId: Long, callback: IDataSourceCallback<GetMenuSectionsResponseDTO>) {
        networkController?.getAllMenuSections(catalogueCategoryId,outletId, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response : GetMenuSectionsResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            GetMenuSectionsResponseDTO::class.java
                        )

                        if (response.success != null) {
                            callback.onDataFound(response)

                            /* if (response.activeMenuSectionDTOs.isNotEmpty()) {
                                 callback.onDataFound(response.activeMenuSectionDTOs)
                             } else {
                                 callback.onDataNotFound()
                             }*/
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }

                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveMenuSection(
        catalogueSection: CatalogueSectionDTO,
        callback: IDataSourceCallback<Long>
    ) {
        networkController?.saveMenuSection(catalogueSection, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if(data.isNotBlank()){
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if (response.success != null) {
                            callback.onDataFound(response.catalogueSectionId?:0)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }else{
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }

            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveMenuSectionsSequence(
        catalogueSectionList: ArrayList<CatalogueSectionDTO>,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveMenuSectionsSequence(catalogueSectionList, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                networkController?.commonResponseString(data, callback)
              /*  try {
                    if(data.isNotBlank()){
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if (response.success != null) {
                            callback.onDataFound(response.success.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }else{
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }*/

            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun changeMenuSectionStatus(
        catalogueSectionId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.changeMenuSectionStatus(catalogueSectionId, status, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                networkController?.commonResponseString(data, callback)
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getAllDishes(
        catalogueCategoryId: Long,
        outletId: Long,
        productName: String,
        callback: IDataSourceCallback<ArrayList<ProductWithSectionDetails>>
    ) {
        networkController?.getAllDishes(catalogueCategoryId, outletId, productName, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response : GetDishListResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            GetDishListResponseDTO::class.java
                        )

                        if (response.success != null) {
                            if (response.productSectionListingDTOs.isNotEmpty()) {
                                callback.onDataFound(response.productSectionListingDTOs)
                            } else {
                                callback.onDataNotFound()
                            }
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getAllDishFlags(callback: IDataSourceCallback<ArrayList<ProductFlagDTO>>) {
        networkController?.getAllDishFlag(object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response : GetDishFlagResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            GetDishFlagResponseDTO::class.java
                        )

                        if (response.success != null) {
                            if (response.productFlagList.isNotEmpty()) {
                                callback.onDataFound(response.productFlagList)
                            } else {
                                callback.onDataNotFound()
                            }
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveDish(productDetailsDTO: ProductDetailsDTO, callback: IDataSourceCallback<Long>) {
        networkController?.saveDish(productDetailsDTO, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if(data.isNotBlank()){
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if (response.success != null) {
                            callback.onDataFound(response.id!!)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }else{
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }

            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveDishTiming(
        dishTimingRequestDTO: DishTimingRequestDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveDishTiming(dishTimingRequestDTO, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if(data.isNotBlank()){
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if (response.success != null) {
                            callback.onDataFound(response.success.message!!)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }else{
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }

            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun changeDishStatus(
        productId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.changeDishStatus(productId, status, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                networkController?.commonResponseString(data, callback)
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getDishDetails(productId: Long, callback: IDataSourceCallback<ProductDetailsDTO>) {
        networkController?.getDishDetails(productId, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if(data.isNotBlank()){
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: DishDetailsResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            DishDetailsResponseDTO::class.java
                        )
                        if (response.success != null) {
                            callback.onDataFound(response.productDTO!!)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }else{
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }

            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getDishTiming(
        productId: Long,
        callback: IDataSourceCallback<ArrayList<DishTimingDTO>>
    ) {
        networkController?.getDishTiming(productId, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if(data.isNotBlank()){
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: DishTimingResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            DishTimingResponseDTO::class.java
                        )
                        if (response.success != null) {
                            callback.onDataFound(response.timings!!)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }else{
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }

            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getInactiveDishes(
        catalogueCategoryId: Long,
        catalogueSectionId: Long,
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<ProductDetailsDTO>>
    ) {
        networkController?.getInactiveDishes(catalogueCategoryId, catalogueSectionId, outletId, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response : GetInactiveDishListResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            GetInactiveDishListResponseDTO::class.java
                        )

                        if (response.success != null) {
                            if (response.inactiveProductList.isNotEmpty()) {
                                callback.onDataFound(response.inactiveProductList)
                            } else {
                                callback.onDataNotFound()
                            }
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveDishSequence(
        productList: ArrayList<ProductDetailsDTO>,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveDishSequence(productList, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                networkController?.commonResponseString(data, callback)
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getStaffListing(
        commonListingDTO : CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<UserDTO>>
    ) {
        networkController?.getStaffListing(commonListingDTO, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response : StaffListingResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            StaffListingResponseDTO::class.java
                        )

                        if (response.success != null) {
                            if (response.userMasterDTOs.isNullOrEmpty()) {
                                callback.onDataNotFound()
                            } else {
                                callback.onDataFound(response.userMasterDTOs!!)
                            }
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveStaffUser(staffUserDTO: UserDTO, callback: IDataSourceCallback<String>) {
        networkController?.saveStaffUser(staffUserDTO, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        /*val gson = CustomGsonBuilder().getInstance().create()
                        val response : CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )*/

                        if (jsonObject.has("success") && jsonObject.getString("success") != null) {
                            callback.onDataFound(jsonObject.getString("success"))
                        } else if (jsonObject.has("error") && jsonObject.getString("error") != null) {
                            callback.onError(jsonObject.getString("error"))
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                       /* if(jsonObject.has("success")){

                        }*/
//                        callback.onDataFound("Staff save successfully")
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getStaffUserDetails(userId: Long, callback: IDataSourceCallback<UserDTO>) {
        networkController?.getStaffUserDetails(userId, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                      val gson = CustomGsonBuilder().getInstance().create()
                        /*  val response : CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )*/

                        if (jsonObject.has("userMasterDTO")) {
                            val userDetails = jsonObject.get("userMasterDTO")
                            val response : UserDTO = gson.fromJson(
                                userDetails.toString(),
                                UserDTO::class.java
                            )
                            callback.onDataFound(response)
                        } /*else if (response.error != null) {
                            callback.onError(response.error.message)
                        }*/ else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getStaffSafetyReadings(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<StaffSafetyReadingDTO>>
    ) {
        networkController?.getStaffSafetyReadings(commonListingDTO, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                         val gson = CustomGsonBuilder().getInstance().create()
                        val response : GetStaffSafetyReadingResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            GetStaffSafetyReadingResponseDTO::class.java
                        )

                        if (response.success != null) {
                            if (response.staffSecurityMeasuresList.isNullOrEmpty()) {
                                callback.onDataNotFound()
                            } else {
                                callback.onDataFound(response.staffSecurityMeasuresList?: arrayListOf())
                            }
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveStaffSafetyReadings(
        staffSafetyReadingRequestDTO: StaffSafetyReadingRequestDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveStaffSafetyReadings(staffSafetyReadingRequestDTO, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response : CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        if (response.success != null) {
                            callback.onDataFound(response.success.message!!)

                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getAccessRolesListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<AccessRoleDTO>>
    ) {
        networkController?.getAccessRoleListing(commonListingDTO, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                          val response : GetAccessRolesResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                              GetAccessRolesResponseDTO::class.java
                        )

                        if (response.success != null) {
                            if (response.list.isNullOrEmpty()) {
                                callback.onDataNotFound()
                            } else {
                                callback.onDataFound(response.list!!)
                            }
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getApplicationModules(callback: IDataSourceCallback<ArrayList<PermissionDTO>>) {
        networkController?.getApplicationModules( object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response : GetApplicationModulesResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            GetApplicationModulesResponseDTO::class.java
                        )

                        if (response.success != null) {
                            if (response.modules.isNullOrEmpty()) {
                                callback.onDataNotFound()
                            } else {
                                callback.onDataFound(response.modules!!)
                            }
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveAccessRole(
        accessRoleDTO: AccessRoleDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveAccessRole(accessRoleDTO, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                networkController?.commonResponseString(data, callback)
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getAccessRoleDetails(
        accessRoleId: Long,
        callback: IDataSourceCallback<AccessRoleDTO>
    ) {
        networkController?.getAccessRoleDetails(accessRoleId, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (!data.isNullOrBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response : GetAccessRolesDetailsResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            GetAccessRolesDetailsResponseDTO::class.java
                        )

                        if (response.success != null) {
                            if (response.groupRoleDto == null) {
                                callback.onDataNotFound()
                            } else {
                                callback.onDataFound(response.groupRoleDto!!)
                            }
                        } else if (response.error != null) {
                            callback.onError(response.error.message)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                } catch (e: Exception) {
                    callback.onError("Error")
                }
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun changeAccessRoleStatus(
        accessRoleId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.changeAccessRoleStatus(accessRoleId, status, object : NetworkResponseCallback{
              override fun onSuccess(data: String) {
                  networkController?.commonResponseString(data, callback)
              }

              override fun onError(errorCode: Int, errorData: String) {
                  callback.onError(errorData)
              }
          })
    }

    override fun getReservedTableListing(commonListingDTO: CommonListingDTO,callback: IDataSourceCallback<ArrayList<BookingDTO>>) {
        networkController?.getReservedTableListing(commonListingDTO,object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if (response.success != null) {
                            if (response.success.status == "200") {
                                when {
                                    jsonObject.has("BookingList") -> {
                                        val bookingListJsonArray =
                                            jsonObject.getJSONArray("BookingList")
                                        val bookingList: ArrayList<BookingDTO> =
                                            gson.fromJson(
                                                bookingListJsonArray.toString(),
                                                object :
                                                    TypeToken<ArrayList<BookingDTO?>>() {}.type
                                            )
                                        callback.onDataFound(bookingList)
                                    }
                                    response.error?.message != null -> {
                                        callback.onError(response.error.message)
                                    }
                                    else -> {
                                        callback.onError(NetworkController.SERVER_ERROR)
                                    }
                                }
                            }
//                            callback.onDataFound(data)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    } else {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError(NetworkController.SERVER_ERROR)
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getReservedTableListingByOutletId(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<TableManagementDTO>>
    ) {
        networkController?.getTableListingByOutletId(outletId, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if(jsonObject != null){
                            if(jsonObject.has("tableDTOList")){
                                val tableListJsonArray =
                                    jsonObject.getJSONArray("tableDTOList")
                                val tableList: ArrayList<TableManagementDTO> =
                                    gson.fromJson(
                                        tableListJsonArray.toString(),
                                        object :
                                            TypeToken<ArrayList<TableManagementDTO?>>() {}.type
                                    )
                                callback.onDataFound(tableList)
                            }else{
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        }
                        /*if (response.success != null) {
                            if (response.success.status == "200") {
                                when {
                                    jsonObject.has("tableDTOList") -> {
                                        val tableListJsonArray =
                                            jsonObject.getJSONArray("tableDTOList")
                                        val tableList: ArrayList<TableManagementDTO> =
                                            gson.fromJson(
                                                tableListJsonArray.toString(),
                                                object :
                                                    TypeToken<ArrayList<TableManagementDTO?>>() {}.type
                                            )
                                        callback.onDataFound(tableList)
                                    }
                                    response.error?.message != null -> {
                                        callback.onError(response.error.message)
                                    }
                                    else -> {
                                        callback.onError(NetworkController.SERVER_ERROR)
                                    }
                                }
                            }
//                            callback.onDataFound(data)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }*/
                    } else {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError(NetworkController.SERVER_ERROR)
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun assignTable(
        bookingId: Long,
        status: String,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.assignTable(bookingId,status, object : NetworkResponseCallback{
            override fun onSuccess(data: String) {
                networkController?.commonResponseString(data, callback)
            }
            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun getTableListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<ArrayList<TableManagementDTO>>
    ) {
        networkController?.getTableListing(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val response: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        if (response.success != null) {
                            if (response.success.status == "200") {
                                when {
                                    jsonObject.has("TableList") -> {
                                        val tableListJsonArray =
                                            jsonObject.getJSONArray("TableList")
                                        val tableList: ArrayList<TableManagementDTO> =
                                            gson.fromJson(
                                                tableListJsonArray.toString(),
                                                object :
                                                    TypeToken<ArrayList<TableManagementDTO?>>() {}.type
                                            )
                                        callback.onDataFound(tableList)
                                    }
                                    response.error?.message != null -> {
                                        callback.onError(response.error.message)
                                    }
                                    else -> {
                                        callback.onError(NetworkController.SERVER_ERROR)
                                    }
                                }
                            }
//                            callback.onDataFound(data)
                        } else {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    } else {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    callback.onError(NetworkController.SERVER_ERROR)
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun saveTableListing(
        tableListManagementDTO: ArrayList<TableManagementDTO>,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveTableListing(
            tableListManagementDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    networkController?.commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

}