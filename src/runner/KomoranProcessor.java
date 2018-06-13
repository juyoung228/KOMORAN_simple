package runner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import cc.mallet.types.Token;
import kr.co.shineware.util.common.model.Pair;
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;

public class KomoranProcessor {
		
	Komoran komoran = null;
		
	public KomoranProcessor() {
		komoran = new Komoran("./models");	
		komoran.setUserDic("./dic.txt");
	}
		

	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader("./kt_result.txt"));
		BufferedWriter out = new BufferedWriter(new FileWriter("./kt_result_morph.txt"));
		String line;
			
		Komoran komoran = new Komoran("./models");
			
		
		int i=0;
			
		while(((line=in.readLine())!=null)){
			 if(line.startsWith("2017")||line.startsWith("2018")) {
				System.out.println(line);
				String data = "";
				
				try {
						data = line.split("\t")[1];	
						//System.out.println(data);
						i++;
					
				} catch (Exception e) {
					System.out.println(line);
					continue;
				}
				
				data = data.replaceAll("[^\\p{L}\\p{Z}]","");
				
				
				String[] datas = data.split("\\.");
				
				for(String t_data : datas) {
					
					ArrayList<String> temp = new ArrayList<String>();
					
					List<List<Pair<String,String>>> result = komoran.analyze(t_data);
					
					for (List<Pair<String, String>> eojeolResult : result) {
						for (Pair<String, String> wordMorph : eojeolResult) {
							String term = wordMorph.getFirst();
							String pos = wordMorph.getSecond();
							
							if(term.length()<2) continue;
							//if( !pos.startsWith("EC") && !pos.startsWith("JKB") 
							//		&&  !pos.startsWith("ETM") ) {
							temp.add(term+ "-" + pos);
							out.write(term+ "-" + pos + "\t");;
							
						}
					}
					System.out.println(temp);
					out.write("\n");

				}
				
				
				
			}
		}
			System.out.println(i);
			
			in.close();
			out.close();

		}
	}

