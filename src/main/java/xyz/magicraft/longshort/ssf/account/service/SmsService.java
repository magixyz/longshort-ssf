package xyz.magicraft.longshort.ssf.account.service;

import java.util.Date;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xyz.magicraft.longshort.ssf.account.iface.ISmsSender;
import xyz.magicraft.longshort.ssf.account.model.Sms;
import xyz.magicraft.longshort.ssf.account.repository.SmsRepository;

@Service
public class SmsService {

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
