package com.sagar.logutil.checqk.network.network_utils

//const val SERVER_URL: String = "http://103.146.177.39:7010/edge-service/"
//const val SERVER_VAL: String = SERVER_URL + "edge-service/"
//const val OUTLET_URL: String = SERVER_VAL + "outlet-service/"
//const val BOOKING_URL: String = SERVER_VAL + "booking-service/"

//const val MASTER_SERVER_URL: String = "http://103.146.177.39:7002/system-service/"

const val SERVER_URL: String = "https://uat-api.checqk.com/edge-service/"  // sit server
//const val SERVER_URL: String = "https://uat-api.checqk.com/edge-service/"  // uat server

//const val SERVER_IMG_URL: String = "https://uat-api.checqk.com/~cravx/public_html/"
//const val SERVER_IMG_URL: String = "https://uat-api.checqk.com/~cravx/public_html/"
const val SERVER_IMG_URL: String = "https://cravxfiles.s3.amazonaws.com/home/cravx/public_html"

//const val SERVER_URL: String = "http://103.146.177.39:7010/edge-service/"
//const val SERVER_IMG_URL: String = "http://103.146.177.39/~cravx/public_html/"

//const val SERVER_URL: String = "http://114.79.157.143:7010/edge-service/"
//const val SERVER_IMG_URL: String = "http://114.79.157.143/~cravx/public_html/"

const val SVM_URL: String = "system-service/"
const val RESTAURANT_URL: String = SERVER_URL + "restaurant-service/"
const val OUTLET_URL: String = SERVER_URL + "outlet-service/"
const val CATALOGUE_URL: String = SERVER_URL + "catalogue-service/"
const val TIMING_URL: String = SERVER_URL + "timing-service/"
const val LOCATION_URL: String = SERVER_URL + "location-service/"
const val DISCOUNT_URL: String = SERVER_URL + "discount-service/"
const val USER_URL: String = SERVER_URL + "user-service/"
const val STAFF_URL: String = SERVER_URL + "staff-service/"
const val BOOKING_URL: String = SERVER_URL + "booking-service/"
const val CHECKIN_URL: String = SERVER_URL + "checkin-service/"
const val OAUTH_URL: String = SERVER_URL + "oauth-server/"

const val IS_FIRST_TIME_LOGIN_WITH_LOGIN_ID = OAUTH_URL + "isFirstTimeLoginWithLoginId"
//const val LOGIN_WITH_LOGIN_ID = "oauth-server/loginWithLoginId"
const val LOGIN_WITH_LOGIN_ID = OAUTH_URL + "login"
const val RESET_PASSWORD = OAUTH_URL + "resetPassword"
const val LOGIN_MASTERS = OAUTH_URL + "loginForMasters"

// Timings
const val FETCH_TIMINGS = OUTLET_URL + "get/OutletTiming/outletId"
const val SAVE_TIMING = OUTLET_URL + "save/OutletTiming"

// Closed Dates
const val DELETE_OUTLET_CLOSED_DATE = TIMING_URL + "changeStatus/DateRestriction"
const val FETCH_OUTLET_CLOSED_DATES = TIMING_URL + "get/DateRestriction/outletId"
const val SAVE_OUTLET_CLOSED_DATES = TIMING_URL + "save/DateRestriction"

const val SAVE_SAFETY_MEASURES = OUTLET_URL + "save/OutletSecurityMeasures"
const val FETCH_SAFETY_MEASURES = OUTLET_URL + "get/OutletSecurityMeasures/outletId"
const val DELETE_SAFETY_MEASURES = OUTLET_URL + "changeStatus/OutletSecurityMeasures"


// Basic Info
const val FETCH_RESTAURANT_DETAILS = RESTAURANT_URL + "get/RestaurantMaster/Id"
const val SAVE_RESTAURANT_DETAILS = RESTAURANT_URL + "save/RestaurantMaster"
const val SAVE_RESTAURANT_DETAILS_BY_VISITOR = RESTAURANT_URL + "save/RestaurantMaster/ByVisitor"

// Communication Info
const val FETCH_COMMUNICATION_INFO = OUTLET_URL + "get/CommunicationInfo/outletId"
const val SAVE_COMMUNICATION_INFO = OUTLET_URL + "save/CommunicationInfo"

