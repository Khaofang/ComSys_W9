import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentCount1 {
	static FileReader fr;
	static BufferedReader in;
	static long result = 0;
	static String PATH = "./02all.nt";
	public static void main(String[] args) {
		List<Callable<String>> callables = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
		try {
			fr = new FileReader(PATH);
			in = new BufferedReader(fr);
			try {
				while (true) {
					String line = in.readLine();
					if (line == null)
						break;
					Callable<String> c = new StringCallable(line);
					String[] words = executor.submit(c).get().split(" ");
					for (int i = 0; i < words.length; i++) {
						if (words[i].contains("<http://www.w3.org/2001/XMLSchema#string>"))
							result++;
					}
					System.out.println("Result: " + result);
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Total Result = "+result);
		executor.shutdown();
	}
}

class StringCallable implements Callable<String>{
	String line;
	public StringCallable(String line){
		this.line = line;
	}
	@Override
	public String call() throws NumberFormatException, IOException{
		return line;
	}
}
