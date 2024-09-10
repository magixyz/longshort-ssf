package xyz.magicraft.longshort.ssf.generic2;

import java.lang.reflect.Field;
import java.util.List;
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
import lombok.extern.slf4j.Slf4j;
import xyz.magicraft.longshort.ssf.base.Pagination;


public class Generic2ControllerV1<T> extends Generic2Clazz<T>{
	
	
	@Autowired
	protected Generic2Service<T> genericService;

	
    @GetMapping("")
    public ResponseEntity<Iterable<T>> list(){
        return ResponseEntity.ok(genericService.list());
    }
    
    
    @GetMapping("/list-last")
    public ResponseEntity<Iterable<T>> listLast(@RequestParam(defaultValue = "5") Integer limit){
        return ResponseEntity.ok(genericService.listLast(limit));
    }

    @PostMapping("")
    public ResponseEntity<T> create(@RequestBody @Validated T data,HttpSession session){
        return ResponseEntity.ok(genericService.create(data));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<T> load(@PathVariable UUID uuid){
        return ResponseEntity.ok(genericService.get(uuid));
    }
    
    @GetMapping("/load-by-field/{field}/{value}")
    public ResponseEntity<T> loadByField(@PathVariable String field,@PathVariable String value){
    	T t = genericService.loadByField(field, value);
    	
    	if (t == null)  return ResponseEntity.notFound().build();

    	
        return ResponseEntity.ok(t);
    }
    

	@PatchMapping("/{uuid}/{fields}")
	public ResponseEntity<?> patch(@PathVariable UUID uuid,@PathVariable String fields,@RequestBody T data, HttpSession session ) throws Exception {
		
		
		String [] fieldstrs = fields.split(",");
		
		Field[] fs = new Field[fieldstrs.length];
		for (int i= 0; i< fieldstrs.length; i++ ) {
			
			
			Field f = ReflectionUtils.findRequiredField(getClazz(), StrUtil.toCamelCase(fieldstrs[i])) ;
			
			fs[i] =f;
		}
		

		
		
		T obj = genericService.patch(uuid,fs,data);
		
		if (obj == null) return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(obj);
		
  
		
	}
    

    @DeleteMapping("/{uuid}")
    public ResponseEntity<UUID> delete(@PathVariable UUID uuid){
    	genericService.delete(uuid);
        return ResponseEntity.ok(uuid);
    }
    


    @GetMapping("/pagination/list")
    public ResponseEntity<Pagination<T>> paginationList(@RequestParam(defaultValue = "0") Integer _page,@RequestParam(defaultValue = "20") Integer _size, HttpSession session){
        return ResponseEntity.ok(genericService.paginationList(_page,_size));
    }
    
    
    @GetMapping("list-by-foreign/{foreign}/{uuid}")
    public ResponseEntity<Iterable<T>> listByForeign(@PathVariable String foreign, @PathVariable UUID uuid,HttpSession session){
        return ResponseEntity.ok(genericService.listByForeign(foreign, uuid));
    }
    
    @PostMapping("search-by-foreign/{foreign}")
    public ResponseEntity<Iterable<T>> searchByForeign(@PathVariable String foreign, @RequestBody T data){
        return ResponseEntity.ok(genericService.searchByForeign(foreign, data));
    }
    
	

//    @PutMapping("")
//    public ResponseEntity<T> update(@RequestBody T updated){
//        return ResponseEntity.ok(service.update(updated));
//    }


}