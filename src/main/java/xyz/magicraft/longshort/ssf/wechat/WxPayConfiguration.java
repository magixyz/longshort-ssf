package xyz.magicraft.longshort.ssf.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

@Configuration
@ConditionalOnClass(WxPayService.class)
public class WxPayConfiguration {
    @Autowired
    private  WxPayProperties properties;

    @Autowired
    public WxPayConfiguration(WxPayProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxService() {
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig());
        return wxPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(this.properties.getAppId());
        payConfig.setMchId(this.properties.getMchId());
        payConfig.setPrivateKeyPath(this.properties.getPrivateKeyPath());
        payConfig.setPrivateCertPath(this.properties.getPrivateCertPath());
        payConfig.setApiV3Key(this.properties.getApiV3key());
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        return payConfig;
    }
}
