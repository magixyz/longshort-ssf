package xyz.magicraft.longshort.ssf.wechat.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import me.chanjar.weixin.common.error.WxErrorException;
import xyz.magicraft.longshort.ssf.wechat.model.WechatAccount;
import xyz.magicraft.longshort.ssf.wechat.repository.WechatAccountRepository;

@Service
public class WechatAccountService {
	

	@Autowired
    private WxMaService wxMaService;
	
	@Autowired
	WechatAccountRepository wechatAccountRepository;
	
	public void login(String openid,String sessionKey,String unionid,String appid) {
		
		WechatAccount wechatAccount = wechatAccountRepository.findByOpenid(openid);
		
		if (wechatAccount == null) {
			wechatAccount = new WechatAccount();
			wechatAccount.setOpenid(openid);
			wechatAccount.setUnionid(unionid);
			wechatAccount.setAppid(appid);
		}
		wechatAccount.setSessionKey(sessionKey);
		
		
		wechatAccountRepository.save(wechatAccount);
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
