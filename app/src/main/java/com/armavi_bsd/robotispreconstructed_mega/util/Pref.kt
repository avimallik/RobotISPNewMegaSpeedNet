package com.armavi_bsd.robotispreconstructed_mega.util

data class Pref(
    val prefMikrotikCheckStatus: String = "MIKROTIKCHECKSTATUS",
    val prefIsloggedIn: String = "ISLOGGEDIN",
    val prefUserCred: String = "USER_CREDENTIALS",
    val prefMikrotikID: String = "MIKROTIK_ID_PREF",
    val prefMikrotikIP: String = "MIKROTIK_IP_PREF",
    val prefUserName: String = "USERNAME",
    val prefUserID: String = "USER_ID",
    val prefFullNameID: String = "EXTRA_NAME_ID",
    val prefEmailID: String = "EMAIL_ID",
    val prefAddressID: String = "ADDRESS_ID",
    val prefImagePathID: String = "IMAGE_PATH_ID",
    val prefPhoneID: String = "PHONE_ID",
    val prefAdminTypeID: String = "ADMIN_TYPE_ID",
    val preAdminPasswordID: String = "PASSWORD",
    val prefPrintingDeviceMAC: String = "PRINTING_DEVICE_MAC"
)
