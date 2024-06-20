//package xyz.magicraft.longshort.ssf.generic2;
//
//import java.lang.reflect.Field;
//import java.util.Map;
//import java.util.UUID;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import cn.hutool.core.util.StrUtil;
//import io.swagger.v3.oas.annotations.Hidden;
//import jakarta.annotation.PostConstruct;
//import jakarta.servlet.http.HttpSession;
//import xyz.magicraft.longshort.ssf.base.BaseModel;
//
//public class CustomController<T> extends Generic2Clazz<T>{
//	
//	
//	@Autowired
//	Generic2Service<T> genericService;
//	
//	@PostConstruct
//	public void init() {
//		
//		genericService.setClazz(clazz);
//		
//		
//	}
//
//
//}