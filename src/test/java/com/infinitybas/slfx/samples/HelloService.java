package com.infinitybas.slfx.samples;

import org.springframework.stereotype.Service;

@Service
public class HelloService {

	public String sayHello() {
		return "Hello from this Spring-managed service!";
	}
}
