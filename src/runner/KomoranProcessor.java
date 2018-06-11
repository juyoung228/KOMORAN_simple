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
			// 수집한 데이터 파일
			
			String line;
			
			Komoran komoran = new Komoran("./models");
			// komoran 모델 로딩
			
		
			int i=0;
			
			while((line=in.readLine())!=null) {
				String data = "";
				try {
					if(line.split("\t").length>2) {
						data = line.split("\t")[0];		
					}
					
					// 정부 동시출현 100개 출현 데이터를 처리할 당시의 파일 기준
					
					// 이 당시 사용한 데이터 파일이 기사 내 \n을 그대로 넣었었기 때문에,
					// \n이 들어간 것도 같이 처리해주기 위해 추가
					
					else {
						data = line;
					}
				} catch (Exception e) {
					System.out.println(line);
					continue;
				}
				
				//data = data.replaceAll("[^A-Za-z0-9가-힇\\. ]", "");
				data = data.replaceAll("[^\\p{L}\\p{Z}]","");
				
				// 영어, 한글, 숫자, '.' (문장 구분을 위해) 이외 삭제
				
				String[] datas = data.split("\\.");
				// 문장 구분
				
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
								// 명사, 동사 등을 제외한 의미없는 품사 제거
							}
						}
					}
					System.out.println(temp);

				}
				
				i++;
				
				if(i%100000==0) System.out.println(i);
				// 진행 정도 체크 용도
			}
			
			//System.out.println(i);
			
			in.close();

		}
	}

