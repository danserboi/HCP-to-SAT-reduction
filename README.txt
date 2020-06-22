Am ales sa implementez tema in Java. Avem doar o clasa, clasa main, care contine doar metoda main.
In aceasta metoda realizam citirea grafului din fisier si scrierea transformarii realizate in 
fisierul cerut.
Citim prima linie si extragem numarul de noduri dupa care citim linie cu linie muchiile si extragem 
nodurile unei muchii pentru ca in final sa obtinem matricea de adiacenta.
Apoi realizam transformarea polinomiala.
Tratam separat cazul cand exista un nod care nu are muchie cu cel putin 2 noduri (implicit nu 
exista niciun ciclu). In acest caz, scriem o formula pentru care valoarea este mereu 0. In cest 
caz, transformarea are o complexitate O(n^2) deoarece sunt imbricate 2 for-uri care parcurg de la 1 
la n, iar in interiorul for-urilor instructiunile au complexitate O(1).
In cel de-al doilea caz, "impartim" transformarea in 3 parti.
In prima parte realizez constructii de genul ((x i-j & x i-k & ~x i-t ...) | ...) & (a j-i|...) & 
((x (i+1)-j & x i-k & ~x i-t ...) | ...)) & ... . Aici avem o complexitate O(n^4) deoarece sunt 
imbricate 4 for-uri care realizeaza maxim n pasi, iar in interiorul for-urilor instructiunile au 
complexitate O(1).
In a doua parte verific daca exista muchia x i-j prin constructii de genul ((x i-j | ~x i-j) & (~x 
i-j | x i-j)) & ... . Aici avem o complexitate O(n^2) deoarece sunt imbricate 2 for-uri care 
parcurg de la 1 la n, respectiv de la index-ul primului for + 1 pana la n, iar in interiorul 
for-urilor instructiunile au complexitate O(1).
In a treia parte tratez fiecare lungime a celei mai scurte cai de la 1 la nod, pentru fiecare nod.
Aici, tratez separat cazul cand cea mai scurta cale de la 1 la nod are lungimea 1. Realizez 
constructii de genul ((a 1-j|~x 1-j)&(~a 1-j|x 1-j)) & ...  si constructii de genul & ~ a 1-j & ... 
daca nu exista muchie intre 1 si j . Complexitatea este O(n) deoarece avem 2 for-uri separate care 
realizeaza cel mult n pasi, iar in interiorul lor instructiunile au complexitate O(1).
Altfel, realizez constructii de genul ((a i-j|~(((a (i-1)-k & x k-j)|...) & ~(a t-i| ...)))&(~a 
i-j|(((a (i-1)-k & x k-j)|...) & ~(a t-i| ...))))&... . Aici avem o complexitate O(n^4) deoarece 
sunt imbricate 4 for-uri care realizeaza maxim n pasi, iar in interiorul for-urilor instructiunile 
au complexitate O(1).
Complexitatea celui de-al doilea caz este O(n^4), deoarece fiecare din cele 3 parti are o 
complexitate cel mult O(n^4).
Avand in vedere cazurile tratate si complexitatea lor, deducem ca avem o complexitate a 
transformarii O(n^4). Concluzionam astfel ca transformarea realizata este polinomiala.
In final, se scrie formula rezultata in fisierul cerut.
