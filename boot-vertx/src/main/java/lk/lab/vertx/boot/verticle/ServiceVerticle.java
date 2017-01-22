package lk.lab.vertx.boot.verticle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import lk.lab.vertx.boot.service.SomeService;

@Component
public class ServiceVerticle extends AbstractVerticle {
	
	@Autowired
	private SomeService service;
	
	@Override
	public void start() {
		vertx.eventBus().<String>consumer("say.hello").handler(helloHandler());
	}
	
	private Handler<Message<String>> helloHandler() {
		return msg -> vertx.<String>executeBlocking(
				future -> {
					try {
						future.complete(service.echo(msg.body()));
					} catch (Exception e) {
						future.fail(e);
					}
				},
				result -> {
					if (result.succeeded()) {
						msg.reply(result.result());
					} else {
						msg.reply(result.cause().toString());
					}
				}
			);
	}

}
