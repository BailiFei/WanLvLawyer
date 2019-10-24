package com.lawyer.net;

import com.lawyer.BuildConfig;
import com.lawyer.util.UserCache;

/**
 @author wang
 on 2019/2/21 */

public class Url {

    /*   -------------     其他            --------------*/
    /** 阿里oss初始化地址 */
    public static final String OSS_DISTRIBUTE_TOKEN = "common/alioss/distribute_token.htm";

    /*   -------------     H5            --------------*/

    /** 用户-案例-详情 */
    public static final String caseExampleDetail = "caseExample/detail.htm";

    public static String h5CaseExampleDetail(int id) {
        return BuildConfig.SERVICE_HOST + caseExampleDetail + "?accessToken=" + UserCache.getAccessToken() + "&id=" + id;
    }

    /** 用户-公益项目-项目详情 */
    public static final String projectDetail = "project/detail.htm";

    public static String h5ProjectDetail(int id) {
        return BuildConfig.SERVICE_HOST + projectDetail + "?accessToken=" + UserCache.getAccessToken() + "&id=" + id;
    }

    /** 用户-法知识-知识详情 */
    public static final String caseKnowledgeDetail = "caseKnowledge/detail.htm";

    public static String h5CaseKnowledgeDetail(int id) {
        return BuildConfig.SERVICE_HOST + caseKnowledgeDetail + "?accessToken=" + UserCache.getAccessToken() + "&id=" + id;
    }

    /** 案件详情 */
    public static final String caseDetail = "case/caseDetail.htm";

    public static String h5CaseDetail(int id) {
        return BuildConfig.SERVICE_HOST + caseDetail + "?accessToken=" + UserCache.getAccessToken() + "&id=" + id;
    }

    /** 用户注册协议 */
    public static final String userAgreement = "http://down.wanlvonline.com/agreement/userAgreement.html";
    /** 隐私协议 */
    public static final String privacyAgreement = "http://down.wanlvonline.com/agreement/privacyAgreement.html";

    /*   -------------     API接口            --------------*/

    /** 用户注册 */
    public static final String user_register = "user/register.htm";
    /** 获取注册验证码 */
    public static final String user_checkCode = "user/checkCode.htm";
    /** 忘记密码 */
    public static final String user_forgetPwd = "user/forgetPwd.htm";
    /** 登录 */
    public static final String user_login = "user/login.htm";

    /** 用户端首页 */
    public static final String userIndex = "index/userIndex.htm";
    /** 用户-我的接口 */
    public static final String userQuery = "user/query.htm";
    /** 律师端首页 */
    public static final String lawyerIndex = "index/lawyerIndex.htm";

    /** 用户-案例 */
    public static final String caseExampleList = "caseExample/list.htm";

    /** 用户-法律短剧 */
    public static final String caseVideoList = "caseVideo/list.htm";
    /** 用户-获取短剧的案件类型 */
    public static final String caseVideoCaseType = "caseVideo/videoCaseType.htm";
    /** 用户-法律知识 */
    public static final String caseKnowledgeList = "caseKnowledge/list.htm";
    /** 用户-案件进程 */
    public static final String caseProgress = "case/caseProgress.htm";
    /** 用户-公益项目 */
    public static final String projectList = "project/list.htm";
    /** 用户-公益项目-项目进展 */
    public static final String projectProgressList = "project/progressList.htm";
    /** 用户-公益项目-捐款记录 */
    public static final String projectLogList = "project/logList.htm";
    /** 用户-公益项目-生成公益项目捐款订单 */
    public static final String orderCreateProjectOrder = "order/createProjectOrder.htm";
    /** 用户-我的-案件详情--生成案件支付费用订单 */
    public static final String orderCreateCaseOrder = "order/createCaseOrder.htm";
    /** 用户-我的-充值 */
    public static final String orderCreateCapitalOrder = "order/createCapitalOrder.htm";
    /** 畅捷支付 短信确认*/
    public static final String orderSMSConfirm = "order/chan/pay/smsConfirmOrder.htm";
    /** 重发验证码*/
    public static final String orderSMSResend = "order/chan/pay/smsResend.htm";
    /** 获取订单状态接口*/
    public static final String orderState = "order/getOrderState.htm";
    /** 我的进行中/已结束 */
    public static final String userMyCaseList = "user/myCaseList.htm";
    /** 律师我的-我的投标 */
    public static final String userMyCaseApplyList = "/user/myCaseApplyList.htm";

    /** 用户-我的-案件委托列表(待接洽/进行中/已完成) */
    public static final String caseCaseList = "case/caseList.htm";
    /** 律师投标 */
    public static final String caseApplyCase = "case/applyCase.htm";

    /** 共用-修改密码 */
    public static final String userModifyPassword = "user/modifyPassword.htm";
    /** 共用-修改昵称等用户信息 */
    public static final String userUpdateUserInfo = "user/updateUserInfo.htm";
    /** 获取安卓/ios版本信息 */
    public static final String getVersion = "version/getVersion.htm";
    /** 消息列表 */
    public static final String messageList = "message/list.htm";
    /** 更新视频播放量 */
    public static final String caseVideoUpdatePlayCount = "caseVideo/updatePlayCount.htm";
    /** 获取城市信息 */
    public static final String userFetchCity = "user/fetchCity.htm";
    /** 验证并绑定银行卡 */
    public static final String doBankCardAuth = "capital/doBankCardAuth.htm";
    /** 获取银行卡详情 */
    public static final String getBankCardDetail = "capital/getBankCardDetail.htm";
    /** 资金模块-提现 */
    public static final String withdraw = "capital/withdraw.htm";
    /** 用余额支付案件 */
    public static final String payCaseByCapital = "capital/payCaseByCapital.htm";
    /** 用余额支付公益项目 */
    public static final String payProjectByCapital = "capital/payProjectByCapital.htm";
    /** 律师申请 */
    public static final String lawyerApply = "lawyer/apply.htm";
    /** 律师申请详情App */
    public static final String lawyerApplyDetail = "lawyer/applyDetail.htm";
    /** 父节点查询子节点数据，如查询某个省份下面的城市，或查询某个城市下面的区县，如parentId为空或者0，则查询所有的省份 */
    public static final String district = "district/queryByParentId.htm";


}
