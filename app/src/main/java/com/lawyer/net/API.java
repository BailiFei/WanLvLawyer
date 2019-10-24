package com.lawyer.net;


import com.google.gson.JsonObject;
import com.lawyer.entity.AccountBean;
import com.lawyer.entity.AppVersionBean;
import com.lawyer.entity.BankCardBean;
import com.lawyer.entity.CapitalWithdrawBean;
import com.lawyer.entity.CaseExampleBean;
import com.lawyer.entity.CaseExampleExt;
import com.lawyer.entity.CaseKnowledgeExt;
import com.lawyer.entity.CaseProgressBean;
import com.lawyer.entity.CaseTypeBean;
import com.lawyer.entity.CaseVideoExt;
import com.lawyer.entity.CityBean;
import com.lawyer.entity.DataExt;
import com.lawyer.entity.DistrictBean;
import com.lawyer.entity.LawyerApplyBean;
import com.lawyer.entity.LawyerIndexBean;
import com.lawyer.entity.MessageBeanExt;
import com.lawyer.entity.User;
import com.lawyer.entity.UserCaseBean;
import com.lawyer.entity.UserCaseBeanExt;
import com.lawyer.entity.UserIndexBean;
import com.lawyer.entity.WelfareBeanExt;
import com.lawyer.entity.WelfareRecordBean;
import com.lawyer.enums.CaseStateEnum;
import com.lawyer.enums.PayStateEnum;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 @author wang
 on 2019/2/21 */

public interface API {
    /** 用户注册 */
    @FormUrlEncoded
    @POST(Url.user_register)
    Observable<Result<AccountBean>> register(@Field("mobileNo") String mobileNo, @Field("checkCode") String checkCode, @Field("password") String password);

    /** 忘记密码 */
    @FormUrlEncoded
    @POST(Url.user_forgetPwd)
    Observable<Result<AccountBean>> forgetPwd(@Field("mobileNo") String mobileNo, @Field("checkCode") String checkCode, @Field("password") String password);

    /** 获取验证码 */
    @FormUrlEncoded
    @POST(Url.user_checkCode)
    Observable<Result> checkCode(@Field("mobileNo") String mobileNo, @Field("type") String type);

    /** 用户登录 */
    @FormUrlEncoded
    @POST(Url.user_login)
    Observable<Result<AccountBean>> login(@Field("mobileNo") String mobileNo, @Field("password") String password);

    /** 用户首页 */
//    @FormUrlEncoded
    @GET(Url.userIndex)
    Observable<Result<UserIndexBean>> userIndex(/*@Field("accessToken") String accessToken*/);

    /** 用户首页 */
    @FormUrlEncoded
    @POST(Url.userQuery)
    Observable<Result<User>> userQuery(@Field("accessToken") String accessToken);

    /** 律师首页 */
    @FormUrlEncoded
    @POST(Url.lawyerIndex)
    Observable<Result<LawyerIndexBean>> lawyerIndex(@Field("accessToken") String accessToken, @Field("pageNum") int pageNum, @Field("pageSize") int pageSize);


    /** 用户-案例 */
    @FormUrlEncoded
    @POST(Url.caseExampleList)
    Observable<Result<CaseExampleExt>> caseExampleList(/*@Field("accessToken") String accessToken,*/ @Field("pageNum") int pageNum, @Field("caseType") Integer caseType);

    /** 用户-案例 */
    @FormUrlEncoded
    @POST(Url.caseExampleDetail)
    Observable<Result<CaseExampleBean>> caseExampleDetail(@Field("accessToken") String accessToken, @Field("id") int id);

    /** 用户-法律短剧 */
    @FormUrlEncoded
    @POST(Url.caseVideoList)
    Observable<Result<CaseVideoExt>> caseVideoList(/*@Field("accessToken") String accessToken,*/ @Field("pageNum") int pageNum, @Field("caseType") Integer caseType);

    /** 用户-法律短剧 */
    @FormUrlEncoded
    @POST(Url.caseVideoCaseType)
    Observable<Result<List<CaseTypeBean>>> caseVideoCaseType(/*@Field("accessToken") String accessToken,*/@Field("pageNum") int pageNum);

    /** 用户-法律知识 */
    @FormUrlEncoded
    @POST(Url.caseKnowledgeList)
    Observable<Result<CaseKnowledgeExt>> caseKnowledgeList(/*@Field("accessToken") String accessToken,*/ @Field("pageNum") int pageNum);

    @FormUrlEncoded
    @POST(Url.caseProgress)
    Observable<Result<List<CaseProgressBean>>> caseProgress(@Field("accessToken") String accessToken, @Field("caseId") int caseId, @Field("pageNum") int pageNum);

    /** 用户-公益列表 */
    @FormUrlEncoded
    @POST(Url.projectList)
    Observable<Result<WelfareBeanExt>> projectList(/*@Field("accessToken") String accessToken,*/ @Field("pageNum") int pageNum);

    /** 用户-公益项目-项目进展 */
    @FormUrlEncoded
    @POST(Url.projectProgressList)
    Observable<Result<JsonObject>> projectProgressList(/*@Field("accessToken") String accessToken, */@Field("projectId") int projectId, @Field("pageNum") int pageNum);

    /** 用户-公益项目-捐款记录 */
    @FormUrlEncoded
    @POST(Url.projectLogList)
    Observable<Result<DataExt<WelfareRecordBean>>> projectLogList(/*@Field("accessToken") String accessToken,*/ @Field("projectId") int projectId, @Field("pageNum") int pageNum);

