package lk.lab.vertx.boot.service.disruptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lmax.disruptor.WorkHandler;

public class DisruptorConsumer implements WorkHandler<DisruptorData> {
	
	private final Log logger = LogFactory.getLog(getClass());

	@Override
	public void onEvent(DisruptorData event) throws Exception {
		logger.info("Consumer [" + Thread.currentThread().getName() + "] "
				+ event.getValue() * event.getValue());
	}

}
