package disruptor.chain;

import com.lmax.disruptor.EventFactory;

public class TradeFactory  implements EventFactory<Trade> {
	
    @Override
    public Trade newInstance() {
        return new Trade();
    }
}
