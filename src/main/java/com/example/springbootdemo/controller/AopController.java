package com.example.springbootdemo.controller;

import com.example.springbootdemo.dto.AopParam;
import com.example.springbootdemo.repository.MMaterialRepository;
import com.example.springbootdemo.service.impl.AopService;
import com.example.springbootdemo.service.impl.AopServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/aop")
public class AopController {

    private final AopServiceImpl aopService;
    private final AopService iAopService;
    private final MMaterialRepository materialRepo;


    public AopController(
            @Qualifier("aopServiceConcreteImpl") AopServiceImpl aopServiceConcrete,
            @Qualifier("aopServiceImpl") AopServiceImpl aopService,
            @Qualifier("aopServiceImpl") AopService iAopService,
            MMaterialRepository materialRepo) {
        this.aopService = aopServiceConcrete;
        this.iAopService = iAopService;
        this.materialRepo = materialRepo;
    }

    @GetMapping("/execute")
    public Object execute(@RequestParam(defaultValue = "1") int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("override interface", aopService.executeOverrideInterface(id));
        map.put("default interface", aopService.executeDefaultInInterface(id));
        map.put("override default interface", aopService.executeOverrideDefaultInInterface(id));
        map.put("override common abstract", aopService.executeCommonMethod(id));
        map.put("normal common abstract", aopService.executeNormalCommonMethod(id));
        map.put("common interface", aopService.executeCommonInterface(id));
        map.put("common default interface", aopService.executeCommonDefaultInterface(id));
        map.put("normal", aopService.executeNormalMethodInClass(id));
        map.put("override abstract", aopService.executeOverrideAbstract(id));
        map.put("normal abstract", aopService.executeNormalInAbstract(id));
        map.put("override normal abstract", aopService.executeOverrideNormalInAbstract(id));
        map.put("do anno args class", aopService.doAnnoArgsInClass(1, new AopParam(id, "namnn"), new AopParam(), "namnn2"));
        map.put("do args class", aopService.doArgsInClass(1, new AopParam(), "namnn2"));
        map.put("do annotation on method", aopService.doAnnotationOnMethod(id));
        map.put("method in repo", materialRepo.countBySystemUserCodeAndMaterialCode("01", "M001"));
        map.put("method in super repo", materialRepo.count());
        return map;
    }


}
