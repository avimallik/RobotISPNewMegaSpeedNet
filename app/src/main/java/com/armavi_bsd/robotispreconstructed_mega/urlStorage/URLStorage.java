package com.armavi_bsd.robotispreconstructed_mega.urlStorage;

/**
 * Created by Arm Avi on 9/7/2020.
 */

public class URLStorage {

    String httpStd = "http://";
    String httpsStd = "https://";
//    String baseUrl = "192.168.0.122/mega";
    String baseUrl = "meganew.robotispsoft.net";
//    String baseUrl = "roms.robotispsoft.net";
//    String baseUrl = "192.168.0.132/mitisp";
//    String baseUrl = "192.168.0.132/newisp";
//    String baseUrl = "newisp.bsdbd.xyz";
    String slash = "/";
    String loginIntercect = "/rest_api_mob_dx/robotispuserlogin.php";
    String userEntryIntersect = "/rest_api_mob_dx/robotispagententry.php";
    String zoneIntersect = "/rest_api_mob_dx/robotispzone.php";
    String paymentIntersect = "";
    String zoneInsertIntersect = "/rest_api_mob_dx/robotispzoneinsert.php";
    String billViewIntersect = "/rest_api_mob_dx/robotispviewpayment.php";
    String billpaymIntersect = "/rest_api_mob_dx/robotisppaydatainsert.php";
    String billpaymentIntersect = "/rest_api_mob_dx/robotisppaymentinsert.php";
    String employeedataIntersect = "/rest_api_mob_dx/robotispeployeefetch.php";
    String customerdataIntersect = "/rest_api_mob_dx/robotispcustomerdatafetch.php";
    String templetedataIntersect = "/rest_api_mob_dx/robotispcomplaintemplate.php";
    String complanviewIntersect = "/rest_api_mob_dx/robotispcomplainview.php";
    String complainupdateIntersect = "/rest_api_mob_dx/robotispcomplainupdate.php";
    String complaindataInsertIntersect = "/rest_api_mob_dx/robotispcomplaininsert.php";
    String userviewIntersect = "/rest_api_mob_dx/robotispuser.php";
    String smsSendIntersect = "/rest_api_mob_dx/sms_android.php";
    String customerProfilerIntersect = "/rest_api_mob_dx/robotispagentdisplay.php";
    String profileDataIntersect = "/rest_api_mob_dx/mikrotik_access.php";
    String expenseIntersect = "/rest_api_mob_dx/robotispexpense.php";
    String expenseDeleteIntersect = "/rest_api_mob_dx/robotispexpensedelete.php";
    String accountHeadListIntersect = "/rest_api_mob_dx/accountheadlist.php";
    String expenseEditIntersect = "/rest_api_mob_dx/robotispexpenseedit.php";
    String expenseInsertIntersect = "/rest_api_mob_dx/robotispexpenseinsert.php";
    String accountheadInsertIntersect = "/rest_api_mob_dx/accountheadinsert.php";
    String accountheadDeleteIntersect = "/rest_api_mob_dx/accountheaddelete.php";
    String accountSubHeadListEndpoint = "/rest_api_mob_dx/accountSubHeadList.php";
    String accountSubHeadInsertEndpoint = "/rest_api_mob_dx/accountSubHeadInsert.php";
    String imageAssetDir = "/assets/images/";
    String mikrottikConnectionCheck = "/rest_api_mob_dx/mikrotikConnectionCheck.php";
    String mikrotikList = "/rest_api_mob_dx/mikrotikList.php";
    String complainView = "/rest_api_mob_dx/robotispcomplainview.php";
    String zoneSelection = "/rest_api_mob_dx/zoneSelection.php?parent_id=";
    String zoneSeletionWithChildCount = "/rest_api_mob_dx/zoneSelectionWithChildCount.php";
    String subZoneWithChildCount = "/rest_api_mob_dx/subZoneWithChildCount.php?parent_id=";
    String complainSubmitEndpoint = "/rest_api_mob_dx/testComplainInput.php";
    String billingPersonEndpoint = "/rest_api_mob_dx/billingPersons.php";
    String packageEndpoint = "/rest_api_mob_dx/robotispPackage.php";
    String areaEndpoint = "/rest_api_mob_dx/subZoneWithChildCount.php?parent_id=";
    String subZoneEndpoint = "/rest_api_mob_dx/subZoneWithChildCount.php?parent_id=";
    String agentSubmitEndpoint = "/rest_api_mob_dx/testAgInputTuned.php";
    String testBillPaySubmitEndpoint = "/rest_api_mob_dx/testBillPay.php";
    String complainStatusChangeEndpoint = "/rest_api_mob_dx/robotispComplainStatusUpdate.php";
    String complainTypeEndpoint = "/rest_api_mob_dx/complainType.php";
    String customerDialogResourceEndpoint = "/rest_api_mob_dx/testCustomerView.php";
    String employeeMinimalViewEndpoint = "/rest_api_mob_dx/robotispEmployeeMinimalView.php";
    String mikrotikSingleIPCheckStatusEndpoint = "/rest_api_mob_dx/singleMikrotikTest.php?mik_ip=";
    String billInfoEndpoint = "/rest_api_mob_dx/billinfo.php?token=";
    String posPrinting = "/rest_api_mob_dx/pos_print.php?token=";
    String lastAgentIDAs = "/rest_api_mob_dx/lastAgentIDS.php";
    String accountStatementOverviewEndpoint = "/rest_api_mob_dx/viewAccountStatementOverview.php?userid=";
    String posPrintAccountStatementEndpoint = "/rest_api_mob_dx/posPrintAccountStatement.php?userid=";

