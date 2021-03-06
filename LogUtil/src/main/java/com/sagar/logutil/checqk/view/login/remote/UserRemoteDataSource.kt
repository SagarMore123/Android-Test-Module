package com.sagar.logutil.checqk.view.login.remote

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.sagar.logutil.checqk.model.*
import com.sagar.logutil.checqk.model.discount.CorporateMembershipOneDashboardDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountDetailsDTO
import com.sagar.logutil.checqk.network.NetworkController
import com.sagar.logutil.checqk.network.network_utils.IDataSourceCallback
import com.sagar.logutil.checqk.network.network_utils.NetworkResponseCallback
import com.sagar.logutil.checqk.utils.Constants
import com.sagar.logutil.checqk.utils.CustomGsonBuilder
import org.json.JSONObject

class UserRemoteDataSource : UserDataSource {

    companion object {
        private var instance: UserRemoteDataSource? = null
        private var networkController: NetworkController? = null
        private var mContext: Context? = null
        private lateinit var sharedPreferences: SharedPreferences

        @JvmStatic
        fun getInstance(context: Context?): UserDataSource? {
            mContext = context
            networkController = NetworkController.getInstance(mContext!!)
            sharedPreferences =
                Constants.getSharedPreferences(context!!.applicationContext)
            if (instance == null) {
                instance = UserRemoteDataSource()
            }
            return instance
        }

    }

