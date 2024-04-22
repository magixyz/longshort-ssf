package xyz.magicraft.longshort.ssf.wechat.repository;

import java.util.Optional;


import xyz.magicraft.longshort.ssf.generic.IGenericRepository;
import xyz.magicraft.longshort.ssf.wechat.model.WechatAccount;

public interface WechatAccountRepository extends IGenericRepository<WechatAccount> {
	

	WechatAccount findByOpenid(String openid);

}
