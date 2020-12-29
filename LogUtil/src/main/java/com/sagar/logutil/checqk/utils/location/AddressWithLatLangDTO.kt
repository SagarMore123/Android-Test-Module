package com.astrika.checqk.utils.location

import java.io.Serializable

class AddressWithLatLangDTO : Serializable {
    var latitude: Double? = 0.0
    var longitude: Double? = 0.0
    var cityName: String? = ""
    var countryName: String? = ""
    var pincode: String? = ""
    var address: String? = ""
}