package com.sagar.logutil.checqk.model

import androidx.annotation.NonNull
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.*

@Entity(tableName = "outlet_type")
class OutletTypeMasterDTO : Serializable {

    @PrimaryKey
    @ColumnInfo
    var outletTypeId: Long = 0

    @ColumnInfo
    var outletTypeName: String = ""

    var selected = false

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as OutletTypeMasterDTO

        if (outletTypeId != other.outletTypeId) {
            return false
        }

        if (outletTypeName != other.outletTypeName) {
            return false
        }

        if (selected != other.selected) {
            return false
        }

        return true
    }
}

@Entity(tableName = "meal_type")
class MealTypeMasterDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var mealTypeId: Long = 0

    @ColumnInfo
    var mealTypeName: String = ""

    var selected = false

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as MealTypeMasterDTO

        if (mealTypeId != other.mealTypeId) {
            return false
        }

        if (mealTypeName != other.mealTypeName) {
            return false
        }

        if (selected != other.selected) {
            return false
        }
        return true
    }
}

@Entity(tableName = "food_type")
class FoodTypeMasterDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var foodTypeId: Long = 0

    @ColumnInfo
    var foodTypeName: String = ""

    var selected = false

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as FoodTypeMasterDTO

        if (foodTypeId != other.foodTypeId) {
            return false
        }

        if (foodTypeName != other.foodTypeName) {
            return false
        }

        if (selected != other.selected) {
            return false
        }

        return true
    }
}

@Entity(tableName = "cuisine")
class CuisineMasterDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var cuisineId: Long = 0

    @ColumnInfo
    var cuisineName: String = ""

    var selected = false

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as CuisineMasterDTO

        if (cuisineId != other.cuisineId) {
            return false
        }

        if (cuisineName != other.cuisineName) {
            return false
        }

        if (selected != other.selected) {
            return false
        }

        return true
    }
}

@Entity(tableName = "famous_for")
class FamousForMasterDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var famousForId: Long = 0

    @ColumnInfo
    var name: String = ""

    var selected = false

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as FamousForMasterDTO

        if (famousForId != other.famousForId) {
            return false
        }

        if (name != other.name) {
            return false
        }

        if (selected != other.selected) {
            return false
        }

        return true
    }
}

@Entity(tableName = "facility")
class FacilityMasterDTO : Serializable {

    @PrimaryKey
    @ColumnInfo
    var facilityId: Long = 0

    @ColumnInfo
    var facilityName: String = ""

    var facilityTextMappingId: Long = 0

    var facilityValue: String = ""

    var selected = false

/*
    var outletTypeIds: ArrayList<Long>? = null
    var facilityActiveImage: ImageDTO? = null
    var facilityInactiveImage: ImageDTO? = null
*/

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as FacilityMasterDTO

        if (facilityId != other.facilityId) {
            return false
        }

        if (facilityName != other.facilityName) {
            return false
        }

        if (facilityValue != other.facilityValue) {
            return false
        }

        if (selected != other.selected) {
            return false
        }

        return true
    }
}


class BooleanMasterDTO : Serializable {
    var id: Long = 0
    var name: String = ""
    var selected = false

}

@Entity(tableName = "area")
class AreaMasterDTO : Serializable {

    @PrimaryKey
    @ColumnInfo
    @NonNull
    var areaId: Long = 0

    @ColumnInfo
    @NonNull
    var areaName: String = ""

    @ColumnInfo
    @NonNull
    var cityId: Long = 0

    @ColumnInfo
    @NonNull
    var countryId: Long = 0

    @ColumnInfo
    var latitude: String? = ""

    @ColumnInfo
    var longitude: String? = ""

    @ColumnInfo
    var pinCodeId: Long? = 0

    @ColumnInfo
    var stateId: Long? = 0

    @ColumnInfo
    var showToUserFlag = true

    @ColumnInfo
    var active = true

}

@Entity(tableName = "city")
class CityMasterDTO : Serializable {

    @PrimaryKey
    @ColumnInfo
    @NonNull
    var cityId: Long = 0

    @ColumnInfo
    @NonNull
    var cityName: String = ""

    @ColumnInfo
    @NonNull
    var countryId: Long = 0

    @ColumnInfo
    @NonNull
    var stateId: Long = 0

    @ColumnInfo
    var latitude: String? = ""

    @ColumnInfo
    var longitude: String? = ""

    @ColumnInfo
    var code: String? = ""

    @ColumnInfo
    var showToUserFlag = true

    @ColumnInfo
    var active = true


}

