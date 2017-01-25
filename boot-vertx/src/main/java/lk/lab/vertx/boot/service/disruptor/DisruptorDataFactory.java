package lk.lab.vertx.boot.service.disruptor;

import com.lmax.disruptor.EventFactory;

public class DisruptorDataFactory implements EventFactory<DisruptorData> {

	@Override
	public DisruptorData newInstance() {
		return new DisruptorData();
	}

}