    public String getPosPrintAccountStatementEndpoint() {
        return posPrintAccountStatementEndpoint;
    }

    public void setPosPrintAccountStatementEndpoint(String posPrintAccountStatementEndpoint) {
        this.posPrintAccountStatementEndpoint = posPrintAccountStatementEndpoint;
    }

    public String getAccountStatementOverviewEndpoint() {
        return accountStatementOverviewEndpoint;
    }

    public void setAccountStatementOverviewEndpoint(String accountStatementOverviewEndpoint) {
        this.accountStatementOverviewEndpoint = accountStatementOverviewEndpoint;
    }

    public String getLastAgentIDAs() {
        return lastAgentIDAs;
    }

    public void setLastAgentIDAs(String lastAgentIDAs) {
        this.lastAgentIDAs = lastAgentIDAs;
    }

    public String getPosPrinting() {
        return posPrinting;
    }

    public void setPosPrinting(String posPrinting) {
        this.posPrinting = posPrinting;
    }

    public String getBillInfoEndpoint() {
        return billInfoEndpoint;
    }

    public void setBillInfoEndpoint(String billInfoEndpoint) {
        this.billInfoEndpoint = billInfoEndpoint;
    }

    public String getMikrotikSingleIPCheckStatusEndpoint() {
        return mikrotikSingleIPCheckStatusEndpoint;
    }

    public void setMikrotikSingleIPCheckStatusEndpoint(String mikrotikSingleIPCheckStatusEndpoint) {
        this.mikrotikSingleIPCheckStatusEndpoint = mikrotikSingleIPCheckStatusEndpoint;
    }

    public String getEmployeeMinimalViewEndpoint() {
        return employeeMinimalViewEndpoint;
    }

    public void setEmployeeMinimalViewEndpoint(String employeeMinimalViewEndpoint) {
        this.employeeMinimalViewEndpoint = employeeMinimalViewEndpoint;
    }

    public String getCustomerDialogResourceEndpoint() {
        return customerDialogResourceEndpoint;
    }

    public void setCustomerDialogResourceEndpoint(String customerDialogResourceEndpoint) {
        this.customerDialogResourceEndpoint = customerDialogResourceEndpoint;
    }

    public String getComplainTypeEndpoint() {
        return complainTypeEndpoint;
    }

    public void setComplainTypeEndpoint(String complainTypeEndpoint) {
        this.complainTypeEndpoint = complainTypeEndpoint;
    }

    public String getComplainStatusChangeEndpoint() {
        return complainStatusChangeEndpoint;
    }

    public void setComplainStatusChangeEndpoint(String complainStatusChangeEndpoint) {
        this.complainStatusChangeEndpoint = complainStatusChangeEndpoint;
    }

    public String getTestBillPaySubmitEndpoint() {
        return testBillPaySubmitEndpoint;
    }

    public void setTestBillPaySubmitEndpoint(String testBillPaySubmitEndpoint) {
        this.testBillPaySubmitEndpoint = testBillPaySubmitEndpoint;
    }

    public String getAgentSubmitEndpoint() {
        return agentSubmitEndpoint;
    }

    public void setAgentSubmitEndpoint(String agentSubmitEndpoint) {
        this.agentSubmitEndpoint = agentSubmitEndpoint;
    }

