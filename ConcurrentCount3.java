import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCount3 {
	static ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
	static FileReader fr;
	static BufferedReader in;
	static String PATH = "/Users/kundjanasith/Desktop/myText.txt";
	public static void main(String[] args) {
		long total = 0;

		try {
			fr = new FileReader("./02all.nt");
			in = new BufferedReader(fr);
			String line;
			for (int i = 0; true; i++) {
				line = in.readLine();
				if (line == null)
					break;
				
				String[] s = line.split(" ");
				for (int j = 0; j < s.length; j++) {
					map.put(i + "",  s[j]);

					if (i % 200000 == 0 && i != 0) {
						total += (long) map.reduceValuesToInt(
							4,
							(value) ->  {
								if (value.contains("<http://www.w3.org/2001/XMLSchema#string>"))
									return 1;
								else
									return 0;				
							},
							0,
							(x1, x2) -> {	
								return  x1+x2;	
							}
						);
						map.clear();				
					}

					i++;
				}
			}

			total += (long) map.reduceValuesToInt(
				4,
				(value) ->  {
					if (value.contains("<http://www.w3.org/2001/XMLSchema#string>"))
						return 1;
					else
						return 0;				
				},
				0,
				(x1, x2) -> {	
					return  x1+x2;	
				}
			);
			map.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Result = "+total);
	}
}
