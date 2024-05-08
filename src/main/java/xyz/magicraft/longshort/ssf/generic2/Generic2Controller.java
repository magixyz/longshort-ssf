package xyz.magicraft.longshort.ssf.generic2;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import xyz.magicraft.longshort.ssf.base.BaseModel;

public class Generic2Controller<T> extends Generic2Clazz<T>{
	
	
	@Autowired
	Generic2Service<T> genericService;
	
	@PostConstruct
	public void init() {
		
		genericService.setClazz(clazz);
		
		
	}

    @GetMapping("")
    public ResponseEntity<Iterable<T>> list(){
        return ResponseEntity.ok(genericService.list());
    }
    

    @PostMapping("")
    public ResponseEntity<T> create(@RequestBody @Validated T data){
        return ResponseEntity.ok(genericService.create(data));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<T> load(@PathVariable UUID uuid){
        return ResponseEntity.ok(genericService.get(uuid));
    }
    

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> delete(@PathVariable UUID uuid){
    	genericService.delete(uuid);
        return ResponseEntity.ok(uuid.toString());
    }
    
    @Hidden
    @GetMapping("-by-foreign/{foreign}/{uuid}")
    public ResponseEntity<T> loadByForeign(@PathVariable String foreign, @PathVariable UUID uuid){
        return ResponseEntity.ok(genericService.loadByForeign(foreign, uuid));
    }
    
	
	@PatchMapping("/{uuid}/{fieldsstr}")
	public ResponseEntity<?> patch(@PathVariable UUID uuid,@PathVariable String fieldsstr,@RequestBody T data, HttpSession session ) throws Exception {
		
		String [] fieldstrs = fieldsstr.split(",");
		
		Field[] fields = new Field[fieldstrs.length];
		for (int i= 0; i< fieldstrs.length; i++ ) {
			Field f = clazz.getDeclaredField(StrUtil.toCamelCase(fieldstrs[i]));
			fields[i] =f;
		}
		
		
		
		T obj = genericService.patch(uuid,fields,data);
		
		if (obj == null) return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(obj);
		
  
		
	}

//    @PutMapping("")
//    public ResponseEntity<T> update(@RequestBody T updated){
//        return ResponseEntity.ok(service.update(updated));
//    }


}