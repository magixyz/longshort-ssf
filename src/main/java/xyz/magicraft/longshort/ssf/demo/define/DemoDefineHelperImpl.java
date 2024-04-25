package xyz.magicraft.longshort.ssf.demo.define;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

import xyz.magicraft.longshort.ssf.define.helper.DefineFactory;
import xyz.magicraft.longshort.ssf.define.helper.IDefineHelper;

@Component
public class DemoDefineHelperImpl implements IDefineHelper{

	

	@Override
	public List<DefineFactory> listFactory() {
		
		return List.of(
		
			new DefineFactory<Demo,DemoDefine>(Demo.class,"define.json",new TypeReference<List<DemoDefine>>() {})
				
		);
		
	}



}
