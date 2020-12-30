package com.sagar.logutil.checqk.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sagar.logutil.checqk.master_controller.source.MasterRepository
import com.sagar.logutil.checqk.model.DayDTO
import com.sagar.logutil.checqk.model.DiscountEnum
import com.sagar.logutil.checqk.model.SystemValueMasterDTO
import com.sagar.logutil.checqk.model.discount.OutletDiscountDetailsDTO
import com.sagar.logutil.checqk.source.DashboardRepository
import com.sagar.logutil.checqk.utils.location.AddressWithLatLangDTO
import com.sagar.logutil.checqk.view.AddDiscountTimingViewModel
import com.sagar.logutil.checqk.view.dashboard.viewmodels.*
import com.sagar.logutil.checqk.view.discount.viewmodels.*
import com.sagar.logutil.checqk.view.login.remote.UserRepository
import com.sagar.logutil.checqk.view.login.viewmodels.FirstTimeLoginViewModel
import com.sagar.logutil.checqk.view.login.viewmodels.LoginViewModel
import com.sagar.logutil.checqk.view.login.viewmodels.SetPasswordViewModel
import com.sagar.logutil.checqk.view.login.viewmodels.VerifyOtpViewModel
import com.sagar.logutil.checqk.view.signup.viewmodels.OutletCreationViewModel
import com.sagar.logutil.checqk.view.signup.viewmodels.SignUpViewModel
import com.sagar.logutil.checqk.view.signup.viewmodels.TimingViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {

        fun <T : GenericBaseObservable?> obtainBaseObservable(
            activity: Activity,
            modelClass: Class<T>,
            owner: LifecycleOwner?,
            view: View?
        ): T? {

            return when {

                // Outlet Creation ViewModel
                modelClass.isAssignableFrom(OutletCreationViewModel::class.java) -> {
                    OutletCreationViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // Login ViewModel
                modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                    LoginViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(FirstTimeLoginViewModel::class.java) -> {
                    FirstTimeLoginViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                modelClass.isAssignableFrom(VerifyOtpViewModel::class.java) -> {
                    VerifyOtpViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(SetPasswordViewModel::class.java) -> {
                    SetPasswordViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }


                // Autocomplete ViewModel
                modelClass.isAssignableFrom(AutocompleteViewModel::class.java) -> {
                    AutocompleteViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!
                    ) as T
                }

                // Timing ViewModel
                modelClass.isAssignableFrom(TimingViewModel::class.java) -> {
                    TimingViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // SignUp ViewModel
                modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                    SignUpViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }


                // Close Dates ViewModel
                modelClass.isAssignableFrom(ClosedDatesViewModel::class.java) -> {
                    ClosedDatesViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // Basic Info ViewModel
                modelClass.isAssignableFrom(BasicInfoViewModel::class.java) -> {
                    BasicInfoViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // Address ViewModel
                modelClass.isAssignableFrom(AddressInfoViewModel::class.java) -> {
                    AddressInfoViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                //Restaurant Images ViewModel
                modelClass.isAssignableFrom(GalleryImagesViewModel::class.java) -> {
                    GalleryImagesViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                //Restaurant Images ViewModel
                modelClass.isAssignableFrom(MenuImagesViewModel::class.java) -> {
                    MenuImagesViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // Discount ViewModel
                modelClass.isAssignableFrom(DiscountViewModel::class.java) -> {
                    DiscountViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                // Membership Discount ViewModel
                modelClass.isAssignableFrom(MembershipDiscountViewModel::class.java) -> {
                    MembershipDiscountViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // CravX Cards Discount ViewModel
                modelClass.isAssignableFrom(CravXCardsDiscountViewModel::class.java) -> {
                    CravXCardsDiscountViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // HW Discount ViewModel
                modelClass.isAssignableFrom(HWDiscountViewModel::class.java) -> {
                    HWDiscountViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // In-House Discount ViewModel
                modelClass.isAssignableFrom(InHouseDiscountViewModel::class.java) -> {
                    InHouseDiscountViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // One Dashboard Discount ViewModel
                modelClass.isAssignableFrom(OneDashboardDiscountViewModel::class.java) -> {
                    OneDashboardDiscountViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // Discount Add Timing ViewModel
                modelClass.isAssignableFrom(AddDiscountTimingViewModel::class.java) -> {
                    AddDiscountTimingViewModel(
                        activity, activity.application, owner, view
                    ) as T
                }

                modelClass.isAssignableFrom(MenuCategoryViewModel::class.java) -> {
                    MenuCategoryViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(MenuSectionViewModel::class.java) -> {
                    MenuSectionViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                modelClass.isAssignableFrom(DishCategoryViewModel::class.java) -> {
                    DishCategoryViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // Sub Admin ViewModel
                modelClass.isAssignableFrom(SubAdminViewModel::class.java) -> {
                    SubAdminViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                // Add New Sub Admin ViewModel
                modelClass.isAssignableFrom(AddNewSubAdminViewModel::class.java) -> {
                    AddNewSubAdminViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(AddDishDialogViewModel::class.java) -> {
                    AddDishDialogViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(AddDishDialogViewModel::class.java) -> {
                    AddDishDialogViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                    DashboardViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        UserRepository.getInstance(activity.application, true)!!
                    ) as T
                }

                modelClass.isAssignableFrom(StaffUserViewModel::class.java) -> {
                    StaffUserViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                // Add Staff User
                modelClass.isAssignableFrom(AddStaffUserDialogViewModel::class.java) -> {
                    AddStaffUserDialogViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                // Add Staff Safety Readings
                modelClass.isAssignableFrom(AddStaffSafetyReadingsViewModel::class.java) -> {
                    AddStaffSafetyReadingsViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                modelClass.isAssignableFrom(SubAdminAccessRoleViewModel::class.java) -> {
                    SubAdminAccessRoleViewModel(
                        activity, activity.application, owner, view,
                        UserRepository.getInstance(activity.application, true)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                // Add New Roles ViewModel
                modelClass.isAssignableFrom(AddNewRoleDialogViewModel::class.java) -> {
                    AddNewRoleDialogViewModel(
                        activity, activity.application, owner, view,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                // Reserved Tables ViewModel
                modelClass.isAssignableFrom(ReservedTableViewModel::class.java) -> {
                    ReservedTableViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                // Table Config ViewModel
                modelClass.isAssignableFrom(TableConfigViewModel::class.java) -> {
                    TableConfigViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                //Customization Bottom Sheet Viewmodel
                modelClass.isAssignableFrom(CustomizationViewModel::class.java) -> {
                    CustomizationViewModel(
                        activity, activity.application, owner, view,
                        MasterRepository.getInstance(activity.applicationContext)!!,
                        DashboardRepository.getInstance(activity.application, true)!!
                    ) as T
                }
                else -> null
            }

        }

        fun selectDate(
            context: Context,
            message: String,
            mutableLiveData: MutableLiveData<String>
        ) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                context,
                { view, year, monthOfYear, dayOfMonth ->
                    mutableLiveData.value =
                        getDatePickerFormattedDate(
                            dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year.toString()
                        )
                }, year, month, day
            )
            datePickerDialog.setTitle(message)
            datePickerDialog.show()

        }

        fun selectDateWithFormat(
            context: Context,
            message: String,
            mutableLiveData: MutableLiveData<String>,
            dateFormat: String
        ) {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val datePickerDialog = DatePickerDialog(
                context,
                { view, year, monthOfYear, dayOfMonth ->
                    mutableLiveData.value =
                        dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year.toString()
                    /* getDatePickerFormattedDate(
                         dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year.toString()
                     )*/
                }, year, month, day
            )
            datePickerDialog.setTitle(message)
            datePickerDialog.show()

        }

        private fun getFormattedDate(DOB: String?, dateFormat: String): String? {
            val format: DateFormat = SimpleDateFormat(dateFormat)
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("dd-MM-yyyy") // 01-10-2020
            return formatString.format(date) ?: ""
        }

        private fun getDatePickerFormattedDate(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("dd-MM-yyyy") // 01-01-2020
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("dd MMMM yyyy") // 01-Jan-2020
            return formatString.format(date) ?: ""
        }

        fun getDatePickerFormattedSavingDate(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("dd MMMM yyyy") // 01 January 2020
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("dd-MM-yyyy") // 01-Jan-2020
            return formatString.format(date) ?: ""
        }

        fun getDatePickerFormattedDate3(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("dd MMMM yyyy") // 01 January 2020
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("dd MMMM") // 01 January
            return formatString.format(date) ?: ""
        }

        fun getDatePickerFormattedDate4(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("dd-MM-yyyy") // 01-01-2020
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("dd MMMM") // 01-Jan-2020
            return formatString.format(date) ?: ""
        }


        fun selectTime(
            context: Context,
            message: String,
            startTime: MutableLiveData<String>
        ) {
            val cal = Calendar.getInstance()
            val hours = cal.get(Calendar.HOUR_OF_DAY)
            val minutes = cal.get(Calendar.MINUTE)


            if (!startTime.value.isNullOrEmpty()) {
                startTime.value = ""
            }

            val timePickerDialog = TimePickerDialog(
                context,
                { view, hourOfDay, minute ->

                    var format = ""
                    var hour = hourOfDay
                    when {
                        hour == 0 -> {
                            hour += 12
                            format = "AM"
                        }
                        hour == 12 -> {
                            format = "PM"
                        }
                        hour > 12 -> {
                            hour -= 12;
                            format = "PM"
                        }
                        else -> {
                            format = "AM"
                        }
                    }

                    val min = if (minute == 0 || minute == 15 || minute == 30 || minute == 45) {
                        (minute / 15 * 15)
                    } else {
                        (minute / 15 * 15) + 15
                    }
                    startTime.value = getDatePickerFormattedTime("$hourOfDay:$min") + " " + format

                }, hours, minutes, false
            )

            timePickerDialog.setTitle(message)
            timePickerDialog.show()
        }

        private fun getDatePickerFormattedTime(DOB: String?): String? {
            val format: DateFormat = SimpleDateFormat("hh:mm")// 00:00
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {
            }
            val formatString: DateFormat = SimpleDateFormat("hh:mm")// 00:00 AM
            return formatString.format(date) ?: ""
        }


        fun getLongToFormattedDate(dateInMillis: Long?): String? {

            var dateString = ""
            if (dateInMillis != null && dateInMillis > 0) {
                dateString =
                    android.text.format.DateFormat.format("dd MMMM", Date(dateInMillis)).toString()
            }

            return dateString
        }

        fun getFormattedDateToLong(DOB: String?): Long {
            val format: DateFormat = SimpleDateFormat("dd MMM yyyy")
            var date: Date? = null
            try {
                date = format.parse(DOB)
            } catch (e: ParseException) {

            }
/*
            val timeZone: TimeZone = TimeZone.getTimeZone("GMT+5:30")
            TimeZone.setDefault(timeZone)
*/
            return date?.time ?: 0L
        }

        fun getCurrentDate(): String {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH) + 1
            val day = c.get(Calendar.DAY_OF_MONTH)
            return "$day-$month-$year"
        }


        fun hideKeyboard(activity: Activity?) {
            if (activity != null) {
                val inputMethodManager =
                    activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                if (inputMethodManager.isAcceptingText) {
                    inputMethodManager.hideSoftInputFromWindow(
                        activity.currentFocus
                            ?.windowToken, 0
                    )
                }
            }
        }

        fun showChooseProfileDialog(
            isFragment: Boolean, activity: Activity, context: Context, fragment: Fragment
        ) {

            if (isFragment) {
                CropImage.activity().start(context, fragment)
            } else {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(activity)
            }

        }

        fun getBitmapFromUri(
            context: Context,
            uri: Uri?
        ): Bitmap? {
            var parcelFileDescriptor: ParcelFileDescriptor? = null
            return try {
                parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri!!, "r")
                val fileDescriptor =
                    parcelFileDescriptor!!.fileDescriptor
                val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
                parcelFileDescriptor.close()
                image
            } catch (e: java.lang.Exception) {
//                Log.e("File Read", "Failed to load image.", e)
                null
            } finally {
                try {
                    parcelFileDescriptor?.close()
                } catch (e: IOException) {

//                    Log.e("File Read", "Error closing ParcelFile Descriptor")
                }
            }
        }


        fun convertBitmapToBase64(imgBitmap: Bitmap): String? {
            val byteArrayOutputStream = ByteArrayOutputStream()
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.NO_WRAP)
        }

        fun selectImages(activity: Activity, context: Context, fragment: Fragment) {
            val items =
                arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
            val builder =
                AlertDialog.Builder(activity)
            builder.setTitle("Add Photo")
            builder.setItems(items) { dialog, item ->
                when {
                    items[item] == "Take Photo" -> {
                        cameraIntent(fragment)
                    }
                    items[item] == "Choose from Library" -> {
                        galleryIntent(fragment)
                    }
                    items[item] == "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        }

        private fun cameraIntent(fragment: Fragment) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            fragment.startActivityForResult(intent, Constants.CAMERA)
        }

        private fun galleryIntent(fragment: Fragment) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            fragment.startActivityForResult(
                Intent.createChooser(
                    intent,
                    "android.intent.action.SEND_MULTIPLE"
                ), Constants.SELECT_FILE
            )
        }

        fun getImageUri(
            inContext: Context,
            inImage: Bitmap?
        ): Uri? {
            val bytes = ByteArrayOutputStream()
            inImage?.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            val path = MediaStore.Images.Media.insertImage(
                inContext.contentResolver,
                inImage,
                "Title",
                null
            )
            return Uri.parse(path)
        }


        fun getNameFromURI(activity: Activity, uri: Uri?): String? {
            val c: Cursor? = activity.contentResolver.query(uri!!, null, null, null, null)
            c?.moveToFirst()

            return c?.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }

        // Location
        fun getLocationAddressDTO(value: String?): AddressWithLatLangDTO? {
            val dto = object : TypeToken<AddressWithLatLangDTO?>() {}.type
            return Gson().fromJson<AddressWithLatLangDTO>(value, dto)
        }

        fun setLocationAddressDTO(dto: AddressWithLatLangDTO?): String? {
            val gSon = Gson()
            return gSon.toJson(dto)
        }


        fun commonDiscountValidations(
            outLetId: Long, outletDiscountDetailsDTO: OutletDiscountDetailsDTO,
            selectedMembershipHolderId: Long, selectedMemberShipTypeDTO: SystemValueMasterDTO,
            discountDayTimingsArrayList: ArrayList<DayDTO>,
            discountDayTimingsListMutableLiveData: MutableLiveData<List<DayDTO>>
        ): Boolean {

            var isValid = true

            outletDiscountDetailsDTO.outletId = outLetId
            outletDiscountDetailsDTO.outletDiscountCategory =
                DiscountEnum.CRAVX_CARD.name

            if (outletDiscountDetailsDTO.cardId != selectedMembershipHolderId
                || outletDiscountDetailsDTO.userMembershipTypeId != selectedMemberShipTypeDTO.serialId
            ) {
                outletDiscountDetailsDTO.outletDiscountDetailsId = 0
            }

            if (selectedMembershipHolderId == 0L) {
                isValid = false
            } else {
                outletDiscountDetailsDTO.cardId = selectedMembershipHolderId
            }
            if (selectedMemberShipTypeDTO.serialId == 0L) {
                isValid = false
            } else {
                outletDiscountDetailsDTO.userMembershipTypeId = selectedMemberShipTypeDTO.serialId
            }

            outletDiscountDetailsDTO.discountTimingList = arrayListOf()

            mainLoop@ for (item in discountDayTimingsArrayList ?: arrayListOf()) {

                for (item2 in item.discountDayAndTimings ?: arrayListOf()) {

                    if (item2.assuredDiscountString.isNullOrBlank()) {
                        item2.assuredDiscount = 0L
                    } else {
                        item2.assuredDiscount = item2.assuredDiscountString.trim().toLong()
                    }

                    if (item2.discountApplicableString.isNullOrBlank()) {
                        item2.discountApplicable = 0L
                    } else {
                        item2.discountApplicable = item2.discountApplicableString.trim().toLong()
                    }


                    if (item.allDaySameDiscountFlag) {

                        if (item2.assuredDiscount != 0L || !item2.complimentaryApplicable.isNullOrBlank()) {
                            if (item2.assuredDiscount <= item2.discountApplicable) {
                                outletDiscountDetailsDTO.discountTimingList.add(item2)
                                item2.complimentaryApplicableErrorMsg = ""
                                item2.discountApplicableErrorMsg = ""
                            } else {
                                isValid = false
                                item2.complimentaryApplicableErrorMsg = ""
                                item2.discountApplicableErrorMsg =
                                    "Discount Applicable cannot be lesser than Assured Discount"
                            }
                        } else if ((item2.assuredDiscountString.isNullOrBlank() && !item2.discountApplicableString.isNullOrBlank())
                            || (item2.assuredDiscountString.isNullOrBlank() && !item2.terms.isNullOrBlank())
                        ) {
                            outletDiscountDetailsDTO.discountTimingList.add(item2)
                            item2.complimentaryApplicableErrorMsg = ""
                            item2.discountApplicableErrorMsg = ""
                        } else if (!item2.assuredDiscountString.isNullOrBlank()) {
                            isValid = false
                            item2.complimentaryApplicableErrorMsg = "Please enter complimentary"
                            item2.discountApplicableErrorMsg = ""

                        }

                    } else {

                        var dayAssuredDiscount = 0L
                        if (!item.assuredDiscountString.isNullOrBlank()) {
                            dayAssuredDiscount = item.assuredDiscountString.trim().toLong()
                        }
                        item2.assuredDiscount = dayAssuredDiscount
                        if (item2.assuredDiscount != 0L || !item2.complimentaryApplicable.isNullOrBlank()) {
                            if (item2.assuredDiscount <= item2.discountApplicable) {
                                outletDiscountDetailsDTO.discountTimingList.add(item2)
                                item2.complimentaryApplicableErrorMsg = ""
                                item2.discountApplicableErrorMsg = ""
                                item.assuredDiscountErrorMsg = ""
                                item.discountTimingErrorMsg = ""
                            } else if (!item2.startsAt.isNullOrBlank()) {
                                isValid = false
                                item2.complimentaryApplicableErrorMsg = ""
                                item.discountTimingErrorMsg =
                                    "For " + item2.startsAt + " - " + item2.endsAt + ",\nDiscount Applicable cannot be lesser than Assured Discount"

                                break@mainLoop
                            }
                        } else if ((item2.assuredDiscountString.isNullOrBlank() && !item2.discountApplicableString.isNullOrBlank())
                            || (item2.assuredDiscountString.isNullOrBlank() && !item2.terms.isNullOrBlank())
                        ) {
                            outletDiscountDetailsDTO.discountTimingList.add(item2)
                            item2.complimentaryApplicableErrorMsg = ""
                            item2.discountApplicableErrorMsg = ""
                        } else if (!item2.assuredDiscountString.isNullOrBlank()) {
                            isValid = false
                            item.discountTimingErrorMsg =
                                "For " + item2.startsAt + " - " + item2.endsAt + ",\nPlease enter complimentary"

                            break@mainLoop
                        } else {
                            item.assuredDiscountErrorMsg = ""
                            item.discountTimingErrorMsg = ""
                        }


                    }
                }
            }

            discountDayTimingsListMutableLiveData.value = discountDayTimingsArrayList

            if (outletDiscountDetailsDTO.discountTimingList.size == 0) {
                isValid = false
            }

            return isValid
        }


    }

}
