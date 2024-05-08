package xyz.magicraft.longshort.ssf.generic2;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.PostConstruct;
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
    public ResponseEntity<T> create(@RequestBody T created){
        return ResponseEntity.ok(genericService.create(created));
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

//    @PutMapping("")
//    public ResponseEntity<T> update(@RequestBody T updated){
//        return ResponseEntity.ok(service.update(updated));
//    }


}