package xyz.magicraft.longshort.ssf.define.controller;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import xyz.magicraft.longshort.ssf.define.helper.DefineRepository;



@RestController
@Slf4j
@RequestMapping("/ssf/v1")
public class LsDefineController {

	
	@Autowired
	DefineRepository defineRepository;


	
	@GetMapping("/rest/define/{page}-list")
	public List list(@PathVariable String page, HttpSession session ) throws Exception {
		
		log.debug(String.format("GET %s-list" ,page));


		return defineRepository.find(page).list;
		
				
	}
	
	@GetMapping("/rest/define/{page}-map")
	public Map map(@PathVariable String page, HttpSession session ) throws Exception {
		
		return defineRepository.find(page).map;
	
	}
	

}
