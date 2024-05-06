package xyz.magicraft.longshort.ssf.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.binarywang.wx.miniapp.constant.WxMaApiUrlConstants.User;
import cn.hutool.core.util.StrUtil;
import xyz.magicraft.longshort.ssf.generic.helper.GenericHelper;
import xyz.magicraft.longshort.ssf.generic.iface.IGenericHelper;
import xyz.magicraft.longshort.ssf.generic.iface.IGenericInterceptor;
import xyz.magicraft.longshort.ssf.generic.iface.IGenericRepository;



@Component
public class DemoGenericHelperImpl extends GenericHelper{

	@Autowired
	private DemoRepository demoRepository;
	

	@Override
	public List<Class> listModels (){
		return List.of(
				DemoModel.class
				);
	}
	

	@Override
	public IGenericRepository getRepository(Class c) {
		
		
		if (c.isAssignableFrom(DemoModel.class)) {
			return demoRepository;
		}else {
			return null;
		}
	}



	@Override
	public IGenericInterceptor getInterceptor(Class c) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}


