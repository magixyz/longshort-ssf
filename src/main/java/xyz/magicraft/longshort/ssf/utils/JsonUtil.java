package xyz.magicraft.longshort.ssf.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	public static <T> T parseJsonResource(String path,TypeReference<T> c){

		
        try {
        	
        	Resource resource = new ClassPathResource(path);
            InputStream inputStream = resource.getInputStream();
        	
//            File file = ResourceUtils.getFile("classpath:" + path);
            ObjectMapper objectMapper = new ObjectMapper();
            T obj = objectMapper.readValue(inputStream, c );
            System.out.println(obj.toString());
            return obj;
        } catch (IOException e) {
        	
        	System.out.println("parse json error");
            e.printStackTrace();
        }
        return null;
	}
	
	
}


