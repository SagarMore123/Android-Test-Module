package com.sagar.logutil.checqk.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class Constants {

    companion object {

        const val SPLASH_TIME_OUT: Long = 1000 * 1 // 3 sec
        const val SIGN_UP_MASTERS_TIME_OUT: Long = 1000 * 10 // 10 sec
        const val PAGINATION_PAGE_DATA_COUNT: Int = 50
        private const val SHARED_PREF_NAME = "CravXSharedPreferences"
        private lateinit var sharedPreferences: SharedPreferences
        const val DATABASE_NAME = "CravXRoomDatabase"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val IS_FIRST_TIME: String = "IS_FIRST_TIME"
        const val IS_LOG_OUT: String = "IS_LOG_OUT"
        const val IS_SIGN_UP: String = "IS_SIGN_UP"
        const val SOURCE = "OUTLETADMINAPP"
        const val ROLE = "OUTLETADMIN"
        const val LOGIN_RESPONSE = "LOGIN_RESPONSE"
        const val LOGIN_ID = "LOGIN_ID"
        const val EMAIL_ID = "EMAIL_ID"
        const val FIRST_NAME = "FIRST_NAME"
        const val LAST_NAME = "LAST_NAME"
        const val MOBILE_NO = "MOBILE_NO"
        const val OUTLET_ID = "OUTLET_ID"
        const val PRODUCT_ID = "PRODUCT_ID"
        const val IS_FORGOT_PASS = "IS_FORGOT_PASS"
        const val SELECT_FILE = 1
        const val CAMERA = 2
        const val LOGIN_IN_USER_ID = "LOGIN_IN_USER_ID"

        const val SELECTED_DROPDOWN_ITEM_RESULT_CODE = "selectedItemResultCode"
        const val SELECTED_DROPDOWN_ITEM = "selectedItem"

        // Common listing default sort fields
        const val SORT_ORDER = "asc"
        const val SORT_ORDER_DESC = "desc"

        const val OUTLET_TYPE_SORT_FIELD = "outletTypeId"
        const val MEAL_TYPE_SORT_FIELD = "mealTypeId"
        const val FOOD_TYPE_SORT_FIELD = "foodTypeId"
        const val CUISINE_SORT_FIELD = "cuisineId"
        const val FAMOUS_FOR_SORT_FIELD = "famousForId"
        const val FACILITY_SORT_FIELD = "facilityId"
        const val COUNTRY_SORT_FIELD = "countryId"
        const val STATE_SORT_FIELD = "stateId"
        const val CITY_SORT_FIELD = "cityId"
        const val AREA_SORT_FIELD = "areaId"
        const val SOCIAL_MEDIA_SORT_FIELD = "mediumId"
        const val DISCOUNT_CATEGORY_SORT_FIELD = "outletDiscountCategoryId"
        const val DISCOUNT_MEMBERSHIP_PLAN_SORT_FIELD = "membershipId"
        const val CRAVX_CARDS_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD = "cravxDiscountCardId"
        const val HW_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD = "hwDiscountCardId"
        const val IN_HOUSE_DISCOUNT_MEMBERSHIP_HOLDER_SORT_FIELD = "inHouseDiscountCardId"
        const val GROUP_ROLE_SORT_FIELD = "groupRoleId"
        const val KNOWN_LANGUAGE_SORT_FIELD = "languageId"
        const val DESIGNATION_SORT_FIELD = "designationId"
        const val SUB_ADMIN_SORT_FIELD = "userId"
        const val SUB_ADMIN_GROUP_ROLE_SEARCH = "outletId"
        const val SUB_ADMIN_GROUP_ROLE_COLUMN = "applicableForEntityId"
        const val STAFF_USER_SORT_COLUMN = "fullName"
        const val STAFF_USER_SAFETY_READING_COLUMN = "staffSecurityMeasuresId"
        const val OUTLET_ID_COLUMN = "outletId"
        const val STAFF_USER_ID_COLUMN = "staffUserId"
        const val CHECKED_ON_DATE_COLUMN = "checkedOn"
        const val ACCESS_ROLE_NAME_COLUMN = "name"
        const val BOOKING_ID_COLUMN = "bookingId"

        const val FAMOUS_FOR_STRING_ADAPTER = "FAMOUS_FOR"
        const val EMAILS_ADDRESS_STRING_ADAPTER = "EMAILS_ADDRESS"
        const val MOBILE_NO_STRING_ADAPTER = "MOBILE_NO"

        const val CUISINE_CODE = 1001
        const val DISCOUNT_TIMING_CODE = 1002

        const val COUNTRY_ID_KEY: String = "COUNTRY_ID_KEY"
        const val STATE_ID_KEY: String = "STATE_ID_KEY"
        const val CITY_ID_KEY: String = "CITY_ID_KEY"

        const val LOCATION_ADDRESS_KEY: String = "LOCATION_ADDRESS_KEY"
        const val LOCATION_LATITUDE_KEY: String = "LOCATION_LATITUDE_KEY"
        const val LOCATION_LONGITUDE_KEY: String = "LOCATION_LONGITUDE_KEY"
        const val OUTLET_DISCOUNT_DETAILS_DTO_KEY = "OUTLET_DISCOUNT_DETAILS_DTO_KEY"
        const val DISCOUNT_TIMING_DTO_KEY = "DISCOUNT_TIMING_DTO_KEY"
        const val DISCOUNT_PLAN_ELIGIBLE_NAME_KEY = "DISCOUNT_PLAN_ELIGIBLE_NAME_KEY"
        const val WEEK_NAME_KEY = "WEEK_NAME_KEY"
        const val CORPORATE_MEMBERSHIPS_ONE_DASHBOARD_MASTERS_KEY = "ONE_DASHBOARD_MASTER_KEY"
        const val RESTAURANT_DTO_KEY = "RESTAURANT_DTO_KEY"
        const val IS_SPLASH_MASTER = "IS_SPLASH_MASTER"

        const val COUNTRY_CODE = 2001
        const val STATE_CODE = 2002
        const val CITY_CODE = 2003
        const val AREA_CODE = 2004
        const val SOCIAL_MEDIA_CODE = 2005
        const val MAPS_CODE = 2006
        const val GROUP_ROLE_CODE = 2007
        const val CORPORATE_MEMBERSHIP_ONE_DASHBOARD_CODE = 2008
        const val OUTLET_CREATED_CODE = 2009
        const val KNOWN_LANGUAGES_CODE = 2010
        const val DESIGNATION_CODE = 2011
        const val GROUP_ROLE_STAFF_CODE = 2012

        const val ACCESS_TOKEN_INVALID = "ACCESS_TOKEN_INVALID"
        const val SUB_ADMIN_DTO = "SUB_ADMIN_DTO"
        const val STAFF_USER_DTO = "STAFF_USER_DTO"
        const val ACCESS_ROLE_DTO = "ACCESS_ROLE_DTO"

        const val OUTLET_DASHBOARD = "Outlet Dashboard"
        const val RESTAURANT_DETAILS = "Restaurant Details"
        const val PHOTO_GALLERY = "Restaurant Photo Gallery"
        const val MENU_IMAGES = "Restaurant Menu Images"
        const val TIMING = "Timing"
        const val COMMUNICATION_DETAILS = "Restaurant Communication Details"
        const val DISCOUNT = "Restaurant Discounts"
        const val MENU_MANAGEMENT = "Menu Management"
        const val MENU_BUILDER = "Menu Builder"
        const val MENU_CATEGORY = "Menu Category"
        const val MENU_SECTION = "Menu Section"
        const val MENU_DISHES = "Menu Dishes"
        const val ACCESS_ROLE_MANAGEMENT = "Access Role Management"
        const val SUB_ADMIN_MANAGEMENT = "Sub Admin Management"
        const val STAFF_MANAGEMENT = "Restaurant Staff"
        const val CAPTURE_SAFETY_READINGS = "Capture Safety Readings"
        const val TAKE_ORDER = "Take Order"
        const val TABLE_MANAGEMENT = "Table Management"
        const val CHECK_IN_USERS = "Check In Users"
        const val BILL_GENERATION = "Bill Generation"
        const val LOG_OUT = "Logout"

        const val STAFF_USERS_LIST = "STAFF_USERS_LIST"

        const val CONFIRMED_BOOKING_STATUS = "CONFIRMED"

        const val TABLE_ID_COLUMN = "tableId"

        inline fun <reified T : Activity> changeActivity(activity: Activity) {
            val intent = Intent(activity, T::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        }

        inline fun <reified T : Activity> changeActivityAndFinish(activity: Activity) {
            val intent = Intent(activity, T::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
            activity.finish()
        }


        fun showToastMessage(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        }

        fun getAccessToken(context: Context?): String? {
            sharedPreferences = getSharedPreferences(context!!)
            return try {
                "Bearer ${decrypt(sharedPreferences.getString(ACCESS_TOKEN, ""))}"

            } catch (e: Exception) {
                ""
            }
        }

        fun getRefreshToken(context: Context?): String? {
            sharedPreferences = getSharedPreferences(context!!)
            return try {
                "${decrypt(sharedPreferences.getString(REFRESH_TOKEN, ""))}"

            } catch (e: Exception) {
                ""
            }
        }


        fun getIsFirstTime(context: Context?): Boolean {
            sharedPreferences = getSharedPreferences(context!!)
            return try {
                decrypt(sharedPreferences.getString(IS_FIRST_TIME, ""))?.toBoolean()?:true
            } catch (e: Exception) {
                true
            }
        }

        fun getIsLogout(context: Context?): Boolean? {
            sharedPreferences = getSharedPreferences(context!!)
            return try {
                decrypt(sharedPreferences.getString(IS_LOG_OUT, ""))?.toBoolean()
            } catch (e: Exception) {
                true
            }
        }

        fun getIsSignUp(context: Context?): Boolean {
            sharedPreferences = getSharedPreferences(context!!)
            return try {
                decrypt(sharedPreferences.getString(IS_SIGN_UP, ""))?.toBoolean() ?: false
            } catch (e: Exception) {
                false
            }
        }


        fun getOutletId(context: Context): String? {
            sharedPreferences = getSharedPreferences(context)
            return try {
                return decrypt(sharedPreferences.getString(OUTLET_ID, ""))
            } catch (e: Exception) {
                ""
            }

        }

        fun getEmailId(context: Context): String? {
            sharedPreferences = getSharedPreferences(context)
            return try {
                return decrypt(sharedPreferences.getString(EMAIL_ID, ""))
            } catch (e: Exception) {
                ""
            }

        }


        private const val secretKey = "3f26328d2d014be5"
        private const val salt = "a805e0093325872e"
        private const val ivKey = "0123456789ABCDEF"

        fun passwordEncrypt(strToEncrypt: String): String? {
            Log.i("START method encrypt", "")
            var encryptedText=""
            try {
//                val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
//                val ivspec = IvParameterSpec(iv)
                val iv: ByteArray = ivKey.toByteArray()
                val ivspec = IvParameterSpec(iv)

                val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                val spec: KeySpec = PBEKeySpec(
                    secretKey.toCharArray(),
                    salt.toByteArray(),
                    65536,
                    256
                )
                // spec = new PBEKeySpec(secretKey.toCharArray());
                val tmp = factory.generateSecret(spec)
                val secretKey = SecretKeySpec(tmp.encoded, "AES")
                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec)

                val encryptedByte: ByteArray = cipher.doFinal(strToEncrypt.toByteArray(charset("UTF-8")))
                encryptedText = Base64.encodeToString(encryptedByte, Base64.NO_WRAP)

                return encryptedText

            } catch (e: Exception) {
                Log.i("Error while encrypting:", e.message ?: "")
            }
            Log.i("START method encrypt", "")

            return encryptedText
        }

        @Throws(Exception::class)
        fun encrypt(plainText: String?): String? {
            var encryptedText = ""
            if (plainText != null) {
                val secretKey: SecretKey = getKey()
                val plainTextByte = plainText.toByteArray()
                val cipher: Cipher = Cipher.getInstance("AES")
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                val encryptedByte: ByteArray = cipher.doFinal(plainTextByte)
                //Base64.Encoder encoder = Base64.getEncoder();
                encryptedText = Base64.encodeToString(encryptedByte, Base64.URL_SAFE)
            }
            return encryptedText
        }


        @Throws(Exception::class)
        fun decrypt(encryptedText: String?): String? {
            var decryptedText: String?
            if (encryptedText != null && !encryptedText.equals("", ignoreCase = true)) {
                val secretKey: SecretKey = getKey()
                val cipher: Cipher = Cipher.getInstance("AES")
                // Base64.Decoder decoder = Base64.getDecoder();
                val encryptedTextByte: ByteArray =
                    Base64.decode(encryptedText, Base64.URL_SAFE)
                cipher.init(Cipher.DECRYPT_MODE, secretKey)
                val decryptedByte: ByteArray = cipher.doFinal(encryptedTextByte)
                decryptedText = String(decryptedByte)
                return decryptedText
            }
            return null
        }

        @Throws(NoSuchAlgorithmException::class)
        private fun getKey(): SecretKey {
            val md: MessageDigest = MessageDigest.getInstance("SHA-256")
            md.update("reliance@123".toByteArray())
            val encryptionKey: ByteArray = md.digest()
            return SecretKeySpec(encryptionKey, "AES")
        }


        fun disableUserInteraction(mActivity: Activity) {
            mActivity.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }

        fun enableUserInteraction(mActivity: Activity) {
            mActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }

        //    public static void showLogoutDialog(final Activity activity) {
        //        LayoutInflater inflater = activity.getLayoutInflater();
        //        View alertLayout = inflater.inflate(R.layout.logout_dialog, null);
        //        Button cancelBtn = alertLayout.findViewById(R.id.cancel_button);
        //        Button confirmBtn = alertLayout.findViewById(R.id.confirm_button);
        //        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        //        // this is set the view from XML inside AlertDialog
        //        alert.setView(alertLayout);
        //        // disallow cancel of AlertDialog on click of back button and outside touch
        //        alert.setCancelable(false);
        //
        //        final AlertDialog dialog = alert.create();
        //        cancelBtn.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                dialog.dismiss();
        //            }
        //        });
        //
        //        confirmBtn.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                Intent intent = new Intent(activity, EmailVerificationActivity.class);
        //                activity.startActivity(intent);
        //                dialog.dismiss();
        //                activity.finish();
        //                //shared preferences to be cleared once confirmed
        //                clearSharedPrefs(activity);
        //
        //            }
        //        });
        //        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //        dialog.show();
        //    }

        fun clearSharedPrefs(context: Context) {
            try {
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply()
                getSharedPreferences(context).edit()?.putString(IS_FIRST_TIME, encrypt(false.toString()))?.apply()
//                getSharedPreferences(context).edit()?.putString(IS_LOG_OUT, encrypt(true.toString()))?.apply()
            } catch (e: Exception) {
            }
        }


        fun clearAllPrefsExceptKey(context: Context, requiredKeyList: List<String>) {
            val prefs: Map<String?, *> =
                context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).all
            for(requiredKey in requiredKeyList){
                for ((key) in prefs) {
                    if (!key.equals(requiredKey, true)) {
                        context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit()
                            .remove(key).apply()
                    }
                }
            }

        }

        fun getTypeOfSeaterInFullName(name: String?): String {
            val list: List<String> = name!!.split("_")
            var typeOfSeater = list[0]
            when (typeOfSeater) {
                "2" -> typeOfSeater = "TWO"
                "3" -> typeOfSeater = "THREE"
                "4" -> typeOfSeater = "FOUR"
                "5" -> typeOfSeater = "FIVE"
                "6" -> typeOfSeater = "SIX"
                "7" -> typeOfSeater = "SEVEN"
                "8" -> typeOfSeater = "EIGHT"
                "9" -> typeOfSeater = "NINE"
                "10" -> typeOfSeater = "TEN"
                "11" -> typeOfSeater = "ELEVEN"
                "12" -> typeOfSeater = "TWELVE"
                "13" -> typeOfSeater = "THIRTEEN"
                "14" -> typeOfSeater = "FOURTEEN"
                "15" -> typeOfSeater = "FIFTEEN"
                "16" -> typeOfSeater = "SIXTEEN"
                "17" -> typeOfSeater = "SEVENTEEN"
                "18" -> typeOfSeater = "EIGHTTEEN"
                "19" -> typeOfSeater = "NINETEEN"
                "20" -> typeOfSeater = "TWENTY"
            }
            return typeOfSeater
        }

        fun getTypeOfSeater(name: String?): String {
            val list: List<String> = name!!.split("_")
            var typeOfSeater = list[0]
            when (typeOfSeater) {
                "TWO" -> typeOfSeater = "2"
                "THREE" -> typeOfSeater = "3"
                "FOUR" -> typeOfSeater = "4"
                "FIVE" -> typeOfSeater = "5"
                "SIX" -> typeOfSeater = "6"
                "SEVEN" -> typeOfSeater = "7"
                "EIGHT" -> typeOfSeater = "8"
                "NINE" -> typeOfSeater = "9"
                "TEN" -> typeOfSeater = "10"
                "ELEVEN" -> typeOfSeater = "11"
                "TWELVE" -> typeOfSeater = "12"
                "THIRTEEN" -> typeOfSeater = "13"
                "FOURTEEN" -> typeOfSeater = "14"
                "FIFTEEN" -> typeOfSeater = "15"
                "SIXTEEN" -> typeOfSeater = "16"
                "SEVENTEEN" -> typeOfSeater = "17"
                "EIGHTTEEN" -> typeOfSeater = "18"
                "NINETEEN" -> typeOfSeater = "19"
                "TWENTY" -> typeOfSeater = "20"
            }
            return typeOfSeater
        }

    }

}