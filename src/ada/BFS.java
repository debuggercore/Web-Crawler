/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ada;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author SUJITHA
 */
public class BFS {
    int a[][];
int s[]=new int[1000];
int q[]=new int[10000];
int f=0,r=-1,count;
static Connection conn;
   static Statement stmt;
   ResultSet res;
   int rs;
public BFS() throws ClassNotFoundException, SQLException{
 Class.forName("com.mysql.jdbc.Driver");
         String USER = "root"; 
         String PASS = "";
        System.out.println("Connecting to database..."); 
        conn = DriverManager.getConnection("jdbc:mysql://localhost/urldatabase",USER,PASS); 
        System.out.println("Creating statement..."); 
        stmt = conn.createStatement();
        
        
}
void BFS(int a[][],int size) throws SQLException{
   count=0;
   this.a=a;
   String sql;
    int i,j;
    for(i=0;i<=size;i++){
        if(s[i]==0){
           /*sql="select urls from urlvisited where array_index="+i;
           res=stmt.executeQuery(sql);
           while(res.next()){
               //String s=res.getString("urls");
               p
           }*/
            System.out.print(i+"\t");
           bfs(i,size);
           
        }

    }
    
}
void bfs(int i,int size){
    count++;
    s[i]=count;
    insert(i);
   
    while(!empty()){

        int j;
        for(j=0;j<=size;j++){
          
            if(a[front()][j]==1&&s[j]==0)
            {
                System.out.print(j+"\t");
                count++;
                s[j]=count;
                insert(j);

            }
            
        }
        
        del();
    }
    System.out.println();
}
void init()
{
	f = 0;
	r = -1;
}

boolean empty()
{
	return f > r;
}

void insert(int x)
{
	if(r < f)
	{
		r = -1;
		f = 0;
	}
	q[++r] = x;
}

int del() // not checking empty
{
	if(f == r)
	{
		r = -1;
	}
	return q[f++];
}

int front(){
    return q[f];
}
}












