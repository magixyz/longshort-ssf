package xyz.magicraft.longshort.ssf.demo;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.util.StrUtil;
import xyz.magicraft.longshort.ssf.framework.helper.IGenericHelper;
import xyz.magicraft.longshort.ssf.framework.repository.IGenericRepository;



@Component
public class DemoGenericHelperImpl implements IGenericHelper{
	
	final static List<Class> MODEL_CLASSES = List.of(
			DemoModel.class
		);


	
	@Autowired
	private DemoRepository demoRepository;
	

	@Autowired
	private ObjectMapper objectMapper;
	
	

	@Override
	public IGenericRepository getRepository(Class c) {
		
		
		if (c.isAssignableFrom(DemoModel.class)) {
			return demoRepository;
		}else {
			return null;
		}
	}


	@Override
	public Object getDataByMsg(Map<String,Object> msg, Class c) {
		String str;
		try {
			str = objectMapper.writeValueAsString(msg);
			

			Object obj = objectMapper.readValue(str, c);
			
			
			return obj;
			
		} catch (JsonProcessingException e) {

			e.printStackTrace();
			
			return null;
		}
		
	}
	


	@Override
	public Class getClass(String page) {
		
		for (Class c: MODEL_CLASSES) {
			if (StrUtil.toUnderlineCase(c.getSimpleName()).equals(page)) {
				return c;
			}
		}
		
		return null;
		
		
	}
	
	
}


