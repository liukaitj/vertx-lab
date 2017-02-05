package lk.lab.vertx.boot.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class SomeService {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	public String echo(String name) throws InterruptedException {
		logger.info("[" + Thread.currentThread().getName() + "] "
				+ getClass() + "#echo(" + name + ") called.");
		
		Thread.sleep(3000);
//		throw new RuntimeException("intentionallly throwed exception.");
		return "{\"hello\":\"" + name + "\"}";
	}

}
