package xyz.magicraft.longshort.ssf.module.admin.controller;


import java.util.Map;
import java.util.UUID;
import java.io.File;
import java.lang.reflect.Field;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpSession;
import xyz.magicraft.longshort.ssf.base.Pagination;
import xyz.magicraft.longshort.ssf.generic.iface.IGenericHelper;
import xyz.magicraft.longshort.ssf.generic.service.GenericService;
import xyz.magicraft.longshort.ssf.utils.CsvUtil;




@RestController
public class GenericAdminController {

	Logger logger = LoggerFactory.getLogger(GenericAdminController.class);
	

	@Autowired
	private GenericService genericService;
	
	@Autowired
	private IGenericHelper genericHelper;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Value("${app.files.dir}")
	private String appFilesDir;
	
	


 
	@GetMapping("/admin/rest/generic/search-{page}s")
	public ResponseEntity<?> search(@PathVariable String page, @RequestParam Map<String,String> msg, HttpSession session ) throws Exception {
		
		logger.info(String.format("GET /admin/rest/search-%ss, msg: %s" ,page, msg.toString()));
		
		Class c = genericHelper.getClass(page);
		
		
		Pagination pg = genericService.search(msg, 0, 20,c);
		
		return new ResponseEntity<Pagination>(pg,HttpStatus.OK);
  
		
	}
	
	 
		@GetMapping("/admin/rest/generic/{page}s")
		public ResponseEntity<?> list(@PathVariable String page, HttpSession session ) throws Exception {
			
			logger.info(String.format("GET %s" ,page));
			

			Class c = genericHelper.getClass(page);
			

			Iterable ret =  genericService.search(null,c);
				
			
			
			return new ResponseEntity(ret,HttpStatus.OK);
	  
			
		}
		
		@GetMapping("/admin/rest/generic/{page}/{uuid}")
		public ResponseEntity<?> load(@PathVariable String page,@PathVariable UUID uuid, HttpSession session ) throws Exception {
			
			logger.info(String.format("GET %s" ,page));
			

			Class c = genericHelper.getClass(page);
			

			Object ret =  genericService.get(uuid,c);
				
			
			
			return new ResponseEntity(ret,HttpStatus.OK);
	  
			
		}
	
//
//	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
//	@GetMapping("/admin/rest/faqs")
//	public Page<Faq> list(@RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "20") Integer size, HttpSession session ) throws Exception {
//		
//
//  
//		Page<Faq> pg = faqService.list(page,size);
//		
//		return pg;
//		
//	}
//
//	
//
	@PostMapping(value= "/admin/rest/generic/{page}",consumes = "application/json",produces = "application/json")
	public Object post(@PathVariable String page, @RequestBody Map<String,Object> msg,Authentication auth,HttpSession session ) throws Exception {
		
		logger.info("page: " + page + "msg: " + msg.toString());
		
		Class c = genericHelper.getClass(page);
		Object obj = genericHelper.getDataByMsg(msg, c);
		
		
		if (auth != null) {
			System.out.println("auth: " + auth.toString());
		}
		
		
		return genericService.post( obj,c);
		

		

		
	}
	
	@PostMapping(value= "/admin/rest/generic/import-{page}s",consumes = "application/json",produces = "application/json")
	public Object import_(@PathVariable String page, @RequestBody Map<String,Object> msg,Authentication auth,HttpSession session ) throws Exception {
		
		logger.info("page: " + page + "msg: " + msg.toString());
		
		Class c = genericHelper.getClass(page);
		
		String filename = (String)msg.get("filename");
		String filetype = (String)msg.get("filetype");
		if (filetype == null) {
			filetype = "import_";
		} 
		
		
		if (auth != null) {
			System.out.println("auth: " + auth.toString());
		}
		
		File file = new File(new File(this.appFilesDir,filetype), filename);
		
		System.out.println("file: " + file.getAbsolutePath());
		
		List objs = new CsvUtil().loadObjectList(c, file);
		
		System.out.println(objs.size());
		
		
		return genericService.import_( objs,c);
		

		

		
	}
	
	
	@PatchMapping("/admin/rest/generic/{page}/{uuid}/{fieldsstr}")
	public ResponseEntity<?> patch(@PathVariable String page,@PathVariable UUID uuid,@PathVariable String fieldsstr, @RequestBody Map<String,Object> msg, HttpSession session ) throws Exception {
		
		logger.info(String.format("Patch /admin/rest/%s, msg: %s", page , msg.toString()));
		
		Class c = genericHelper.getClass(page);
		Object obj = genericHelper.getDataByMsg(msg, c);
		String [] fieldstrs = fieldsstr.split(",");
		
		Field[] fields = new Field[fieldstrs.length];
		for (int i= 0; i< fieldstrs.length; i++ ) {
			Field f =  ReflectionUtils.findRequiredField(c,StrUtil.toCamelCase(fieldstrs[i]));
			fields[i] =f;
		}
		
		
		Object obj2 = genericService.patch(uuid, fields, obj, c);
  
		return new ResponseEntity<Object>(obj2,HttpStatus.OK);
		
		
	}
	
	@PatchMapping("/admin/rest/generic/{page}-foreign/{uuid}/{foreign}")
	public ResponseEntity<?> patchForeign(@PathVariable String page,@PathVariable UUID uuid,@PathVariable String foreign, @RequestBody Map<String,UUID> msg , HttpSession session ) throws Exception {
		
		UUID foreignUuid = msg.get("uuid");
		
		
		boolean ret = genericService.patchForeign(page, uuid, foreign, foreignUuid);
  
		
		return new ResponseEntity<Object>(HttpStatus.OK);
		
		
	}
	
	@DeleteMapping(value= "/admin/rest/generic/{page}/{uuid}")
	public Object delete(@PathVariable String page, @PathVariable UUID uuid,Authentication auth,HttpSession session ) throws Exception {
		
		logger.info("page: " + page + "uuid: " + uuid);

		Class c = genericHelper.getClass(page);
		
		if (auth != null) {
			System.out.println("auth: " + auth.toString());
		}
		
		
		return genericService.delete( uuid, c);
		
		
	}

	
}
