package com.andy.boot.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * projectName: springstudy  com.andy.boot.web
 *
 * @desc:
 * @author: xiangdan
 * @time : 2019-01-24 Thursday 20:53
 */
@SpringBootApplication
@RestController
public class FirstWebApplication {

	@GetMapping("/{name}")
	public String hello(@PathVariable("name") String name){
		return "hello:"+name+"现在是北京时间"+ LocalDateTime.now();
	}

	public static void main(String[] args){
		SpringApplication.run(FirstWebApplication.class,args);
	}

}
