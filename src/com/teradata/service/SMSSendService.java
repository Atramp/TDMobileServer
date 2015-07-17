package com.teradata.service;

import com.teradata.common.config.Configuration;
import com.teradata.common.utils.CommonUtil;
import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

public class SMSSendService {
    private static final String PROP_SERVICE_URL = "SMS_SERVICE_URL";
    private static final String PROP_SERVICE_NAME = "SMS_SERVICE_NAME";
    private static final String PROP_NAMESPACE = "WEB_SERVICE_NAMESPACE";
    private static final String PROP_SMS_VERIFICATION_MSG = "SMS_VERIFICATION_MSG";
    private static final String PROP_SMS_RESET_PASSWORD_MSG = "SMS_RESET_PASSWORD_MSG";

    private static Call call = null;

    static {
        String namespace = Configuration.get(PROP_NAMESPACE);
        String serviceName = Configuration.get(PROP_SERVICE_NAME);
        try {
            call = ((Call) new Service().createCall());
            call.setTargetEndpointAddress(new URL(Configuration.get(PROP_SERVICE_URL)));
            call.setOperationName(new QName(namespace, serviceName));
            call.setSOAPActionURI(namespace + "/" + serviceName);
            call.addParameter(new QName(namespace, "mobile"), Constants.XSD_STRING, ParameterMode.IN);
            call.addParameter(new QName(namespace, "content"), Constants.XSD_STRING, ParameterMode.IN);
            call.setReturnType(Constants.XSD_BOOLEAN);
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送短信
     *
     * @param mobile
     * @param content
     * @throws Exception
     */
    public static void sendSMS(String mobile, String content) {
        try {
            // 调用服务
            call.invoke(new Object[]{mobile, content});
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送登录验证码
     *
     * @param mobile
     */
    public static String sendVerificationCode(String mobile) {
        String verificationCode = CommonUtil.getRandom(6);
        sendSMS(mobile, String.format(Configuration.get(PROP_SMS_VERIFICATION_MSG), verificationCode));
        return verificationCode;
    }

    /**
     * 发送重置密码的验证码
     *
     * @param mobile
     */
    public static void SendSMSForPassword(String mobile) {
        sendSMS(mobile, String.format(Configuration.get(PROP_SMS_RESET_PASSWORD_MSG), CommonUtil.getRandom(6)));
    }

}
