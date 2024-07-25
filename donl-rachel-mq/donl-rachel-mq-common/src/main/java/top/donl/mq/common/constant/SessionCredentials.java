package top.donl.mq.common.constant;

import top.donl.mq.common.enums.ONSChannel;

import java.io.File;
import java.nio.charset.Charset;

/**
 * <p></p>
 *
 * @author Crux
 */


public class SessionCredentials {
    public static final Charset CHARSET = Charset.forName("UTF-8");
    public static final String AccessKey = "AccessKey";
    public static final String SecretKey = "SecretKey";
    public static final String Signature = "Signature";
    public static final String SecurityToken = "SecurityToken";
    public static final String SignatureMethod = "SignatureMethod";
    public static final String ONSChannelKey = "OnsChannel";

    public static final String KeyFile = System.getProperty("rocketmq.client.keyFile",
            System.getProperty("user.home") + File.separator + "onskey");

    private String accessKey;
    private String secretKey;
    private String securityToken;
    private String signature;
    private String signatureMethod;
    private ONSChannel onsChannel = ONSChannel.ALIYUN;
}
