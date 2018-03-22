package com.br.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class BrBaseController {
	
	
	@RequestMapping(value = "/ping")
	public String ping(){
		return "pong-"+System.currentTimeMillis();
	}
}
