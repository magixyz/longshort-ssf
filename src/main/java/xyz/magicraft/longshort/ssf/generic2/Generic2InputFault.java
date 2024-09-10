package xyz.magicraft.longshort.ssf.generic2;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class Generic2InputFault extends Throwable{
	
	int code = 0;
	
	public Generic2InputFault(String msg) {
		super(msg);
	}
	
	public Generic2InputFault(int code,String msg) {
		super(msg);
		
		this.code = code;
	}
	
	public String jsonMsgStr() {
		
		JSONObject obj = JSONUtil.createObj();
		obj.set("code", code);
		obj.set("msg", this.getMessage());
		
		return obj.toString();
	}

}
