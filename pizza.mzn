include "globals.mzn";

int: n;
array[1..n] of int: price;
int: m;
array[1..m] of int: buy;
array[1..m] of int: free;

array [1..n, 1..m+1] of var 0..1: vP;
array [1..n, 1..m+1] of var 0..1: vF;

var 0..10000 :cost;

% Sum of rows = 1
constraint forall (i in 1..n) (sum(j in 1..m+1) (vP[i, j]+vF[i, j]) = 1);

% Inte köpa för många per kupong
constraint forall (j in 1..m) (sum(i in 1..n) (vP[i, j]) <= buy[j]);

% Inte för många gratis per kupong och alla köpta måste va uppfyllda
constraint forall (j in 1..m) (sum(i in 1..n) (vF[i, j]) <= if sum(i in 1..n) (vP[i, j]) = buy[j] then free[j] else 0 endif);
constraint sum(i in 1..n) (vF[i, m+1]) = 0;

% Alla gratispizzor måste va billigare än de köpta
constraint forall(i in 1..n, j in 1..m, k in 1..n) (price[i]*vF[i, j] <= if vP[k, j]>0 then vP[k, j]*price[k] else 1000 endif);

% Total kostnad
constraint cost = sum(i in 1..n, j in 1..m+1) (vP[i, j]*price[i]);

solve minimize cost;

output[show(cost)++"\n"] ++ ["\npaid pizzas\n"]++[show(vP[i,j]) ++ if i=n then "\n" else " " endif | j in 1..m+1, i in 1..n]++["\nfree pizzas\n"]++[show(vF[i,j]) ++ if i=n then "\n" else " " endif | j in 1..m, i in 1..n];

n = 10;
price = [70,10,60,60,30,
100,60,40,60,20];
m = 4;
buy = [1,2,1,1];
free = [1,1,1,0];