@Entity(tableName = "country")
class CountryMasterDTO : Serializable {

    @PrimaryKey
    @ColumnInfo
    @NonNull
    var countryId: Long = 0

    @ColumnInfo
    @NonNull
    var countryName: String = ""

    @ColumnInfo
    var currencyId: Long? = 0

    @ColumnInfo
    var languageId: Long? = 0

    @ColumnInfo
    var callCode: String? = ""

    @ColumnInfo
    var code: String? = ""

    @ColumnInfo
    var dateFormat: String? = ""

    @ColumnInfo
    var showToUserFlag = true

    @ColumnInfo
    var active = true


}

@Entity(tableName = "state")
class StateMasterDTO : Serializable {

    @PrimaryKey
    @ColumnInfo
    @NonNull
    var stateId: Long = 0

    @ColumnInfo
    @NonNull
    var stateName: String = ""

    @ColumnInfo
    @NonNull
    var countryId: Long = 0

    @ColumnInfo
    var timezoneId: Long? = 0

    @ColumnInfo
    var languageId: Long? = 0

    @ColumnInfo
    var code: String? = ""

    @ColumnInfo
    var active = true

}

@Entity(tableName = "pincode")
class PincodeMasterDTO : Serializable {

    @PrimaryKey
    @ColumnInfo
    @NonNull
    var pincodeId: Long = 0

    @ColumnInfo
    @NonNull
    var pincode: String = ""

    @ColumnInfo
    @NonNull
    var cityId: Long = 0

    @ColumnInfo
    var districtId: Long? = 0

    @ColumnInfo
    var regionId: Long? = 0

    @ColumnInfo
    var code: String? = ""

    @ColumnInfo
    var active = true

}

@Entity(tableName = "social_media")
class SocialMediaMasterDTO : Serializable {

    @PrimaryKey
    @ColumnInfo
    @NonNull
    var mediumId: Long = 0

    @ColumnInfo
    @NonNull
    var mediumName: String = ""

    @ColumnInfo
    var mediumUrl: String? = ""

    @ColumnInfo
    var mediumIcon: ImageDTO? = null

    @ColumnInfo
    var active = true

}

@Entity(tableName = "group_roles")
class GroupRolesDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var id: Long = 0

    @ColumnInfo
    var name: String = ""

    @ColumnInfo
    var description: String? = ""

    @ColumnInfo
    var assignedToUser: Long? = 0

    var selected = false

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as GroupRolesDTO

        if (id != other.id) {
            return false
        }

        if (name != other.name) {
            return false
        }
        if (description != other.description) {
            return false
        }
        if (assignedToUser != other.assignedToUser) {
            return false
        }

        if (selected != other.selected) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (assignedToUser?.hashCode() ?: 0)
        result = 31 * result + selected.hashCode()
        return result
    }
}

@Entity(tableName = "dashboard_drawer")
class DashboardDrawerDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var moduleId: Long = 0

    @ColumnInfo
    var name: String = ""

    @ColumnInfo
    @TypeConverters(DashboardDrawerTypeConverter::class)
    var applicationModules: List<DashboardDrawerDTO> ?= null

    @ColumnInfo
    var applicableForRole: String ?= null

    var selected = false

}

class DashboardDrawerTypeConverter {
    private val gson = Gson()
    @TypeConverter
    fun stringToList(data: String?): List<DashboardDrawerDTO> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<DashboardDrawerDTO>>() {

        }.type

        return gson.fromJson<List<DashboardDrawerDTO>>(data, listType)
    }

    @TypeConverter
    fun listToString(someObjects: List<DashboardDrawerDTO>): String {
        return gson.toJson(someObjects)
    }
}


@Entity(tableName = "known_languages")
class KnownLanguagesDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var languageId: Long = 0

    @ColumnInfo
    var languageName: String? = ""

    @ColumnInfo
    var code: String? = ""

    var selected = false
}

@Entity(tableName = "designation")
class DesignationDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var designationId: Long = 0

    @ColumnInfo
    var designationName: String? = ""

    @ColumnInfo
    var designationDesc: String? = ""

    var selected = false
}


@Entity(tableName = "group_roles_staff")
class GroupRolesStaffDTO : Serializable {
    @PrimaryKey
    @ColumnInfo
    var id: Long = 0

    @ColumnInfo
    var name: String? = ""



    var selected = false

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) {
            return false
        }

        other as GroupRolesDTO

        if (id != other.id) {
            return false
        }

        if (name != other.name) {
            return false
        }
        
        if (selected != other.selected) {
            return false
        }
        return true
    }
}
