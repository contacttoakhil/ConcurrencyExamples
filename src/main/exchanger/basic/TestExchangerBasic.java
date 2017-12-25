package main.exchanger.basic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * Created by Akhil on 01-05-2015.
 */
public class TestExchangerBasic {
    @Test
    public void testExchangerBasic() {
        List<String> buffer1=new ArrayList<>();
        List<String> buffer2=new ArrayList<>();
        Exchanger<List<String>> exchanger=new Exchanger<>();

        Producer producer=new Producer(buffer1, exchanger);
        Consumer consumer=new Consumer(buffer2, exchanger);

        Thread threadProducer=new Thread(producer);
        Thread threadConsumer=new Thread(consumer);
        threadProducer.start();
        threadConsumer.start();
    }
}