    public String getSubZoneEndpoint() {
        return subZoneEndpoint;
    }

    public void setSubZoneEndpoint(String subZoneEndpoint) {
        this.subZoneEndpoint = subZoneEndpoint;
    }

    public String getPackageEndpoint() {
        return packageEndpoint;
    }

    public void setPackageEndpoint(String packageEndpoint) {
        this.packageEndpoint = packageEndpoint;
    }

    public String getAreaEndpoint() {
        return areaEndpoint;
    }

    public void setAreaEndpoint(String areaEndpoint) {
        this.areaEndpoint = areaEndpoint;
    }

    public String getBillingPersonEndpoint() {
        return billingPersonEndpoint;
    }

    public void setBillingPersonEndpoint(String billingPersonEndpoint) {
        this.billingPersonEndpoint = billingPersonEndpoint;
    }

    public String getComplainSubmitEndpoint() {
        return complainSubmitEndpoint;
    }

    public void setComplainSubmitEndpoint(String complainSubmitEndpoint) {
        this.complainSubmitEndpoint = complainSubmitEndpoint;
    }

    public String getSubZoneWithChildCount() {
        return subZoneWithChildCount;
    }

    public void setSubZoneWithChildCount(String subZoneWithChildCount) {
        this.subZoneWithChildCount = subZoneWithChildCount;
    }

    public String getZoneSeletionWithChildCount() {
        return zoneSeletionWithChildCount;
    }

    public void setZoneSeletionWithChildCount(String zoneSeletionWithChildCount) {
        this.zoneSeletionWithChildCount = zoneSeletionWithChildCount;
    }

    public String getZoneSelection() {
        return zoneSelection;
    }

    public void setZoneSelection(String zoneSelection) {
        this.zoneSelection = zoneSelection;
    }

    public String getAccountheadDeleteIntersect() {
        return accountheadDeleteIntersect;
    }

    public void setAccountheadDeleteIntersect(String accountheadDeleteIntersect) {
        this.accountheadDeleteIntersect = accountheadDeleteIntersect;
    }

    public String getAccountheadInsertIntersect() {
        return accountheadInsertIntersect;
    }

    public void setAccountheadInsertIntersect(String accountheadInsertIntersect) {
        this.accountheadInsertIntersect = accountheadInsertIntersect;
    }

    public String getExpenseInsertIntersect() {
        return expenseInsertIntersect;
    }

    public void setExpenseInsertIntersect(String expenseInsertIntersect) {
        this.expenseInsertIntersect = expenseInsertIntersect;
    }

    public String getExpenseEditIntersect() {
        return expenseEditIntersect;
    }

    public void setExpenseEditIntersect(String expenseEditIntersect) {
        this.expenseEditIntersect = expenseEditIntersect;
    }

    public String getAccountHeadListIntersect() {
        return accountHeadListIntersect;
    }

    public void setAccountHeadListIntersect(String accountHeadListIntersect) {
        this.accountHeadListIntersect = accountHeadListIntersect;
    }

    public String getExpenseDeleteIntersect() {
        return expenseDeleteIntersect;
    }

    public void setExpenseDeleteIntersect(String expenseDeleteIntersect) {
        this.expenseDeleteIntersect = expenseDeleteIntersect;
    }

    public String getExpenseIntersect() {
        return expenseIntersect;
    }

    public void setExpenseIntersect(String expenseIntersect) {
        this.expenseIntersect = expenseIntersect;
    }

    public String getCustomerProfilerIntersect() {
        return customerProfilerIntersect;
    }

    public void setCustomerProfilerIntersect(String customerProfilerIntersect) {
        this.customerProfilerIntersect = customerProfilerIntersect;
    }

    public String getSmsSendIntersect() {
        return smsSendIntersect;
    }

    public void setSmsSendIntersect(String smsSendIntersect) {
        this.smsSendIntersect = smsSendIntersect;
    }

    public String getComplainupdateIntersect() {
        return complainupdateIntersect;
    }

    public void setComplainupdateIntersect(String complainupdateIntersect) {
        this.complainupdateIntersect = complainupdateIntersect;
    }

    public String getComplanviewIntersect() {
        return complanviewIntersect;
    }

    public void setComplanviewIntersect(String complanviewIntersect) {
        this.complanviewIntersect = complanviewIntersect;
    }

    public String getComplaindataInsertIntersect() {
        return complaindataInsertIntersect;
    }

    public void setComplaindataInsertIntersect(String complaindataInsertIntersect) {
        this.complaindataInsertIntersect = complaindataInsertIntersect;
    }


