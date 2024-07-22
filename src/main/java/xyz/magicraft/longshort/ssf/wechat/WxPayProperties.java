package xyz.magicraft.longshort.ssf.wechat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.Value;

@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxPayProperties {
    /**
     * 设置微信公众号或者小程序等的appid
     */
    private String appId;
    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 证书相对路径
     */
    private String privateKeyPath;

    /**
     * 证书相对路径
     */
    private String privateCertPath;

    /**
     * 下单回调地址
     */
    private String returnUrl;

    /**
     * 退款回调地址
     */
    private String refundUrl;


    /**
     * apiV3key
     */
    private String apiV3key;


}
