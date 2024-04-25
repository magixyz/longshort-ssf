package xyz.magicraft.longshort.ssf.wechat.service;


import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import xyz.magicraft.longshort.ssf.wechat.controller.WechatAccountController;
import xyz.magicraft.longshort.ssf.wechat.model.WechatAccount;
import xyz.magicraft.longshort.ssf.wechat.repository.WechatAccountRepository;

@Service
@Slf4j
public class WechatAccountService {
	

	@Autowired
    private WxMaService wxMaService;
	
	@Autowired
	WechatAccountRepository wechatAccountRepository;
	
	private WechatAccount put(String openid,String sessionKey,String unionid,String appid) {
		
		WechatAccount wechatAccount = wechatAccountRepository.findByOpenid(openid);
		
		if (wechatAccount == null) {
			wechatAccount = new WechatAccount();
			wechatAccount.setOpenid(openid);
			wechatAccount.setUnionid(unionid);
			wechatAccount.setAppid(appid);
		}
		wechatAccount.setSessionKey(sessionKey);
		
		
		return wechatAccountRepository.save(wechatAccount);
	}
	
	public WechatAccount loginByCode(String appid, String code) {
		
		if (StringUtils.isBlank(code)) return null;

        if (!wxMaService.switchover(appid)) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        try {
            WxMaJscode2SessionResult wechatSession = wxMaService.getUserService().getSessionInfo(code);
            
            WechatAccount wechatAccount = put(wechatSession.getOpenid(), wechatSession.getSessionKey(), wechatSession.getUnionid(),appid);
            
            return wechatAccount;
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
		
		
	}
	
	public WechatAccount wechatMobile(String openid,String code) {
		
		WechatAccount wechatAccount = wechatAccountRepository.findByOpenid(openid);
		
		if (wechatAccount == null) return null;
		
		WxMaPhoneNumberInfo phoneNoInfo;
		try {
			phoneNoInfo = wxMaService.getUserService().getPhoneNoInfo(code);
		} catch (WxErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		wechatAccount.setMobile(phoneNoInfo.getPhoneNumber());
		
		return wechatAccountRepository.save(wechatAccount);
		
	}
}