    /** 用户-我的-案件委托列表(待接洽/进行中/已完成) */
    @FormUrlEncoded
    @POST(Url.caseCaseList)
    Observable<Result<List<UserCaseBean>>> caseCaseList(@Field("accessToken") String accessToken, @Field("caseState") CaseStateEnum stateEnum, @Field("pageNum") int pageNum);

    /** 律师投标 */
    @FormUrlEncoded
    @POST(Url.caseApplyCase)
    Observable<Result> caseApplyCase(@Field("accessToken") String accessToken, @Field("caseId") int caseId);

    /** 共用-修改密码 */
    @FormUrlEncoded
    @POST(Url.userModifyPassword)
    Observable<Result> userModifyPassword(@Field("accessToken") String accessToken, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    /** 共用-修改昵称 */
    @FormUrlEncoded
    @POST(Url.userUpdateUserInfo)
    Observable<Result<User>> userUpdateUserInfo(@Field("accessToken") String accessToken, @Field("nickName") String nickName);


    /** 共用-修改昵称 */
    @FormUrlEncoded
    @POST(Url.userUpdateUserInfo)
    Observable<Result<User>> userUpdateUserInfo(@FieldMap Map<String, Object> userMap);

    /** 获取安卓/ios版本信息 */
    @FormUrlEncoded
    @POST(Url.getVersion)
    Observable<Result<AppVersionBean>> getVersion(@Field("device") String device);

    @FormUrlEncoded
    @POST(Url.userMyCaseList)
    Observable<Result<List<UserCaseBeanExt>>> userMyCaseList(@Field("accessToken") String accessToken, @Field("pageNum") Integer pageNum, @Field("pageSize") Integer pageSize, @Field("caseState") String caseState);

    @FormUrlEncoded
    @POST(Url.userMyCaseApplyList)
    Observable<Result<List<UserCaseBeanExt>>> userMyCaseApplyList(@Field("accessToken") String accessToken, @Field("pageNum") Integer pageNum, @Field("pageSize") Integer pageSize);

    /** 消息列表 */
    @FormUrlEncoded
    @POST(Url.messageList)
    Observable<Result<MessageBeanExt>> messageList(@Field("accessToken") String accessToken, @Field("pageNum") Integer pageNum, @Field("pageSize") Integer pageSize);

    /** 更新视频播放次数 */
    @FormUrlEncoded
    @POST(Url.caseVideoUpdatePlayCount)
    Observable<Result> caseVideoUpdatePlayCount(@Field("id") int id);

    /** 获取城市信息 */
    @POST(Url.userFetchCity)
    Observable<Result<CityBean>> userFetchCity();

    /** 验证并绑定银行卡 */
    @FormUrlEncoded
    @POST(Url.doBankCardAuth)
    Observable<Result> doBankCardAuth(@Field("accessToken") String accessToken, @Field("accountNo") String accountNo,
                                      @Field("idCard") String idCard, @Field("mobile") String mobile,
                                      @Field("name") String name);

    @FormUrlEncoded
    @POST(Url.getBankCardDetail)
    Observable<Result<BankCardBean>> getBankCardDetail(@Field("accessToken") String accessToken);

    @FormUrlEncoded
    @POST(Url.withdraw)
    Observable<Result<CapitalWithdrawBean>> withdraw(@Field("accessToken") String accessToken, @Field("amount") String amount);

    /** 用余额支付公益项目 */
    @FormUrlEncoded
    @POST(Url.payProjectByCapital)
    Observable<Result<String>> payProjectByCapital(@Field("accessToken") String accessToken, @Field("projectId") int projectId, @Field("money") BigDecimal money);

    /** 用余额支付案件 */
    @FormUrlEncoded
    @POST(Url.payCaseByCapital)
    Observable<Result<String>> payCaseByCapital(@Field("accessToken") String accessToken, @Field("caseId") int caseId);

    /** 律师申请 */
    @FormUrlEncoded
    @POST(Url.lawyerApply)
    Observable<Result> lawyerApply(@FieldMap Map<String, Object> map);

    /** 律师申请详情 */
    @FormUrlEncoded
    @POST(Url.lawyerApplyDetail)
    Observable<Result<LawyerApplyBean>> lawyerApplyDetail(@Field("accessToken") String accessToken);

    /** 律师申请详情 */
    @FormUrlEncoded
    @POST(Url.district)
    Observable<Result<List<DistrictBean>>> district(@Field("parentId") int parentId);


    /**
     下单接口
     一元公益项目捐款订单： projectId ,money,payType
     用户-我的-案件详情--生成案件支付费用订单: caseId,money,payType
     */
    @FormUrlEncoded
    @POST("{path}")
    Observable<JsonObject> orderCreate(@Path("path") String path, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(Url.orderSMSConfirm)
    Observable<Result> orderSMSConfirm(@Field("orderTrxId") String orderTrxId, @Field("smsCode") String smsCode);

    @FormUrlEncoded
    @POST(Url.orderSMSResend)
    Observable<Result<String>> orderSMSResend(@Field("orderTrxId") String orderTrxId);

    @FormUrlEncoded
    @POST(Url.orderState)
    Observable<Result<PayStateEnum>> orderState(@Field("accessToken") String accessToken,@Field("orderSn") String orderSn);
}
