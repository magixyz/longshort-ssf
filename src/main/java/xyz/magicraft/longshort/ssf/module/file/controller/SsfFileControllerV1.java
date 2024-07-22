package xyz.magicraft.longshort.ssf.module.file.controller;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import jakarta.servlet.http.HttpSession;
import xyz.magicraft.longshort.ssf.utils.Md5Util;

@RestController
@RequestMapping("/v1/file")
public class SsfFileControllerV1 {
	
	@Value("${app.files.dir}")
	private String appFilesDir;
	
	
    @GetMapping(value = "/{type}/{filename}")
    public ResponseEntity download(@PathVariable String type,@PathVariable  String filename, HttpSession sesion) throws IOException{
    	
    	File f = new File(  new File( new File(appFilesDir),  type ),filename);

    	String filepath = f.getAbsolutePath();
    	
    	FileSystemResource fsr = new FileSystemResource(filepath);
    	
    	return ResponseEntity.ok()
    			.header("Content-Disposition", "attachment; filename="+ filename )
    			.contentLength(fsr.contentLength())
    			.contentType(MediaType.parseMediaType("application/octet-stream"))
    			.body(new InputStreamResource( fsr.getInputStream()));
    	
    }
    

	
	@PostMapping("/{type}/{suffix}")
	public Map upload( @PathVariable String type,@PathVariable String suffix, @RequestParam("file") MultipartFile file ) {
		
		
		
		String ret = String.format("%s-%d.%s", type, System.currentTimeMillis(), suffix );
		
		File f = new File( new File ( new File( new File(appFilesDir),  type).getPath() ),ret );
		
		System.out.println(f.getPath());
		
		f.mkdirs();

		String md5 = Md5Util.getMd5(file);
		
		try {
			file.transferTo(f);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Map.of("filename",ret,"md5",md5);
		
	}
}
