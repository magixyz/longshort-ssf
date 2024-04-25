package xyz.magicraft.longshort.ssf.generic;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import cn.binarywang.wx.miniapp.constant.WxMaApiUrlConstants.User;
import cn.hutool.core.util.StrUtil;
import xyz.magicraft.longshort.ssf.generic.IGenericRepository;
import xyz.magicraft.longshort.ssf.generic.helper.IGenericHelper;
import xyz.magicraft.longshort.ssf.generic.helper.IGenericInterceptor;

@Component
public abstract class GenericHelper implements IGenericHelper{
	

	@Autowired
	private ObjectMapper objectMapper;
	
	

	
	@Override
	public Object getDataByMsg(Map<String,Object> msg, Class c) {
		String str;
		try {
			str = objectMapper.writeValueAsString(msg);
			
			System.out.println("msg:" + str);
			System.out.println("class:" + c.getName());

			Object obj = objectMapper.readValue(str, c);
			
			
			return obj;
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return null;
		}
		
	}
	

	@Override
	public Class getClass(String page) {
		
		for (Class c: listModels()) {
			if (StrUtil.toUnderlineCase(c.getSimpleName()).equals(page)) {
				return c;
			}
		}
		
		return null;
		
		
	}

	
	
}
