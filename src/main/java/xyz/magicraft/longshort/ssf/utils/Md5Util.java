package xyz.magicraft.longshort.ssf.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.springframework.web.multipart.MultipartFile;

public class Md5Util {

	/**
	 * 获取上传文件的md5
	 * @param file
	 * @return
	 */
	public static String getMd5(MultipartFile file) {
	    try {
	        //获取文件的byte信息
	        byte[] uploadBytes = file.getBytes();
	        // 拿到一个MD5转换器
	        MessageDigest md5 = MessageDigest.getInstance("MD5");
	        byte[] digest = md5.digest(uploadBytes);
	        //转换为16进制
	        return new BigInteger(1, digest).toString(16);
	    } catch (Exception e) {

	    	e.printStackTrace();
	    	return null;
	    }
	}

}
