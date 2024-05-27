package xyz.magicraft.longshort.ssf.module.message.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.magicraft.longshort.ssf.generic2.Generic2Service;
import xyz.magicraft.longshort.ssf.module.message.iface.ISmsSender;
import xyz.magicraft.longshort.ssf.module.message.model.Sms;
import xyz.magicraft.longshort.ssf.module.message.repository.SmsRepository;

@Service
public class SmsService extends Generic2Service<Sms>{

	Logger logger = LoggerFactory.getLogger(SmsService.class);

	@Autowired
	private SmsRepository smsRepository;

	@Autowired
    private ISmsSender smsSender;
	
	
	public boolean sendSmsCode(String mobile,String code) {
		
		Sms sms = new Sms();
		sms.setMobile(mobile);
		sms.setCode(code);
		smsRepository.save(sms);
		
		return smsSender.sendCode(mobile, code);
		
		 
	}


//	


}
