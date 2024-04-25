package xyz.magicraft.longshort.ssf.wechat.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import xyz.magicraft.longshort.ssf.wechat.JsonUtils;
import xyz.magicraft.longshort.ssf.wechat.model.WechatAccount;
import xyz.magicraft.longshort.ssf.wechat.service.WechatAccountService;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/v1/wechat/account/{appid}")
public class WechatAccountController {
    private final WxMaService wxMaService;
    
    @Autowired
    WechatAccountService wechatAccountService;

    /**
     * 登录状态
     */
    @GetMapping("/check")
    public String check(@PathVariable String appid,HttpSession session) {

        System.out.println("check called");
    	System.out.println("session id:" + session.getId());
    	
    	
    	String openid = (String)session.getAttribute("openid");

    	if (openid != null) {
    		return "OK";
    	}else {
    		return "Fail";
    	}
    }
    
    /**
     * 登录
     */
    @GetMapping("/login")
    public Map<String,Object> login(@PathVariable String appid, String code,HttpSession session) {
    	
    	WechatAccount wechatAccount = wechatAccountService.loginByCode(appid, code);
    	
    	if (wechatAccount != null) {

            session.setAttribute("openid", wechatAccount.getOpenid());
            
            return Map.of("result","OK");
    	}else {
    		return Map.of("result","Fail");
    	}
    	
    	
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    public String info(@PathVariable String appid, String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        // 用户信息校验
        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            WxMaConfigHolder.remove();//清理ThreadLocal
            return "user check failed";
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        WxMaConfigHolder.remove();//清理ThreadLocal
        return JsonUtils.toJson(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/mobile")
    public Map<String,Object> mobile(@PathVariable String appid, String code,HttpSession session,HttpServletResponse resp) {
        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        String openid =  (String)session.getAttribute("openid");

        
        if (openid == null) {
        	try {
				resp.sendError(HttpStatus.UNAUTHORIZED.value());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	return null;
        }
        
        
        if (wechatAccountService.wechatMobile(openid, code) != null) {
        	return Map.of("result","OK");
        }else {
        	return Map.of("result","Fail");
        }
        
    }

}