    public String getEmployeedataIntersect() {
        return employeedataIntersect;
    }

    public void setEmployeedataIntersect(String employeedataIntersect) {
        this.employeedataIntersect = employeedataIntersect;
    }

    public String getCustomerdataIntersect() {
        return customerdataIntersect;
    }

    public void setCustomerdataIntersect(String customerdataIntersect) {
        this.customerdataIntersect = customerdataIntersect;
    }

    public String getTempletedataIntersect() {
        return templetedataIntersect;
    }

    public void setTempletedataIntersect(String templetedataIntersect) {
        this.templetedataIntersect = templetedataIntersect;
    }

    public String getBillpaymentIntersect() {
        return billpaymentIntersect;
    }

    public void setBillpaymentIntersect(String billpaymentIntersect) {
        this.billpaymentIntersect = billpaymentIntersect;
    }

    public String getBillpaymIntersect() {
        return billpaymIntersect;
    }

    public void setBillpaymIntersect(String billpaymIntersect) {
        this.billpaymIntersect = billpaymIntersect;
    }

    public String getHttpStd() {
        return httpStd;
    }

    public void setHttpStd(String httpStd) {
        this.httpStd = httpStd;
    }

    public String getSlash() {
        return slash;
    }

    public void setSlash(String slash) {
        this.slash = slash;
    }

    public String getLoginIntercect() {
        return loginIntercect;
    }

    public void setLoginIntercect(String loginIntercect) {
        this.loginIntercect = loginIntercect;
    }

    public String getUserEntryIntersect() {
        return userEntryIntersect;
    }

    public void setUserEntryIntersect(String userEntryIntersect) {
        this.userEntryIntersect = userEntryIntersect;
    }

    public String getPaymentIntersect() {
        return paymentIntersect;
    }

    public void setPaymentIntersect(String paymentIntersect) {
        this.paymentIntersect = paymentIntersect;
    }

    public String getZoneIntersect() {
        return zoneIntersect;
    }

    public void setZoneIntersect(String zoneIntersect) {
        this.zoneIntersect = zoneIntersect;
    }

    public String getZoneInsertIntersect() {
        return zoneInsertIntersect;
    }

    public String getBillViewIntersect() {
        return billViewIntersect;
    }

    public void setBillViewIntersect(String billViewIntersect) {
        this.billViewIntersect = billViewIntersect;
    }

    public void setZoneInsertIntersect(String zoneInsertIntersect) {
        this.zoneInsertIntersect = zoneInsertIntersect;
    }

    public String getUserviewIntersect() {
        return userviewIntersect;
    }

    public void setUserviewIntersect(String userviewIntersect) {
        this.userviewIntersect = userviewIntersect;
    }

    public String getProfileDataIntersect() {
        return profileDataIntersect;
    }

    public void setProfileDataIntersect(String profileDataIntersect) {
        this.profileDataIntersect = profileDataIntersect;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getHttpsStd() {
        return httpsStd;
    }

    public void setHttpsStd(String httpsStd) {
        this.httpsStd = httpsStd;
    }

    public String getImageAssetDir() {
        return imageAssetDir;
    }

    public void setImageAssetDir(String imageAssetDir) {
        this.imageAssetDir = imageAssetDir;
    }

    public String getAccountSubHeadListEndpoint() {
        return accountSubHeadListEndpoint;
    }

    public void setAccountSubHeadListEndpoint(String accountSubHeadListEndpoint) {
        this.accountSubHeadListEndpoint = accountSubHeadListEndpoint;
    }

    public String getAccountSubHeadInsertEndpoint() {
        return accountSubHeadInsertEndpoint;
    }

    public void setAccountSubHeadInsertEndpoint(String accountSubHeadInsertEndpoint) {
        this.accountSubHeadInsertEndpoint = accountSubHeadInsertEndpoint;
    }

    public String getMikrottikConnectionCheck() {
        return mikrottikConnectionCheck;
    }

    public void setMikrottikConnectionCheck(String mikrottikConnectionCheck) {
        this.mikrottikConnectionCheck = mikrottikConnectionCheck;
    }

    public String getComplainView() {
        return complainView;
    }

    public void setComplainView(String complainView) {
        this.complainView = complainView;
    }

    public String getMikrotikList() {
        return mikrotikList;
    }

    public void setMikrotikList(String mikrotikList) {
        this.mikrotikList = mikrotikList;
    }
}
