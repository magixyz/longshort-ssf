package xyz.magicraft.longshort.ssf.module.mine.controller;


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
import xyz.magicraft.longshort.ssf.generic2.Generic2Service;
import xyz.magicraft.longshort.ssf.utils.CsvUtil;




@RestController
public class LsMineController {

	
}
