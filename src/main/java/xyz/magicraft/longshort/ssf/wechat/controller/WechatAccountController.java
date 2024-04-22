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
        if (StringUtils.isBlank(code)) {
            return Map.of("result","Fail","message","code empty!");
        }

        System.out.println("login called");
        System.out.println("session id:");
        System.out.println(session.getId());
        

        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            WxMaJscode2SessionResult wechatSession = wxMaService.getUserService().getSessionInfo(code);
            log.info(wechatSession.getSessionKey());
            log.info(wechatSession.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据
            
            
            wechatAccountService.login(wechatSession.getOpenid(), wechatSession.getSessionKey(), wechatSession.getUnionid(),appid);
            
            session.setAttribute("openid", wechatSession.getOpenid());
            
            
            return Map.of("result","OK");
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return Map.of("result","Fail","message",e.getMessage());
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
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