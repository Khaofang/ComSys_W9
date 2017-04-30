import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ConcurrentCount2 {
	static FileReader fr;
	static BufferedReader in;
	static long totalResult = 0;

	public static void main(String[] args) {
		int processors = Runtime.getRuntime().availableProcessors();
		ForkJoinPool instance = new ForkJoinPool(processors);
		try {
			fr = new FileReader("./02all.nt");
			in = new BufferedReader(fr);
			StringCounterForkJoinTask task = new StringCounterForkJoinTask(24379824);			
			instance.invoke(task);
			totalResult = task.result;
		} catch (Exception  e) {
			e.printStackTrace();
		}
		System.out.println("Result = " + totalResult);
	}
}

class StringCounterForkJoinTask extends RecursiveTask<Long>{
	public long result;
	private int n;

	public StringCounterForkJoinTask(int n){
		this.n = n;
	}

	@Override
	protected Long compute() {
		if (n < 1) {
			try {
				String s = ConcurrentCount2.in.readLine();
				if(s != null && s.contains("<http://www.w3.org/2001/XMLSchema#string>"))
					result = 1;
			} catch (Exception e) {
				e.printStackTrace();
			} 
		} else {
			try{
				StringCounterForkJoinTask sc1 = new StringCounterForkJoinTask(n / 2);
				StringCounterForkJoinTask sc2 = new StringCounterForkJoinTask(n / 2);
				sc1.fork();
				result = sc2.compute() + sc1.join();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
