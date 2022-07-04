package com.ljx.utils;


// This file is auto-generated, don't edit it. Thanks.
//package com.aliyun.sample;
//
//        import com.aliyun.tea.*;
//        import com.aliyun.dm20151123.*;
//        import com.aliyun.dm20151123.models.*;
//        import com.aliyun.teaopenapi.*;
//        import com.aliyun.teaopenapi.models.*;
//        import com.aliyun.teautil.*;
//        import com.aliyun.teautil.models.*;

import com.aliyun.dm20151123.models.SingleSendMailRequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.ljx.utils.extend.AliyunResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    @Autowired
    public AliyunResource aliyunResource;

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public  com.aliyun.dm20151123.Client createClient(String accessKeyId,String accessKeySecret) throws Exception {

        Config config = new Config()
                // 您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dm.aliyuncs.com";
        return new com.aliyun.dm20151123.Client(config);
    }

    public void sendEmail(String email,String code) throws Exception {
        String accessKeyId = aliyunResource.getAccessKeyID();
        String accessKeySecret = aliyunResource.getAccessKeySecret();
        com.aliyun.dm20151123.Client client = createClient(accessKeyId, accessKeySecret);
        SingleSendMailRequest singleSendMailRequest = new SingleSendMailRequest()
                .setTextBody("Verification code is: "+code+". Welcome to Southampton’s News Forum~")
                .setFromAlias("Soton Forum")
                .setSubject("Soton Verification Code")
                .setToAddress(email)
                .setAccountName("forum@code.uk.lijiexi.com")
                .setAddressType(1)
                .setReplyAddress("1561256741@qq.com")
                .setReplyToAddress(true);
        RuntimeOptions runtime = new RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.singleSendMailWithOptions(singleSendMailRequest, runtime);
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }
}