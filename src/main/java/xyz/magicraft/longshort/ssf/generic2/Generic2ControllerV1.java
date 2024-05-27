package xyz.magicraft.longshort.ssf.generic2;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import xyz.magicraft.longshort.ssf.base.BaseModel;
import xyz.magicraft.longshort.ssf.base.Pagination;

public class Generic2ControllerV1<T> extends Generic2Clazz<T>{
	
	
	@Autowired
	protected Generic2Service<T> genericService;
	
	@PostConstruct
	public void init() {
		
		genericService.setClazz(clazz);
		
		
	}

	
    @GetMapping("")
    public ResponseEntity<Iterable<T>> list(){
        return ResponseEntity.ok(genericService.list());
    }
    
    
    @GetMapping("/list-last")
    public ResponseEntity<Iterable<T>> listLast(@RequestParam(defaultValue = "5") Integer limit){
        return ResponseEntity.ok(genericService.listLast(limit));
    }

    @PostMapping("")
    public ResponseEntity<T> create(@RequestBody @Validated T data){
        return ResponseEntity.ok(genericService.create(data));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<T> load(@PathVariable UUID uuid){
        return ResponseEntity.ok(genericService.get(uuid));
    }
    

	@PatchMapping("/{uuid}/{fieldsstr}")
	public ResponseEntity<?> patch(@PathVariable UUID uuid,@PathVariable String fieldsstr,@RequestBody T data, HttpSession session ) throws Exception {
		
		String [] fieldstrs = fieldsstr.split(",");
		
		Field[] fields = new Field[fieldstrs.length];
		for (int i= 0; i< fieldstrs.length; i++ ) {
			
			
			Field f = ReflectionUtils.findRequiredField(clazz, StrUtil.toCamelCase(fieldstrs[i])) ;
			
			fields[i] =f;
		}
		
		
		
		T obj = genericService.patch(uuid,fields,data);
		
		if (obj == null) return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(obj);
		
  
		
	}
    

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> delete(@PathVariable UUID uuid){
    	genericService.delete(uuid);
        return ResponseEntity.ok(uuid.toString());
    }
    


    @GetMapping("/pagination/list")
    public ResponseEntity<Pagination<T>> paginationList(@RequestParam(defaultValue = "0") Integer _page,@RequestParam(defaultValue = "20") Integer _size, HttpSession session){
        return ResponseEntity.ok(genericService.paginationList(_page,_size));
    }
    
    
    @Hidden
    @GetMapping("-by-foreign/{foreign}/{uuid}")
    public ResponseEntity<T> loadByForeign(@PathVariable String foreign, @PathVariable UUID uuid){
        return ResponseEntity.ok(genericService.loadByForeign(foreign, uuid));
    }
    
	

//    @PutMapping("")
//    public ResponseEntity<T> update(@RequestBody T updated){
//        return ResponseEntity.ok(service.update(updated));
//    }


}