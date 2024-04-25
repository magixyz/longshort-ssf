package xyz.magicraft.longshort.ssf.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	public static <T> T parseJsonResource(String path,TypeReference<T> c){

		
        try {
            File file = ResourceUtils.getFile("classpath:" + path);
            ObjectMapper objectMapper = new ObjectMapper();
            T obj = objectMapper.readValue(file, c );
            System.out.println(obj.toString());
            return obj;
        } catch (IOException e) {
        	
        	System.out.println("parse json error");
            e.printStackTrace();
        }
        return null;
	}
	
	
}


