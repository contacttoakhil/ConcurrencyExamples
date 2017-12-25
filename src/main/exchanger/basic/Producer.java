package main.exchanger.basic;

import java.util.List;
import java.util.concurrent.Exchanger;

public class Producer implements Runnable{
	private List<String> buffer;
	private final Exchanger<List<String>> exchanger;

	public Producer (List<String> buffer, Exchanger<List<String>> exchanger){
		this.buffer=buffer;
		this.exchanger=exchanger;
	}

	@Override
	public void run() {
		int cycle=1;
		// 10 cycles of data interchange.
		for (int outerCount=0; outerCount<10; outerCount++) {
			System.out.printf("Producer: Cycle %d\n",cycle);

			// In each cycle add 10 strings to buffer.
			for (int innerCount=0; innerCount<10; innerCount++){
				String message="Event "+((outerCount*10)+innerCount);
				System.out.printf("Producer: %s\n",message);
				buffer.add(message);
			}

			//Exchange data.
			try {
				buffer=exchanger.exchange(buffer);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Producer: "+buffer.size());
			cycle++;
		}
	}
	
}
