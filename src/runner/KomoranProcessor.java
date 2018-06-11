package runner;

	import java.io.BufferedReader;
	import java.io.BufferedWriter;
	import java.io.FileReader;
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
		
		public ArrayList<String> tokenize(String text)
		{
			ArrayList<String> words = new ArrayList();
			text = text.replaceAll("[^\\p{L}\\p{Z}]","");
			try {	
				
				List<List<Pair<String,String>>> result = komoran.analyze(text);
					
				for (List<Pair<String, String>> eojeolResult : result) {
						for (Pair<String, String> wordMorph : eojeolResult) {
							String term = wordMorph.getFirst();
							String pos = wordMorph.getSecond();
							
							if(term.length()<2) continue;
							
							if( !pos.startsWith("EC") && !pos.startsWith("JKB") 
									&&  !pos.startsWith("ETM") ) {
					    		words.add(term);
							}
							
						}
				} 

			} catch (Exception e) {
				String[] terms = text.split("\\s+");
				for (int i = 0; i < terms.length; ++i) {
					String term = terms[i];
					words.add(term);
				}
			}
			
			return words;
		}
		
		public static void main(String[] args) throws Exception {
			BufferedReader in = new BufferedReader(new FileReader("./sample.txt"));
			// ������ ������ ����
			
			String line;
			
			Komoran komoran = new Komoran("./models");
			// komoran �� �ε�
			
		
			int i=0;
			
			while((line=in.readLine())!=null) {
				String data = "";
				try {
					if(line.split("\t").length>2) {
						data = line.split("\t")[0];		
					}
					
					// ���� �������� 100�� ���� �����͸� ó���� ����� ���� ����
					
					// �� ��� ����� ������ ������ ��� �� \n�� �״�� �־����� ������,
					// \n�� �� �͵� ���� ó�����ֱ� ���� �߰�
					
					else {
						data = line;
					}
				} catch (Exception e) {
					System.out.println(line);
					continue;
				}
				
				//data = data.replaceAll("[^A-Za-z0-9��-Ş\\. ]", "");
				data = data.replaceAll("[^\\p{L}\\p{Z}]","");
				
				// ����, �ѱ�, ����, '.' (���� ������ ����) �̿� ����
				
				String[] datas = data.split("\\.");
				// ���� ����
				
				for(String t_data : datas) {
					
					ArrayList<String> temp = new ArrayList<String>();
					
					List<List<Pair<String,String>>> result = komoran.analyze(t_data);
					
					for (List<Pair<String, String>> eojeolResult : result) {
						for (Pair<String, String> wordMorph : eojeolResult) {
							String term = wordMorph.getFirst();
							String pos = wordMorph.getSecond();
							
							if(term.length()<2) continue;
							if( !pos.startsWith("EC") && !pos.startsWith("JKB") 
									&&  !pos.startsWith("ETM") ) {
								temp.add(term
										+ "-" + pos
										);
								// ���, ���� ���� ������ �ǹ̾��� ǰ�� ����
							}
						}
					}
					System.out.println(temp);

				}
				
				i++;
				
				if(i%100000==0) System.out.println(i);
				// ���� ���� üũ �뵵
			}
			
			//System.out.println(i);
			
			in.close();

		}
	}

