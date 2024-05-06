package xyz.magicraft.longshort.ssf.generic.controller;


import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpSession;
import xyz.magicraft.longshort.ssf.base.Pagination;
import xyz.magicraft.longshort.ssf.generic.iface.IGenericHelper;
import xyz.magicraft.longshort.ssf.generic.service.GenericService;


@Hidden
@RestController
public class LsGenericController {

	Logger logger = LoggerFactory.getLogger(LsGenericController.class);


	@Autowired
	private GenericService genericService;

	@Autowired
	private IGenericHelper genericHelper;


	@GetMapping("/rest/{p}s")
	public Pagination<Object> list(@PathVariable String p, @RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "20") Integer size, HttpSession session ) throws Exception {

		logger.debug(String.format("GET %ss" ,page));

		Class c = genericHelper.getClass(p);
  
		Page pg =  genericService.list(page,size, c);
		
		return new Pagination<Object>(page,size,pg.getTotalPages(),pg.getContent());
		
		
	}
	
	@GetMapping("/rest/generic/{page}-list")
	public ResponseEntity<?> list(@PathVariable String page, HttpSession session ) throws Exception {
		
		logger.debug(String.format("GET %s-list" ,page));

		Class c = genericHelper.getClass(page);
		
		
		Iterable pg = genericService.list(c);
		
		return new ResponseEntity(pg,HttpStatus.OK);
  
		
	}
	
	@PostMapping("/rest/generic/{page}-search")
	public ResponseEntity<?> search(@PathVariable String page,@RequestBody Map<String, Object> condition, HttpSession session ) throws Exception {
		
		logger.debug(String.format("POST %s, condition:%s " ,page , new ObjectMapper().writeValueAsString(condition)) );

		Class c = genericHelper.getClass(page);
		
		Iterable pg = genericService.search(condition,c);
		
		return new ResponseEntity(pg,HttpStatus.OK);
	
	}
	
	@GetMapping("/rest/generic/{page}/{uuid}")
	public ResponseEntity<?> get(@PathVariable String page,@PathVariable UUID uuid, HttpSession session ) throws Exception {
		
		logger.debug(String.format("GET %s, %s" ,page , uuid.toString()));

		Class c = genericHelper.getClass(page);
		
		Object obj = genericService.get(uuid,c);
		
		if (obj == null) return new ResponseEntity("Not Found",HttpStatus.NOT_FOUND);
		
		return new ResponseEntity(obj,HttpStatus.OK);
  
		
	}
	@GetMapping("/rest/generic/{page}-by-foreign/{foreign}/{uuid}")
	public Object loadByForeign(@PathVariable String page,@PathVariable String foreign,@PathVariable UUID uuid, HttpSession session ) throws Exception {
		
		logger.debug(String.format("GET %s" ,page));
		
		Class c = genericHelper.getClass(page);
		
		
		Object obj = genericService.loadByForeign(page,foreign,uuid,c);
		
		return obj;
  
		
	}
	
	@GetMapping("/rest/generic/{page}-list-by-foreign/{foreign}/{uuid}")
	public Iterable listByForeign(@PathVariable String page,@PathVariable String foreign,@PathVariable UUID uuid, HttpSession session ) throws Exception {
		
		logger.debug(String.format("GET %s" ,page));
		
		Class c = genericHelper.getClass(page);
		
		
		Iterable list = genericService.listByForeign(page,foreign,uuid,c);
		
		return list;
  
		
	}

	
	@PatchMapping("/rest/generic/{page}/{uuid}/{fieldsstr}")
	public ResponseEntity<?> patch(@PathVariable String page,@PathVariable UUID uuid,@PathVariable String fieldsstr,@RequestBody Map<String,Object> msg, HttpSession session ) throws Exception {
		
		logger.debug(String.format("Patch %s, %s, %s" ,page, uuid, fieldsstr));
		
		Class c = genericHelper.getClass(page);
		logger.info("class name: " + c.getName());
		
		Object obj = genericHelper.getDataByMsg(msg, c);
		String [] fieldstrs = fieldsstr.split(",");
		
		Field[] fields = new Field[fieldstrs.length];
		for (int i= 0; i< fieldstrs.length; i++ ) {
			Field f = c.getDeclaredField(StrUtil.toCamelCase(fieldstrs[i]));
			fields[i] =f;
		}
		
		
		
		Object obj2 = genericService.patch(uuid,fields,obj,c);
		
		if (obj2 == null) return new ResponseEntity("Not Found",HttpStatus.NOT_FOUND);
		
		return new ResponseEntity(obj2,HttpStatus.OK);
  
		
	}
	
	
	@PutMapping("/rest/generic/{page}/{uuid}")
	public ResponseEntity<?> put(@PathVariable String page,@PathVariable UUID uuid,@RequestBody Map<String,Object> msg, HttpSession session ) throws Exception {
		
		logger.debug(String.format("PUT %s" ,page));

		Class c = genericHelper.getClass(page);
		
		Object obj = genericService.put(uuid,genericHelper.getDataByMsg(msg, c),c);
		
		if (obj == null) return new ResponseEntity("Not Found",HttpStatus.NOT_FOUND);
		
		return new ResponseEntity(obj,HttpStatus.OK);
  
		
	}
	
	@PostMapping(value= "/rest/generic/{page}",consumes = "application/json",produces = "application/json")
	public Object post(@PathVariable String page, @RequestBody Map<String,Object> msg,Authentication auth,HttpSession session ) throws Exception {

		logger.debug(String.format("POST %s, msg: %s" ,page, msg.toString()));

		Class c = genericHelper.getClass(page);
		
		if (auth != null) {
			System.out.println("auth: " + auth.toString());
		}
		
		
		return genericService.post(  genericHelper.getDataByMsg(msg, c), c);
		

		

		
	}
	



}