// Basic Info Masters
//const val FETCH_SVM = "listing/SystemValueMasters"
const val FETCH_SVM = SVM_URL + "listing/SystemValueMasters"
const val FETCH_OUTLET_TYPE = OUTLET_URL + "listing/OutletType"
const val FETCH_MEAL_TYPE = RESTAURANT_URL + "listing/MealType"
const val FETCH_FOOD_TYPE = RESTAURANT_URL + "listing/FoodType"
const val FETCH_FACILITIES = OUTLET_URL + "listing/Facility"
const val FETCH_CUISINES = RESTAURANT_URL + "listing/Cuisine"
const val FETCH_SOCIAL_MEDIA = OUTLET_URL + "listing/SocialMedia"

const val KNOWN_LANGUAGES_LISTING = LOCATION_URL + "listing/Language"
const val DESIGNATION_LISTING = USER_URL + "listing/Designation"

const val FETCH_COUNTRY = LOCATION_URL + "listing/Country"
const val FETCH_STATE = LOCATION_URL + "listing/State"
const val FETCH_CITY = LOCATION_URL + "listing/City"
const val FETCH_AREA = LOCATION_URL + "listing/Area"
const val FETCH_PINCODE = LOCATION_URL + "listing/PincodeData"

// Discount Masters
const val FETCH_DISCOUNT_CATEGORIES = DISCOUNT_URL + "listing/OutletDiscountCategory"
const val FETCH_DISCOUNT_MEMBERSHIP_PLAN = OUTLET_URL + "listing/OutletMembershipPlan"
const val FETCH_CRAVX_CARDS_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS = DISCOUNT_URL + "listing/CravxCard"
const val FETCH_HW_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS = DISCOUNT_URL + "listing/HWCard"
const val FETCH_IN_HOUSE_DISCOUNT_MEMBERSHIP_HOLDER_MASTERS = DISCOUNT_URL + "listing/InHousesCard"

// Discount URLs
const val FETCH_OUTLET_DISCOUNT_DETAILS_LIST = DISCOUNT_URL + "get/OutletDiscountDetails/OutletId"
const val FETCH_OUTLET_MEMBERSHIP_PLAN_MAPPING =
    OUTLET_URL + "get/OutletMembershipPlanMapping/OutletId"
//https://uat-api.checqk.com/edge-service/outlet-service/get/OutletMembershipPlanMapping/OutletId?outletId=61
const val FETCH_OUTLET_CORPORATE_MEMBERSHIP_OR_ONE_DASHBOARD =
    DISCOUNT_URL + "get/CorporateMembership/productIdAndoutletId"
const val SAVE_OUTLET_DISCOUNT_DETAILS = DISCOUNT_URL + "save/OutletDiscountDetails"

const val SAVE_RESTAURANT_PROFILE_IMAGE = RESTAURANT_URL + "save/RestaurantMaster/ProfileImage"
const val GET_RESTAURANT_PROFILE_IMAGE = RESTAURANT_URL + "get/RestaurantMaster/ProfileImage/Id"
const val GALLERY_IMAGE_CATEGORY = OUTLET_URL + "listing/GalleryImageCategory"
const val SAVE_GALLERY_IMAGES_BY_CATEGORY = OUTLET_URL + "save/OutletImageGallery"
const val GET_ALL_GALLERY_IMAGES = OUTLET_URL + "get/OutletImageGallery/outletId"
const val REMOVE_GALLERY_IMAGE_BY_IMAGE_ID = OUTLET_URL + "changeStatus/Image/ByDocumentId"
const val DISCARD_GALLERY_IMAGES_BY_CATEGORY =
    OUTLET_URL + "discardOutletImageGallery/ByGalleryImageCategoryIdAndOutletId"

const val MENU_IMAGE_CATEGORY = CATALOGUE_URL + "listing/CatalogueImageCategory"
const val SAVE_MENU_IMAGES_BY_CATEGORY = CATALOGUE_URL + "save/CatalogueImageGallery"
const val GET_ALL_MENU_IMAGES = CATALOGUE_URL + "get/CatalogueImageGallery/outletId"
const val REMOVE_MENU_IMAGE_BY_IMAGE_ID = CATALOGUE_URL + "changeStatus/CatalogueGalleryImage/ByDocumentId"
const val DISCARD_MENU_IMAGES_BY_CATEGORY =
    CATALOGUE_URL + "discardCatalogueImageGallery/ByCatalogueCategoryIdAndOutletId"


