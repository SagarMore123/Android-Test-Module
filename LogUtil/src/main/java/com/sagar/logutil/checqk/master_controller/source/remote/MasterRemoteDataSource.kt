package com.sagar.logutil.checqk.master_controller.source.remote

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.reflect.TypeToken
import com.sagar.logutil.checqk.master_controller.source.MasterDataSource
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.*
import com.sagar.logutil.checqk.network.NetworkController
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.network.network_utils.NetworkResponseCallback
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomGsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class MasterRemoteDataSource : MasterDataSource() {

    companion object {
        private var instance: MasterRemoteDataSource? = null
        private var networkController: NetworkController? = null
        private var mContext: Context? = null
        private lateinit var sharedPreferences: SharedPreferences


        @JvmStatic
        fun getInstance(context: Context?): MasterDataSource? {
            mContext = context
            if (context != null) {
                networkController = NetworkController.getInstance(context)
                sharedPreferences = Constants.getSharedPreferences(context.applicationContext)
            }

            if (instance == null) {
                instance = MasterRemoteDataSource()
            }
            return instance
        }
    }


    override fun loginMasters(loginDTO: LoginDTO, callback: IDataSourceCallback<String>) {
        networkController?.loginMasters(loginDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        val jsonObject = JSONObject(data)
                        var accessToken = ""
                        if (jsonObject.has("access_token") && !jsonObject.getString("access_token")
                                .isNullOrBlank()
                        ) {
                            accessToken = jsonObject.getString("access_token")
                            NetworkController.accessToken = "Bearer $accessToken"
                            sharedPreferences.edit()?.putString(
                                Constants.ACCESS_TOKEN,
                                Constants.encrypt(accessToken)
                            )?.apply()
                            callback.onDataFound("success")
                        } else {
                            if (jsonObject.has("error")) {
                                val error = jsonObject.optJSONObject("error")
                                val errorMessage = error.getString("message")
                                callback.onError(errorMessage)
                            }
                        }
                    } catch (e: Exception) {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }

        })
    }


    // System Value Master
    override fun fetchSystemValueMasterDataRemote(callback: IDataSourceCallback<List<SystemValueMasterDTO>>) {

        networkController?.fetchSVM(object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {

                    val jsonArray = JSONArray(data)
                    val gSon = CustomGsonBuilder().getInstance().create()

                    val svmList: List<SystemValueMasterDTO> =
                        gSon.fromJson<List<SystemValueMasterDTO>>(
                            jsonArray.toString(),
                            object : TypeToken<List<SystemValueMasterDTO?>?>() {}.type
                        )
                    if (!svmList.isNullOrEmpty()) {
                        callback.onDataFound(svmList)
                    } else {
                        callback.onDataNotFound()
                    }

                } catch (e: java.lang.Exception) {

                    callback.onError(NetworkController.SERVER_ERROR)
                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })

    }


    override fun fetchMealTypeMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<MealTypeMasterDTO>>
    ) {

        networkController?.fetchMealTypes(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {

                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("MealTypeList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("MealTypeList")

                                val arrayList: ArrayList<MealTypeMasterDTO> =
                                    gSon.fromJson<ArrayList<MealTypeMasterDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<MealTypeMasterDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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

    override fun fetchOutletTypeDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<OutletTypeMasterDTO>>
    ) {

        networkController?.fetchOutLetTypes(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {

                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("OutletTypeList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("OutletTypeList")

                                val arrayList: ArrayList<OutletTypeMasterDTO> =
                                    gSon.fromJson<ArrayList<OutletTypeMasterDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<OutletTypeMasterDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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


    // Skills Master
    override fun fetchCuisineMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<CuisineMasterDTO>>
    ) {

        networkController?.fetchCuisines(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {

                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("CuisineList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("CuisineList")

                                val arrayList: ArrayList<CuisineMasterDTO> =
                                    gSon.fromJson<ArrayList<CuisineMasterDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<CuisineMasterDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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

    // Food Type Master
    override fun fetchFoodTypeMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<FoodTypeMasterDTO>>
    ) {
        networkController?.fetchFoodTypes(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {

                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("FoodTypeList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("FoodTypeList")

                                val arrayList: ArrayList<FoodTypeMasterDTO> =
                                    gSon.fromJson<ArrayList<FoodTypeMasterDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<FoodTypeMasterDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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


    // Facility
    override fun fetchFacilityMastersRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<FacilityMasterDTO>>
    ) {

        networkController?.fetchFacilities(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {


                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("FacilityList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("FacilityList")

                                val arrayList: ArrayList<FacilityMasterDTO> =
                                    gSon.fromJson<ArrayList<FacilityMasterDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<FacilityMasterDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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


    // Country
    override fun fetchCountryMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<CountryMasterDTO>>
    ) {

        networkController?.fetchCountries(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {


                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("CountryList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("CountryList")

                                val arrayList: ArrayList<CountryMasterDTO> =
                                    gSon.fromJson<ArrayList<CountryMasterDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<CountryMasterDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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


    // State
    override fun fetchStateMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<StateMasterDTO>>
    ) {

        networkController?.fetchStates(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {


                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("StateList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("StateList")

                                val arrayList: ArrayList<StateMasterDTO> =
                                    gSon.fromJson<ArrayList<StateMasterDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<StateMasterDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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


    // City
    override fun fetchCityMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<CityMasterDTO>>
    ) {

        networkController?.fetchCities(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {


                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("CityList") -> {

                                val jsonArray = jsonObject.optJSONArray("CityList")

                                val arrayList: ArrayList<CityMasterDTO> =
                                    gSon.fromJson<ArrayList<CityMasterDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<CityMasterDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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


    // Area
    override fun fetchAreaMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<AreaMasterDTO>>
    ) {

        networkController?.fetchAreas(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {


                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("AreaList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("AreaList")

                                val arrayList: ArrayList<AreaMasterDTO> =
                                    gSon.fromJson<ArrayList<AreaMasterDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<AreaMasterDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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


    // Social Media
    override fun fetchSocialMediaMastersRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<SocialMediaMasterDTO>>
    ) {

        networkController?.fetchSocialMedia(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {


                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("SocialMediaMasterList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("SocialMediaMasterList")

                                val arrayList: ArrayList<SocialMediaMasterDTO> =
                                    gSon.fromJson<ArrayList<SocialMediaMasterDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<SocialMediaMasterDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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


    // Discount Category
    override fun fetchDiscountCategoryMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<OutletDiscountCategoryDTO>>
    ) {

        networkController?.fetchDiscountCategories(
            commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {

                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gSon = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {

                                commonResponseDTO.error != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }

                                jsonObject.has("DiscountCategoryList") -> {

                                    val jsonArray =
                                        jsonObject.optJSONArray("DiscountCategoryList")

                                    val arrayList: ArrayList<OutletDiscountCategoryDTO> =
                                        gSon.fromJson<ArrayList<OutletDiscountCategoryDTO>>(
                                            jsonArray?.toString(),
                                            object :
                                                TypeToken<ArrayList<OutletDiscountCategoryDTO?>?>() {}.type
                                        )
                                    if (!arrayList.isNullOrEmpty()) {
                                        callback.onDataFound(arrayList)
                                    } else {
                                        callback.onDataNotFound()
                                    }
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
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


    // Discount Membership Plan
    override fun fetchDiscountMembershipPlanMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<OutletDiscountMembershipPlanDTO>>
    ) {

        networkController?.fetchDiscountMembershipPlan(
            commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {

                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gSon = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {

                                commonResponseDTO.error != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }

                                jsonObject.has("list") -> {

                                    val jsonArray =
                                        jsonObject.optJSONArray("list")

                                    val arrayList: ArrayList<OutletDiscountMembershipPlanDTO> =
                                        gSon.fromJson<ArrayList<OutletDiscountMembershipPlanDTO>>(
                                            jsonArray?.toString(),
                                            object :
                                                TypeToken<ArrayList<OutletDiscountMembershipPlanDTO?>?>() {}.type
                                        )
                                    if (!arrayList.isNullOrEmpty()) {
                                        callback.onDataFound(arrayList)
                                    } else {
                                        callback.onDataNotFound()
                                    }
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
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

    // Cravx Cards Discount Membership Holder
    override fun fetchCravxCardDiscountMembershipHolderMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<CravxCardsMembershipHolderDTO>>
    ) {

        networkController?.fetchCravxCardsDiscountMembershipHolderMasters(
            commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {

                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gSon = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {

                                commonResponseDTO.error != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }

                                jsonObject.has("CravxCardList") -> {

                                    val jsonArray =
                                        jsonObject.optJSONArray("CravxCardList")

                                    val arrayList: ArrayList<CravxCardsMembershipHolderDTO> =
                                        gSon.fromJson<ArrayList<CravxCardsMembershipHolderDTO>>(
                                            jsonArray?.toString(),
                                            object :
                                                TypeToken<ArrayList<CravxCardsMembershipHolderDTO?>?>() {}.type
                                        )
                                    if (!arrayList.isNullOrEmpty()) {
                                        callback.onDataFound(arrayList)
                                    } else {
                                        callback.onDataNotFound()
                                    }
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
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


    // HW Discount Membership Holder
    override fun fetchHWDiscountMembershipHolderMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<HWMembershipHolderDTO>>
    ) {

        networkController?.fetchHWDiscountMembershipHolderMasters(
            commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {

                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gSon = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {

                                commonResponseDTO.error != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }

                                jsonObject.has("HWCardList") -> {

                                    val jsonArray =
                                        jsonObject.optJSONArray("HWCardList")

                                    val arrayList: ArrayList<HWMembershipHolderDTO> =
                                        gSon.fromJson<ArrayList<HWMembershipHolderDTO>>(
                                            jsonArray?.toString(),
                                            object :
                                                TypeToken<ArrayList<HWMembershipHolderDTO?>?>() {}.type
                                        )
                                    if (!arrayList.isNullOrEmpty()) {
                                        callback.onDataFound(arrayList)
                                    } else {
                                        callback.onDataNotFound()
                                    }
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
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

    // In-House Discount Membership Holder
    override fun fetchInHouseDiscountMembershipHolderMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<InHouseMembershipHolderDTO>>
    ) {

        networkController?.fetchInHouseDiscountMembershipHolderMasters(
            commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {

                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gSon = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {

                                commonResponseDTO.error != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }

                                jsonObject.has("InHouseCardList") -> {

                                    val jsonArray =
                                        jsonObject.optJSONArray("InHouseCardList")

                                    val arrayList: ArrayList<InHouseMembershipHolderDTO> =
                                        gSon.fromJson<ArrayList<InHouseMembershipHolderDTO>>(
                                            jsonArray?.toString(),
                                            object :
                                                TypeToken<ArrayList<InHouseMembershipHolderDTO?>?>() {}.type
                                        )
                                    if (!arrayList.isNullOrEmpty()) {
                                        callback.onDataFound(arrayList)
                                    } else {
                                        callback.onDataNotFound()
                                    }
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
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

    override fun fetchDashboardDrawerMasterDataRemote(callback: IDataSourceCallback<List<DashboardDrawerDTO>>) {

        networkController?.getDrawerMenu(object : NetworkResponseCallback {
            override fun onSuccess(data: String) {

                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("modules") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("modules")

                                val arrayList: ArrayList<DashboardDrawerDTO> =
                                    gSon.fromJson<ArrayList<DashboardDrawerDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<DashboardDrawerDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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

    override fun fetchKnownLanguagesMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<KnownLanguagesDTO>>
    ) {
        networkController?.fetchKnownLanguages(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {

                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("LanguageList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("LanguageList")

                                val arrayList: ArrayList<KnownLanguagesDTO> =
                                    gSon.fromJson<ArrayList<KnownLanguagesDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<KnownLanguagesDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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

    override fun fetchDesignationMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<DesignationDTO>>
    ) {
        networkController?.fetchDesignation(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {

                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("DesignationList") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("DesignationList")

                                val arrayList: ArrayList<DesignationDTO> =
                                    gSon.fromJson<ArrayList<DesignationDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<DesignationDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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


    // fetch Group Roles
    override fun fetchGroupRolesMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<GroupRolesDTO>>
    ) {

        networkController?.fetchSubAdminGroupRoles(
            commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {

                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gSon = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {

                                commonResponseDTO.error != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }

                                jsonObject.has("list") -> {

                                    val jsonArray =
                                        jsonObject.optJSONArray("list")

                                    val arrayList: ArrayList<GroupRolesDTO> =
                                        gSon.fromJson<ArrayList<GroupRolesDTO>>(
                                            jsonArray?.toString(),
                                            object :
                                                TypeToken<ArrayList<GroupRolesDTO?>?>() {}.type
                                        )
                                    if (!arrayList.isNullOrEmpty()) {
                                        callback.onDataFound(arrayList)
                                    } else {
                                        callback.onDataNotFound()
                                    }
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
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


    // fetch Staff Group Roles
    override fun fetchGroupRolesStaffMasterDataRemote(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<GroupRolesStaffDTO>>
    ) {

        networkController?.fetchStaffGroupRoles(commonListingDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {

                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gSon = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gSon.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {

                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }

                            jsonObject.has("list") -> {

                                val jsonArray =
                                    jsonObject.optJSONArray("list")

                                val arrayList: ArrayList<GroupRolesStaffDTO> =
                                    gSon.fromJson<ArrayList<GroupRolesStaffDTO>>(
                                        jsonArray?.toString(),
                                        object :
                                            TypeToken<ArrayList<GroupRolesStaffDTO?>?>() {}.type
                                    )
                                if (!arrayList.isNullOrEmpty()) {
                                    callback.onDataFound(arrayList)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
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
}