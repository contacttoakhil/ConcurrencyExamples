package main.forkJoin.primeNumber;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoinPrime {
	private static int workSize; 
	private static Queue<Results> resultsQueue;
	
	//Use this class to collect work
	private static class  Results {
		private final int minPrimeToTry;
		private final int maxPrimeToTry;
		private final Set<Integer> resultSet;
		
		public Results (int minPrimeToTry, int maxPrimeToTry, Set<Integer> resultSet) {
			this.minPrimeToTry = minPrimeToTry;
			this.maxPrimeToTry = maxPrimeToTry;
			this.resultSet = resultSet;
		}
	}
	
	private static class FindPrimes extends RecursiveAction {
		private final int start;
		private final int end;
		
		public FindPrimes(int start, int end)
		{
			this.start = start;
			this.end = end;
		}
		
		//Almost same code but the outerCount ranges from minPrimeToTry to  maxPrimeToTry.
		private Set<Integer> findPrimes(int minPrimeToTry, int maxPrimeToTry) {
			Set<Integer> result = new HashSet<Integer>();
			outer:
			for(int outerCount=minPrimeToTry; outerCount<=maxPrimeToTry; outerCount++){
				//Need to try upto sqrt(outerCount)
				int innerTarget = (int) Math.sqrt(outerCount);
				for(int innerCount = 2; innerCount<innerTarget; innerCount++) {
					//If we can divide exactly by innerCount then outerCount is not prime
					if( (outerCount/innerCount) * innerCount == outerCount)
						continue outer;
				}
				//If we reach here outerCount is prime.
				result.add(outerCount);
			}
			return result;
		}
		
		@Override
		protected void compute() {
			//Small enough for us?
			if(end-start < workSize) {
				resultsQueue.offer(new Results(start, end, findPrimes(start, end)));
			}
			else
			{
				//Divide into two pieces
				int mid = (start + end)/2;
				invokeAll(new FindPrimes(start, mid), new FindPrimes(mid+1, end));
			}
		}
	}
	
	public static void main(String args[]) {
		int maxPrimetoTry = 9999999;
		int maxWorkDivisor = 8;
		workSize = (maxPrimetoTry+1)/maxWorkDivisor;
		ForkJoinPool fjp = new ForkJoinPool();
		resultsQueue = new ConcurrentLinkedQueue<Results>();
		long startTime = System.currentTimeMillis();
		fjp.invoke(new FindPrimes(2, maxPrimetoTry));
		long timeTaken = System.currentTimeMillis() - startTime;
		
		System.out.println("Number of taks executed: " + resultsQueue.size());
		while(resultsQueue.size() > 0) {
			Results results = resultsQueue.poll();
			Set<Integer> result = results.resultSet;
			result.stream().forEach(System.out::println);
		}
		System.out.println("Time taken: " + timeTaken);
	}
	
}
