package lk.lab.vertx.boot.verticle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import lk.lab.vertx.boot.service.SomeService;

@Component
public class ServiceVerticle extends AbstractVerticle {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private SomeService service;
	
	@Override
	public void start() {
		logger.info("[" + Thread.currentThread().getName() + "] "
				+ getClass() + "#start() called.");
		
		vertx.eventBus().<String>consumer("say.hello").handler(helloHandler());
	}
	
	private Handler<Message<String>> helloHandler() {
		return msg -> {
			logger.info("[" + Thread.currentThread().getName() + "] "
					+ getClass() + "#consumeHelloHandler called.");
			
			vertx.<String>executeBlocking(
					future -> {
						try {
							future.complete(service.echo(msg.body()));
						} catch (Exception e) {
							future.fail(e);
						}
					},
					false,  // 将线程池的执行顺序设定为无序，因此可以并行地执行多个任务
					result -> {
						if (result.succeeded()) {
							msg.reply(result.result());
						} else {
							msg.fail(500, result.cause().getMessage());
						}
					}
				);
		};
	}

}
