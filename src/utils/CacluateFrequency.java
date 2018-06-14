package utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

//ref: http://bboong100.tistory.com/9 [개발새발]
//ref: http://ekfqkqhd.tistory.com/entry/Java-HashMap-Value-%EC%A0%95%EB%A0%AC
	
public class CacluateFrequency {

	public static void main(String[] args) throws IOException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		BufferedReader in = new BufferedReader(new FileReader("./kt_result_morph.txt"));
		BufferedWriter out = new BufferedWriter(new FileWriter("./kt_frequency.txt"));

		String line;
		while(((line=in.readLine())!=null)){
			String[] words = line.split("\t");
			for (String w : words){
				if (!map.containsKey(w)){
					map.put(w, 1);
				}else{
					map.put(w, map.get(w)+ 1);
				}
			}
		/*
		 * 1. Set<String> s = map.keySet();
		 * 2. Iterator<String> keys = s.iterator();
		 * 아래와 같이 1라인으로 변환 가능
		 * */
			
		}
		//Iterator<String> keys = map.keySet().iterator();
		Iterator keys = sortByValue(map).iterator();
		while(keys.hasNext()){
			String key = (String) keys.next();
			System.out.println(key + "\t" + map.get(key));
			out.write(key + "\t" + map.get(key)+"\n");
		}
		
		out.close();
	}
	
	
	public static List sortByValue(final Map m){
        List<String> list = new ArrayList();
        list.addAll(m.keySet());
         
        Collections.sort(list,new Comparator(){
             
            public int compare(Object o1,Object o2){
                Object v1 = m.get(o1);
                Object v2 = m.get(o2);
                 
                return ((Comparable) v1).compareTo(v2);
            }
             
        });
        Collections.reverse(list); // 주석시 오름차순
        return list;
    }
			
	
}