const val GET_ALL_MENU_CATEGORIES = CATALOGUE_URL + "get/CatalogueCategory/ByOutletId"
const val SAVE_MENU_CATEGORY = CATALOGUE_URL + "save/CatalogueCategory"
const val CHANGE_MENU_CATEGORY_STATUS = CATALOGUE_URL + "changeStatus/CatalogueCategory"
const val CHANGE_MENU_CATEGORY_SEQUENCE = CATALOGUE_URL + "save/CatalogueCategory/SequenceNo"

const val GET_ALL_MENU_SECTIONS = CATALOGUE_URL + "get/CatalogueSection/ByCatalogueCategoryId"
const val SAVE_MENU_SECTION = CATALOGUE_URL + "save/CatalogueSection"
const val CHANGE_MENU_SECTION_STATUS = CATALOGUE_URL + "changeStatus/CatalogueSection"
const val CHANGE_MENU_SECTION_SEQUENCE = CATALOGUE_URL + "save/CatalogueSection/SequenceNo"

const val GROUP_ROLE_SUB_ADMIN_LISTING = "listing/GroupRole/OutletSubAdmin"
const val SAVE_OUTLET_SUB_ADMIN = "save/OutletSubAdmin"
const val OUTLET_SUB_ADMIN_LISTING = "listing/OutletSubAdmin"
const val CHANGE_SUB_ADMIN_STATUS = USER_URL + "changeStatus/User/ById/{userId}/{status}"

const val GET_ALL_DISHES = CATALOGUE_URL + "get/Product/ByCatalogueCategoryId"
const val SAVE_DISH = CATALOGUE_URL + "save/Product"
const val CHANGE_DISH_STATUS = CATALOGUE_URL + "changeStatus/Product"
const val GET_ALL_DISH_FLAG = CATALOGUE_URL + "listing/ProductFlag"
const val SAVE_DISH_TIME = CATALOGUE_URL + "save/ProductTiming"
const val GET_DISH = CATALOGUE_URL + "get/Product/ById"
const val GET_DISH_TIMING = CATALOGUE_URL + "get/ProductTiming/productId"
const val CHANGE_DISH_SEQUENCE = CATALOGUE_URL + "save/Product/SequenceNo"
const val GET_ALL_INACTIVE_DISHES = CATALOGUE_URL + ""

const val GET_DRAWER_MENU = "get/MenuDrawer"

const val GET_STAFF_LISTING = SERVER_URL + "listing/OutletStaff"
const val SAVE_STAFF_USER = SERVER_URL + "save/OutletStaff"
const val GET_STAFF_USER_DETAILS = SERVER_URL + "get/User/ById/{userId}"
const val GET_STAFF_SAFETY_READINGS = STAFF_URL + "listing/StaffSecurityMeasures"
const val SAVE_STAFF_SAFETY_READINGS = STAFF_URL + "save/StaffSecurityMeasures"
const val GROUP_ROLE_STAFF_LISTING = "listing/GroupRole/OutletStaff"

const val GET_ACCESS_ROLE_LISTING = SERVER_URL + "listing/GroupRole/OutletSubAdmin"
const val GET_APPLICATION_MODULES_LISTING = SERVER_URL + "listing/ApplicationModules"
const val SAVE_ACCESS_ROLE = SERVER_URL + "save/GroupRole/OutletSubAdmin"
const val GET_ACCESS_ROLE = SERVER_URL + "get/GroupRole/ById"
const val CHANGE_ACCESS_ROLE_STATUS = SERVER_URL + "changeStatus/GroupRole"

const val LOGOUT_USER_URL = OAUTH_URL +"customLogout"
const val REFRESH_TOKEN = OAUTH_URL + "refreshToken"

const val GET_RESERVED_TABLE_LISTING = BOOKING_URL + "listing/Booking"
const val GET_TABLE_LISTING_BY_OUTLET_ID = RESTAURANT_URL + "listing/Table/ByOutletId"
const val CHANGE_BOOKING_STATUS = BOOKING_URL + "changeStatus/Booking"

const val SAVE_TABLE_LISTING = RESTAURANT_URL + "save/TableList"
const val GET_TABLE_LISTING = RESTAURANT_URL + "listing/Table"
