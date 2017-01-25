package lk.lab.vertx.boot.service.disruptor;

import javax.annotation.PostConstruct;

import com.lmax.disruptor.dsl.Disruptor;

public class SquareDisruptor {
	
	private Disruptor<DisruptorData> disruptor;

	public void setDisruptor(Disruptor<DisruptorData> disruptor) {
		this.disruptor = disruptor;
	}
	
	@PostConstruct
	public void startDisruptor() {
		disruptor.start();
	}

}
