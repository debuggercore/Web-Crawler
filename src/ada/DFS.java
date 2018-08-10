/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ada;

/**
 *
 * @author SUJITHA
 */
public class DFS {
    int visited[]= new int[10000];
int count=0;




        
void DFS(int n , int a[][]){
int i ;
for(i=0 ; i<n ; i++){
    if(visited[i]==0){
        count++;
        visited[i]=1;
        
        System.out.println(i);
        dfs( n , a , i);

       
    }

}
}

void dfs(int n ,int a[][] ,int i){
    int j;
for(j=0 ; j< n ; j++){
   if(a[i][j]==1) {
    if(visited[j]==0){
        visited[j]=1;
        
        
        System.out.println(j);
        dfs(n , a , j);

    }
   }
}
}
}
        
       
        
   
    















