package lk.lab.vertx.boot.service;

import org.springframework.stereotype.Service;

@Service
public class SomeService {
	
	public String echo(String name) {
		return "{'hello':'" + name + "'}";
	}

}
