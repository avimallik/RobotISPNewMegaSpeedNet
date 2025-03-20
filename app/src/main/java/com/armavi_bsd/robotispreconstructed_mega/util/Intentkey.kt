package com.armavi_bsd.robotispreconstructed_mega.util

data class Intentkey(
    val mikrotikIPIntentKey: String = "intent_res_mikrotik_ip",
    val mikrotikIDIntentKey: String = "intent_res_mikrotik_id",
    val agentIdIntentkey: String = "intent_res_agent_id",

    val posAgentNameIntentKey : String = "pos_agent_name_id",
    val posAgentAddressIntentKey : String = "pos_agent_address_id",
    val posAgentTotalDueIntentKey : String = "pos_agent_total_due_id",
    val posDateIntentKey : String = "pos_date_id",
    val posAmountIntentKey : String = "pos_amount_id",
    val posEntryUserNameIntentKey : String = "pos_entry_user_name_id",
    val posCusIDIntentKey : String = "pos_cus_id_id",
    val posMBIntentKey : String = "pos_mb_id",
    val posAgentMobileNoIntentKey : String = "pos_agent_mobile_id",
    val posPackageBill: String = "pos_package_bill_id",
    val posAccountIDIntentKey : String = "pos_account_id"

)
