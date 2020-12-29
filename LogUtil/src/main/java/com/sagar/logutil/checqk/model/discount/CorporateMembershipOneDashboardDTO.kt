package com.astrika.checqk.model.discount

import java.io.Serializable

class CorporateMembershipOneDashboardDTO : Serializable {

    var corporateId: Long = 0
    var corporateName: String? = ""
    var membershipDetailsList: ArrayList<OneDashboardMembershipHolderDTO>? = null

    var selected = false
}

class OneDashboardMembershipHolderDTO : Serializable {

    var membershipId: Long = 0
    var corporateId: Long = 0
    var membershipName: String = ""
    var userMembershipTypeId: ArrayList<Long>? = null
    var selected = false

}