    override fun isFirstTimeLoginWithLoginId(
        loginDTO: LoginDTO,
        callback: IDataSourceCallback<LoginResponseDTO>
    ) {
        networkController?.isFirstTimeLoginWithLoginId(loginDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val loginResponseDTO =
                            gson.fromJson(jsonObject.toString(), LoginResponseDTO::class.java)

                        if (loginResponseDTO.success != null && loginResponseDTO.error == null) {
                            if (loginResponseDTO.success.status == "200") {
                                callback.onDataFound(loginResponseDTO)
                            }
                        } else {
                            if (loginResponseDTO.error != null) {
                                if (loginResponseDTO.error.message != null) {
                                    if (loginResponseDTO.error.message.contains("blocked")) {
                                        callback.onError(loginResponseDTO.error.message)
                                    } else {
                                        callback.onError(loginResponseDTO.error.message)
                                    }
                                }

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

    override fun login(loginDTO: LoginDTO, callback: IDataSourceCallback<UserDTO>) {
        networkController?.login(loginDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        val jsonObject = JSONObject(data)
                        var accessToken = ""
                        if (jsonObject.has("userId")) {
                            val userId = jsonObject.optLong("userId")
                            userId?.let {
                                sharedPreferences.edit().putString(
                                    Constants.LOGIN_IN_USER_ID,
                                    Constants.encrypt(userId.toString())
                                ).apply()
                            }
                        }
                        if (jsonObject.has("data")) {
                            val dataObject = jsonObject.getJSONObject("data")
                            if (dataObject.has("access_token")) {
                                accessToken = dataObject.getString("access_token")
                                NetworkController.accessToken = "Bearer $accessToken"
                                Log.e("AccessToken", "Bearer $accessToken")
                                sharedPreferences.edit()?.putString(
                                    Constants.ACCESS_TOKEN,
                                    Constants.encrypt(accessToken)
                                )?.apply()
                            }
                            if (dataObject.has("refresh_token")) {
                                accessToken = dataObject.getString("refresh_token")
                                NetworkController.refreshToken = "Bearer $accessToken"
                                Log.e("RefreshToken", "Bearer $accessToken")
                                sharedPreferences.edit()?.putString(
                                    Constants.REFRESH_TOKEN,
                                    Constants.encrypt(accessToken)
                                )?.apply()
                            }
                            if (dataObject.has("user")) {
                                val userJson = dataObject.optJSONObject("user")
                                val gson = CustomGsonBuilder().getInstance().create()
                                val userDTO =
                                    gson.fromJson(userJson.toString(), UserDTO::class.java)

                                userDTO?.loginId?.let {
                                    sharedPreferences.edit().putString(
                                        Constants.LOGIN_ID,
                                        Constants.encrypt(userDTO.loginId)
                                    ).apply()
                                }
                                userDTO?.emailAddress?.let {
                                    sharedPreferences.edit().putString(
                                        Constants.EMAIL_ID,
                                        Constants.encrypt(userDTO.emailAddress)
                                    ).apply()
                                }
                                userDTO?.userFirstName?.let {
                                    sharedPreferences.edit().putString(
                                        Constants.FIRST_NAME,
                                        Constants.encrypt(userDTO.userFirstName)
                                    ).apply()
                                }
                                userDTO?.userLastName?.let {
                                    sharedPreferences.edit().putString(
                                        Constants.LAST_NAME,
                                        Constants.encrypt(userDTO.userLastName)
                                    ).apply()
                                }
                                userDTO?.mobileNo?.let {
                                    sharedPreferences.edit().putString(
                                        Constants.MOBILE_NO,
                                        Constants.encrypt(userDTO.mobileNo)
                                    ).apply()
                                }
                                userDTO?.outletId?.let {
                                    sharedPreferences.edit().putString(
                                        Constants.OUTLET_ID,
                                        Constants.encrypt(userDTO.outletId.toString())
                                    ).apply()
                                }
                                userDTO?.productId?.let {
                                    sharedPreferences.edit().putString(
                                        Constants.PRODUCT_ID,
                                        Constants.encrypt(userDTO.productId.toString())
                                    ).apply()
                                }
                                callback.onDataFound(userDTO)
                            }
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

    override fun verifyOtp(loginDTO: LoginDTO, callback: IDataSourceCallback<String>) {
        networkController?.verifyOtp(loginDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        commonCallback(data, callback)
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

    override fun resetPassword(
        resetPassword: ResetPassword,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.resetPassword(resetPassword, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                if (data.isNotEmpty()) {
                    try {
                        commonCallback(data, callback)
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

    private fun commonCallback(
        data: String,
        callback: IDataSourceCallback<String>
    ) {
        val jsonObject = JSONObject(data)
        var accessToken = ""
        var refreshToken = ""
        if (jsonObject.has("data")) {
            val dataObject = jsonObject.optJSONObject("data")
            if (dataObject.has("access_token")) {
                accessToken = dataObject.getString("access_token")
                NetworkController.accessToken = "Bearer $accessToken"
                sharedPreferences.edit()?.putString(
                    Constants.ACCESS_TOKEN,
                    Constants.encrypt(accessToken)
                )?.apply()
            }
            if (dataObject.has("refresh_token")) {
                refreshToken = dataObject.getString("refresh_token")
                NetworkController.refreshToken = "Bearer $refreshToken"
                sharedPreferences.edit()?.putString(
                    Constants.REFRESH_TOKEN,
                    Constants.encrypt(refreshToken)
                )?.apply()
            }
            callback.onDataFound("success")
        } else {
            if (jsonObject.has("error")) {
                val error = jsonObject.optJSONObject("error")
                val errorMessage = error.getString("message")
                callback.onError(errorMessage)
            }
        }
    }


    override fun fetchTimings(outletId: Long, callback: IDataSourceCallback<OutletTimingDTO>) {

        networkController?.fetchTimings(outletId, object : NetworkResponseCallback {
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

                            jsonObject.has("outletTimingDTO") -> {

                                val outLetTimingsJsonObject =
                                    jsonObject.optJSONObject("outletTimingDTO")

                                if (outLetTimingsJsonObject != null) {

                                    val outletTimingDTO: OutletTimingDTO =
                                        gSon.fromJson<OutletTimingDTO>(
                                            outLetTimingsJsonObject.toString(),
                                            object :
                                                TypeToken<OutletTimingDTO?>() {}.type
                                        )
                                    if (!outletTimingDTO.timings.isNullOrEmpty()) {
                                        callback.onDataFound(outletTimingDTO)
                                    } else {
                                        callback.onDataNotFound()
                                    }
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

    override fun saveTimings(
        timings: OutletTimingDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveTimings(timings, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                NetworkController().commonResponseString(data, callback)
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }

        })
    }

    // Closed Dates
    override fun deleteOutletClosedDate(
        outletDateId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {

        networkController?.deleteOutletClosedDate(
            outletDateId,
            status,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    NetworkController().commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }

            })
    }

    override fun fetchOutletClosedDates(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<ClosedDatesDTO>>
    ) {

        networkController?.fetchOutletClosedDates(outletId, object : NetworkResponseCallback {
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

                            jsonObject.has("outletDateRestriction") -> {

                                val outLetTimingsJsonArray =
                                    jsonObject.getJSONArray("outletDateRestriction")

                                if (outLetTimingsJsonArray != null) {
                                    val closedDatesListing: ArrayList<ClosedDatesDTO> =
                                        gSon.fromJson<ArrayList<ClosedDatesDTO>>(
                                            outLetTimingsJsonArray.toString(),
                                            object :
                                                TypeToken<ArrayList<ClosedDatesDTO>>() {}.type
                                        )
                                    if (!closedDatesListing.isNullOrEmpty()) {
                                        callback.onDataFound(closedDatesListing)
                                    } else {
                                        callback.onDataNotFound()
                                    }
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

    override fun saveOutletClosedDates(
        closedDate: ClosedDatesDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveOutletClosedDates(closedDate, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                NetworkController().commonResponseString(data, callback)
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }

        })
    }


    override fun deleteOutletSecurityMeasureById(
        outletSecurityMeasuresId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {

        networkController?.deleteOutletSecurityMeasureById(
            outletSecurityMeasuresId,
            status,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    NetworkController().commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }

            })
    }

    override fun fetchSafetyMeasures(
        outletId: Long,
        callback: IDataSourceCallback<OutletSecurityMeasuresDTO>
    ) {

        networkController?.fetchSafetyMeasures(outletId, object : NetworkResponseCallback {
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

                            jsonObject.has("outletSecurityMeasuresDTOs") -> {

                                val safetyMeasuresJsonObject =
                                    jsonObject.optJSONObject("outletSecurityMeasuresDTOs")

                                if (safetyMeasuresJsonObject != null) {

                                    val safetyMeasuresObject: OutletSecurityMeasuresDTO =
                                        gSon.fromJson<OutletSecurityMeasuresDTO>(
                                            safetyMeasuresJsonObject?.toString(),
                                            object :
                                                TypeToken<OutletSecurityMeasuresDTO>() {}.type
                                        )
                                    if (safetyMeasuresObject != null) {
                                        callback.onDataFound(safetyMeasuresObject)
                                    } else {
                                        callback.onDataNotFound()
                                    }
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

    override fun saveSafetyMeasures(
        outletSecurityMeasuresDTO: OutletSecurityMeasuresDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveSafetyMeasures(
            outletSecurityMeasuresDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    NetworkController().commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }

            })
    }

    override fun fetchRestaurantDetails(
        outletId: Long,
        callback: IDataSourceCallback<RestaurantMasterDTO>
    ) {

        networkController?.fetchRestaurantDetails(outletId, object : NetworkResponseCallback {
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

                            jsonObject.has("restuarantMasterDTO") -> {

                                val restaurantMasterJsonObject =
                                    jsonObject.optJSONObject("restuarantMasterDTO")

                                if (restaurantMasterJsonObject != null) {
                                    val restaurantMasterDTO: RestaurantMasterDTO =
                                        gSon.fromJson<RestaurantMasterDTO>(
                                            restaurantMasterJsonObject?.toString(),
                                            object :
                                                TypeToken<RestaurantMasterDTO>() {}.type
                                        )
                                    if (restaurantMasterDTO != null) {
                                        callback.onDataFound(restaurantMasterDTO)
                                    } else {
                                        callback.onDataNotFound()
                                    }
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

    override fun saveRestaurantDetails(
        restaurantMasterDTO: RestaurantMasterDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveRestaurantDetails(
            restaurantMasterDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    NetworkController().commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }

            })
    }


    override fun saveRestaurantDetailsByVisitor(
        restaurantMasterDTO: RestaurantMasterDTO,
        callback: IDataSourceCallback<CommonResponseDTO>
    ) {
        networkController?.saveRestaurantDetailsByVisitor(
            restaurantMasterDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    NetworkController().commonResponseDTO(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }

            })
    }


    // Communication Info
    override fun fetchCommunicationInfo(
        outletId: Long,
        callback: IDataSourceCallback<CommunicationInfoDTO>
    ) {

        networkController?.fetchCommunicationInfo(outletId, object : NetworkResponseCallback {
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

                            jsonObject.has("communicationInfoDTO") -> {

                                val subJsonObject =
                                    jsonObject.optJSONObject("communicationInfoDTO")

                                if (subJsonObject != null) {

                                    val communicationInfoDTO: CommunicationInfoDTO =
                                        gSon.fromJson<CommunicationInfoDTO>(
                                            subJsonObject?.toString(),
                                            object :
                                                TypeToken<CommunicationInfoDTO>() {}.type
                                        )
                                    if (communicationInfoDTO != null) {
                                        callback.onDataFound(communicationInfoDTO)
                                    } else {
                                        callback.onDataNotFound()
                                    }

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


    override fun saveCommunicationInfo(
        communicationInfoDTO: CommunicationInfoDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveCommunicationInfo(
            communicationInfoDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    NetworkController().commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }

            })
    }

    override fun saveRestaurantProfileImage(
        restaurantProfileImageDTO: RestaurantProfileImageDTO,
        callback: IDataSourceCallback<String>
    ) {

        networkController?.saveRestaurantProfileImage(restaurantProfileImageDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {
                                commonResponseDTO.success?.message != null -> {
                                    callback.onDataFound(commonResponseDTO.success.message)
                                }

                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
                            }

                        }
                    } catch (e: Exception) {

                    }

//                    NetworkController().commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }
            })
    }

    override fun getRestaurantProfileImage(
        outletId: Long,
        callback: IDataSourceCallback<RestaurantProfileImageDTO>
    ) {

        networkController?.getRestaurantProfileImage(outletId, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {

                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )

                        when {
                            jsonObject.has("restaurantMasterProfileImageDTO") -> {

                                val restaurantProfileImageDTOJSONObject =
                                    jsonObject.optJSONObject("restaurantMasterProfileImageDTO")

                                if (restaurantProfileImageDTOJSONObject != null) {

                                    val restaurantProfileImageDTO: RestaurantProfileImageDTO =
                                        gson.fromJson(
                                            restaurantProfileImageDTOJSONObject.toString(),
                                            object :
                                                TypeToken<RestaurantProfileImageDTO?>() {}.type
                                        )
                                    callback.onDataFound(restaurantProfileImageDTO)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            commonResponseDTO.error != null -> {
                                callback.onError(commonResponseDTO.error.message)
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

    override fun getGalleryImageCategory(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<CommonCategoryDTO>
    ) {
        networkController?.getGalleryImageCategory(commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonCategoryDTO: CommonCategoryDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonCategoryDTO::class.java
                            )
                            if (commonCategoryDTO.galleryImageCategoryList.isNotEmpty()) {
                                callback.onDataFound(commonCategoryDTO)
                            }
                        } else {
                            callback.onDataNotFound()
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

    override fun saveGalleryImagesByCategory(
        galleryImageCategory: GalleryImageCategory,
        callback: IDataSourceCallback<Long>
    ) {
        networkController?.saveGalleryImagesByCategory(galleryImageCategory,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {
                                jsonObject.has("id") -> {
                                    callback.onDataFound(jsonObject.getLong("id"))
                                }

                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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

    override fun getAllGalleryImages(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<GalleryImageCategory>>
    ) {
        networkController?.getAllGalleryImages(outletId, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        when {
                            jsonObject.has("imageGalleryDTO") -> {

                                val imageGalleryJsonArray =
                                    jsonObject.getJSONArray("imageGalleryDTO")

                                if (imageGalleryJsonArray != null) {
                                    val galleryImageCategory: ArrayList<GalleryImageCategory> =
                                        gson.fromJson(
                                            imageGalleryJsonArray.toString(),
                                            object :
                                                TypeToken<ArrayList<GalleryImageCategory?>>() {}.type
                                        )
                                    callback.onDataFound(galleryImageCategory)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            commonResponseDTO.error?.message != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        }
                    } else {
                        callback.onDataNotFound()
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

    override fun removeGalleryImageByImageId(
        imageId: Long,
        id: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.removeGalleryImageByImageId(imageId, id,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                commonResponseDTO.success?.message != null -> {
                                    callback.onDataFound(commonResponseDTO.success.message)
                                }
                                /* jsonObject.has("imageGalleryDTO") -> {

                                    val imageGalleryJsonArray =
                                        jsonObject.getJSONArray("imageGalleryDTO")

                                    val galleryImageCategory: ArrayList<GalleryImageCategory> =
                                        gson.fromJson(
                                            imageGalleryJsonArray.toString(),
                                            object :
                                                TypeToken<ArrayList<GalleryImageCategory?>>() {}.type
                                        )
                                    callback.onDataFound(galleryImageCategory)
                                }*/
                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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

    override fun discardGalleryImagesByCategoryId(
        outletId: Long,
        galleryImageCategoryId: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.discardGalleryImageByCategoryId(outletId, galleryImageCategoryId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                commonResponseDTO.success?.message != null -> {
                                    callback.onDataFound(commonResponseDTO.success.message)
                                }

                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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


    override fun getMenuImageCategory(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<CommonCategoryDTO>
    ) {
        networkController?.getMenuImageCategory(commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {

                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonCategoryDTO: CommonCategoryDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonCategoryDTO::class.java
                            )
                            if (!commonCategoryDTO.catalogueImageCategoryList.isNullOrEmpty()) {
                                callback.onDataFound(commonCategoryDTO)
                            } else {
                                callback.onDataNotFound()
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

    override fun saveMenuImagesByCategory(
        catalogueImageCategory: CatalogueImageCategory,
        callback: IDataSourceCallback<Long>
    ) {
        networkController?.saveMenuImagesByCategory(catalogueImageCategory,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )

                            when {
                                jsonObject.has("id") -> {
                                    callback.onDataFound(jsonObject.getLong("id"))
                                }

                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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

    override fun getAllMenuImages(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<CatalogueImageCategory>>
    ) {
        networkController?.getAllMenuImages(outletId, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {
                        val jsonObject = JSONObject(data)
                        val gson = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        when {
                            jsonObject.has("menuImageGalleryDTO") -> {

                                val imageGalleryJsonArray =
                                    jsonObject.getJSONArray("menuImageGalleryDTO")

                                if (imageGalleryJsonArray != null) {
                                    val catalogueImageCategory: ArrayList<CatalogueImageCategory> =
                                        gson.fromJson(
                                            imageGalleryJsonArray.toString(),
                                            object :
                                                TypeToken<ArrayList<CatalogueImageCategory?>>() {}.type
                                        )
                                    callback.onDataFound(catalogueImageCategory)
                                } else {
                                    callback.onDataNotFound()
                                }
                            }
                            commonResponseDTO.error?.message != null -> {
                                callback.onError(commonResponseDTO.error.message)
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

    override fun removeMenuImageByImageId(
        imageId: Long,
        id: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.removeMenuImageByImageId(imageId, id,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                commonResponseDTO.success?.message != null -> {
                                    callback.onDataFound(commonResponseDTO.success.message)
                                }
                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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

    override fun discardMenuImagesByCategoryId(
        outletId: Long,
        catalogueImageCategoryId: Long,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.discardGalleryImageByCategoryId(outletId, catalogueImageCategoryId,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                commonResponseDTO.success?.message != null -> {
                                    callback.onDataFound(commonResponseDTO.success.message)
                                }

                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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

    // Discount
    override fun fetchOutletDiscountDetailsList(
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<OutletDiscountDetailsDTO>>
    ) {

        networkController?.fetchOutletDiscountDetailsList(
            outletId,
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

                                jsonObject.has("outletDiscountDetails") -> {

                                    val jsonArray =
                                        jsonObject.optJSONArray("outletDiscountDetails")
                                    if (jsonArray != null) {
                                        val arrayList: ArrayList<OutletDiscountDetailsDTO> =
                                            gSon.fromJson<ArrayList<OutletDiscountDetailsDTO>>(
                                                jsonArray.toString(),
                                                object :
                                                    TypeToken<ArrayList<OutletDiscountDetailsDTO?>?>() {}.type
                                            )
                                        if (!arrayList.isNullOrEmpty()) {
                                            callback.onDataFound(arrayList)
                                        } else {
                                            callback.onDataNotFound()
                                        }
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

    // One Dashboard Master Details
    override fun fetchOutletOneDashboardMasterDetails(
        productId: Long,
        outletId: Long,
        callback: IDataSourceCallback<ArrayList<CorporateMembershipOneDashboardDTO>>
    ) {

        networkController?.fetchOutletOneDashboardMasterDetails(
            productId,
            outletId,
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

                                jsonObject.has("membershipDetailsDTO") -> {

                                    val jsonArray =
                                        jsonObject.optJSONArray("membershipDetailsDTO")
                                    if (jsonArray != null) {
                                        val arrayList: ArrayList<CorporateMembershipOneDashboardDTO> =
                                            gSon.fromJson<ArrayList<CorporateMembershipOneDashboardDTO>>(
                                                jsonArray?.toString(),
                                                object :
                                                    TypeToken<ArrayList<CorporateMembershipOneDashboardDTO?>?>() {}.type
                                            )
                                        if (!arrayList.isNullOrEmpty()) {
                                            callback.onDataFound(arrayList)
                                        } else {
                                            callback.onDataNotFound()
                                        }
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

    override fun fetchOutletMembershipPlanMapping(
        outletId: Long,
        callback: IDataSourceCallback<Long>
    ) {

        networkController?.fetchOutletMembershipPlanMapping(
            outletId,
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

                                jsonObject.has("outletMembershipPlanMapping") -> {

                                    if (jsonObject.optJSONObject("outletMembershipPlanMapping") != null) {
                                        val outletMembershipPlanId =
                                            jsonObject.optJSONObject("outletMembershipPlanMapping")
                                                .optLong("outletMembershipPlanId")

                                        if (outletMembershipPlanId != null) {
                                            callback.onDataFound(outletMembershipPlanId)
                                        } else {
                                            callback.onDataNotFound()
                                        }
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


    override fun saveOutletDiscountDetails(
        outletDiscountDetailsDTO: OutletDiscountDetailsDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveOutletDiscountDetails(
            outletDiscountDetailsDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    NetworkController().commonResponseString(data, callback)
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }

            })
    }


    override fun saveOutletSubAdmin(
        subAdminDTO: SubAdminDTO,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.saveOutletSubAdmin(subAdminDTO, object : NetworkResponseCallback {
            override fun onSuccess(data: String) {
                try {
                    if (data.isNotBlank()) {
                        val jsonObject = JSONObject(data)

                        when {
                            jsonObject.has("success") -> {
                                val success = jsonObject.getString("success")
                                val userId = jsonObject.getLong("userId")
                                subAdminDTO.userId = userId
                                callback.onDataFound(success)
                            }
                            jsonObject.has("error") -> {
                                val error = jsonObject.getString("error")
                                callback.onError(error)
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        }
                        /*val gson = CustomGsonBuilder().getInstance().create()
                        val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                            jsonObject.toString(),
                            CommonResponseDTO::class.java
                        )
                        when {
                            commonResponseDTO.success?.message != null -> {
                                callback.onDataFound(commonResponseDTO.success.message)
                            }
                            commonResponseDTO.error?.message != null -> {
                                callback.onError(commonResponseDTO.error.message)
                            }
                            else -> {
                                callback.onError(NetworkController.SERVER_ERROR)
                            }
                        }*/
                    } else {
                        callback.onError(NetworkController.SERVER_ERROR)
                    }
                } catch (e: Exception) {
//                    callback.onError(NetworkController.SERVER_ERROR)

                }
            }

            override fun onError(errorCode: Int, errorData: String) {
                callback.onError(errorData)
            }
        })
    }

    override fun outletSubAdminListing(
        commonListingDTO: CommonListingDTO,
        callback: IDataSourceCallback<List<SubAdminDTO>>
    ) {
        networkController?.outletSubAdminListing(commonListingDTO,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                jsonObject.has("userMasterDTOs") -> {

                                    val imageGalleryJsonArray =
                                        jsonObject.getJSONArray("userMasterDTOs")

                                    val subAdminDTOList: ArrayList<SubAdminDTO> =
                                        gson.fromJson(
                                            imageGalleryJsonArray.toString(),
                                            object :
                                                TypeToken<ArrayList<SubAdminDTO?>>() {}.type
                                        )
                                    callback.onDataFound(subAdminDTOList)
                                }
                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
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


    override fun changeSubAdminStatus(
        userId: Long,
        status: Boolean,
        callback: IDataSourceCallback<String>
    ) {
        networkController?.changeSubAdminStatus(userId, status,
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            if (jsonObject.has("201")) {
                                val success = jsonObject.getString("201")
                                callback.onDataFound("UserId $success")
                            } else {
                                callback.onError(data)
                            }
                            /*val gson = CustomGsonBuilder().getInstance().create()
                            val commonResponseDTO: CommonResponseDTO = gson.fromJson(
                                jsonObject.toString(),
                                CommonResponseDTO::class.java
                            )
                            when {
                                commonResponseDTO.success?.message != null -> {
                                    callback.onError(commonResponseDTO.success.message)
                                }

                                commonResponseDTO.error?.message != null -> {
                                    callback.onError(commonResponseDTO.error.message)
                                }
                                else -> {
                                    callback.onError(NetworkController.SERVER_ERROR)
                                }
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

    override fun logoutUser(callback: IDataSourceCallback<String>) {
        networkController?.logoutUser(
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    try {
                        if (data.isNotBlank()) {
                            val jsonObject = JSONObject(data)
                            if (jsonObject.has("status")) {
                                val status = jsonObject.getString("status")
                                if (status == "Logout successfully")
                                    callback.onDataFound(status)
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

    /*override fun refreshToken(callback: IDataSourceCallback<String>) {
        networkController?.refreshToken(
            object : NetworkResponseCallback {
                override fun onSuccess(data: String) {
                    if (data.isNotEmpty()) {
                        try {
                            commonCallback(data, callback)
                        } catch (e: Exception) {
                            callback.onError(NetworkController.SERVER_ERROR)
                        }
                    }
                }

                override fun onError(errorCode: Int, errorData: String) {
                    callback.onError(errorData)
                }

            })
    }*/
}