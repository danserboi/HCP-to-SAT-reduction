/* SERBOI FLOREA-DAN 325CB */
import java.io.*;

public class Tema2AA {

	public static void main(String[] args) throws Exception {
		/* deschidem fisierul si citim linie cu linie */
		File input = new File("graph.in");
		File output = new File("bexpr.out");
		PrintWriter printWriter = new PrintWriter(output);
		BufferedReader buffer = new BufferedReader(new FileReader(input));
		/* citim prima linie si extragem numarul de noduri */
		String line = buffer.readLine();
		int noVertex = Integer.parseInt(line);
		/* initializam matricea de adiacenta */
		int[][] graph = new int[noVertex + 1][noVertex + 1];
		/* citim linie cu linie muchiile */
		while ((line = buffer.readLine()) != null) {
			/* extragem nodurile unei muchii si actualizam matricea de adiacenta */
			String[] fields = line.split(" ");
			int node1 = Integer.parseInt(fields[0]);
			/* citim pana cand intalnim -1 */
			if (node1 == -1)
				break;
			int node2 = Integer.parseInt(fields[1]);
			graph[node1][node2] = 1;
			graph[node2][node1] = 1;
		}
		int i, j, k, t;
		StringBuilder result = new StringBuilder();
		/* tratam separat cazul cand exista un nod care nu are muchie cu cel putin 2 noduri 
		 * (implicit nu exista niciun ciclu)
		 * prin urmare, formula trebuie sa aiba valoarea 0 */
		int firstCase = 0;		
		for(i = 1; i <= noVertex; i++) {
			int no = 0;	
			for(j = 1; j <= noVertex; j++) {
				if(graph[i][j] == 1) {
					no++;					
				}			
			}
			if(no <= 1)
				firstCase = 1;
		}
		if(firstCase == 1)
			result.append(String.format("x1-1&~x1-1"));
		else
		{
			/* in prima parte realizez constructii de genul ((x i-j & x i-k & ~x i-t ...) | ...) &
			 * & (a j-i|...) & ((x (i+1)-j & x i-k & ~x i-t ...) | ...)) & ...*/
			int ok = 0;
			for(i = 1; i <= noVertex; i++) {
				int counter = 0;
				if(ok == 1)
					result.append("&(");
				else
					result.append("(");
				for(j = 1; j <= noVertex; j++) {
					if(i != j && graph[i][j] == 1) {
						ok = 1;
						/* construiesc x i-j */
						int counter2 = 0;
						StringBuilder sb = new StringBuilder(String.format("(x%d-%d", i, j));
						StringBuilder sb2 = null;
						for(k = j + 1; k <= noVertex; k++) {
							if(graph[i][k] == 1) {
								/* construiesc x i-j & x i-k */
								sb2 = new StringBuilder(sb);
								sb2.append(String.format("&x%d-%d", i, k));
								/* construiesc (x i-j & x i-k & ~x i-t ...) */
								for(t = 1; t <= noVertex; t++) {
									if(graph[i][t] == 1 && j !=t && k != t) {
										sb2.append(String.format("&~x%d-%d", i, t));
									}
								}
								sb2.append(")");
								/* daca nu este prima constructie (x i-j & x i-k & ~x i-t ...) a 
								 * nodului i, trebuie sa adaug | inainte */
								if(counter2 >= 1 || (counter >= 1 && counter2 == 0)) {
									StringBuilder sb3 = new StringBuilder("|");
									sb2 = sb3.append(sb2);
								}
								counter2++;
								if(sb2 != null)
									result.append(sb2);
							}
						}
						counter++;
					}
				}
				if(counter != 0) {
					result.append(")");
				}
				/* la sfarsitul nodului tratat pun toate posibilitatile lungimii cailor de la 1 la i
				 * in drumul ales care sunt de forma (a j-i|...)
				 * j ia valori de la 1 la noVertex/2 + 1*/
				if(counter != 0 )
					if(i >= 2) {
						result.append("&(");
						for(j = 1; j <= noVertex/2 + 1; j++) {
							if(j == 1)
								result.append(String.format("a%d-%d", j, i));
							else
								result.append(String.format("|a%d-%d", j, i));
						}
						result.append(")");
					}
			}
			/* in a doua parte verific daca exista muchia x i-j prin constructii
			 * de genul ((x i-j | ~x i-j) & (~x i-j | x i-j)) & ... */
			result.append("&");	
			ok = 0;
			for(i = 1; i <= noVertex; i++) {
				int counter = 0;
				for(j = i + 1; j <= noVertex; j++) {
					if(graph[i][j] == 1) {
						counter++;
						if(counter == 1 && ok == 0){
							result.append("((" + String.format("x%d-%d|~x%d-%d", i, j, j, i) + ")");
							result.append("&");
							result.append("(" + String.format("~x%d-%d|x%d-%d", i, j, j, i) + "))");					
						}
						else{
							result.append("&((" + String.format("x%d-%d|~x%d-%d", i, j, j, i) +")");
							result.append("&");
							result.append("(" + String.format("~x%d-%d|x%d-%d", i, j, j, i) + "))");					
						}
						ok = 1;
					}
				}
			}
			/* in a treia parte tratez fiecare lungime a celei mai scurte cai de la 1 la nod,
			 * pentru fiecare nod */
			result.append("&");
			/* tratez separat cazul cand cea mai scurta cale de la 1 la nod are lungimea 1*/
			/* realizez constructii de genul ((a 1-j|~x 1-j)&(~a 1-j|x 1-j)) & ...  */
			ok = 0;
			for(j = 2; j <= noVertex; j++) {
				if(graph[1][j] == 1) {
					if(ok == 0) {
						result.append(String.format("((a%d-%d|~x%d-%d)", 1, j, 1, j));
						result.append(String.format("&(~a%d-%d|x%d-%d))", 1, j, 1, j));	
					}
					else {
						result.append(String.format("&((a%d-%d|~x%d-%d)", 1, j, 1, j));
						result.append(String.format("&(~a%d-%d|x%d-%d))", 1, j, 1, j));
					}
					ok = 1;
				}
			}
			/* realizez constructii de genul & ~ a 1-j & ... daca nu exista muchie intre 1 si j */
			for(j = 1; j <=noVertex; j++) {
				if(graph[1][j] != 1) {
					result.append(String.format("&~a%d-%d", 1, j));
				}
			}
			StringBuilder sbThirdCase = null;
			/* realizez constructii de genul ((a i-j|~(((a (i-1)-k & x k-j)|...) & ~(a t-i| ...)))
			 * &(~a i-j|(((a (i-1)-k & x k-j)|...) & ~(a t-i| ...))))&...*/
			for(i = 2; i <= noVertex/2 + 1; i++) {
				for(j = 2; j <= noVertex; j++) {
					sbThirdCase = new StringBuilder();
					ok = 0;
					for(k = 2; k <= noVertex; k++) {
						if(graph[j][k] == 1) {
							if(ok == 0)
								sbThirdCase.append(String.format("(a%d-%d&x%d-%d)", i - 1, k, k,j));
							else
								sbThirdCase.append(String.format("|(a%d-%d&x%d-%d)", i - 1, k,k,j));
							ok = 1;
						}	
					}
					if(ok == 1) {
						result.append(String.format("&((a%d-%d|~((", i, j));
						result.append(sbThirdCase);
						result.append(")&~(");
						for(t = 1; t <= i-1; t++) {
							if(t == 1)
								result.append(String.format("a%d-%d", t, j));
							else
								result.append(String.format("|a%d-%d", t, j));							
						}
						result.append(")))");
						result.append("&");
						result.append(String.format("(~a%d-%d|((", i, j));
						result.append(sbThirdCase);
						result.append(")&~(");
						for(t = 1; t <= i-1; t++) {
							if(t == 1)
								result.append(String.format("a%d-%d", t, j));
							else
								result.append(String.format("|a%d-%d", t, j));							
						}
						result.append("))))");
					}
				}
			}
		}
		/* scriu formula rezultata in fisierul cerut */
		printWriter.print(result.toString());
		printWriter.close();
		buffer.close();
	}
}