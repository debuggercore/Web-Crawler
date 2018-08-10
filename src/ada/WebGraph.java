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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.*;
import java.util.Scanner;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.formula.udf.UDFFinder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class WebGraph {
   static int a[][]=new int[10000][10000];
   public static int arrayPointer=-1;
   static Connection conn;
   static Statement stmt;
    static URL aURL;
     static int rs;
     static  ResultSet res;
    static int point[]=new int[3];
  
   public static void main(String args[]) throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
       
        File urls= new File("C:\\Users\\SUJITHA\\Documents\\NetBeansProjects\\ADA\\src\\ada\\urls.txt");
        Scanner in=new Scanner(urls);
      
        
         Class.forName("com.mysql.jdbc.Driver");
         String USER = "root"; 
         String PASS = "";
        System.out.println("Connecting to database..."); 
        conn = DriverManager.getConnection("jdbc:mysql://localhost/urldatabase",USER,PASS); 
        System.out.println("Creating statement..."); 
        stmt = conn.createStatement(); 
        String sql; 
      
        //Insert urls from file into db and increment arrayPointer
        //In-Degree of these urls will be zero
        int l=1; //Used in display method
        while(in.hasNext()){
            arrayPointer++;
            String s=in.nextLine(); 
            
            //Empty the database
            sql="delete from urlvisited";
            rs=stmt.executeUpdate(sql);
            
            sql = "INSERT INTO  urlvisited (urls, array_index, out_degree, in_degree, page_rank, link_name)VALUES ('"+s+"',"+arrayPointer+",0,0,0,'"+s+"')";
            rs = stmt.executeUpdate(sql);    
           webCrawler(l++);
          
           arrayPointer=-1;
           
        }
       
       
}
  
    public static void update(String url, int i, String name) throws SQLException, MalformedURLException{
        String sql;
        ResultSet res;
        int rs;
        sql="select array_index from urlvisited where urls='"+url+"'";
        res=stmt.executeQuery(sql);
            
        //Child link is not visited before
        if(!res.isBeforeFirst()){
            arrayPointer++;
            sql = "INSERT INTO  urlvisited(urls, array_index, out_degree, in_degree, page_rank, link_name) VALUES ('"+url+"',"+arrayPointer+",0,1,0,'"+name+"')";
            rs = stmt.executeUpdate(sql);
            a[i][arrayPointer]=1;

        }

        else{

            int j= 0;
            while(res.next())
                j=res.getInt("array_index");
            
            //If two or more identical links are scanned
            if(a[i][j]!=1){
               a[i][j]=1;
               sql="update urlvisited set in_degree=in_degree+1 where array_index="+j;
               rs=stmt.executeUpdate(sql);
            }
        }          
          
    }
    
    public static void computePageRank(double c) throws SQLException{
        //PR(A) = (1 - d )/N + d * ( PR(B)/L(B)+ PR(C)/L(C) + PR(D)/L(D) + . . . )
        //d=0.85
        String sql;
        ResultSet res;
        int rs;
        int out_degree = 0;double sum=0 ;
        double ans;
        double page_rank = 0,temp;
        for(int i=0;i<=arrayPointer;i++){
            ans=0.0;
            sum=0.0;
            for(int j=0;j<=arrayPointer;j++){
                if(a[j][i]==1){
                    
                    sql="select page_rank ,out_degree from urlvisited where array_index="+j;
                    res=stmt.executeQuery(sql);
                    while(res.next()){   
                      out_degree=res.getInt("out_degree");
                      page_rank=res.getDouble("page_rank");
                      
                    }
                    if(out_degree!=0){
                      temp=page_rank/out_degree;

                      sum+=temp;
                    }                 
                }
                
            }
            
            ans=c+0.85*sum;
            // System.out.println(sum+" "+c+" "+ans);
            sql="update urlvisited set page_rank=page_rank+"+ans+" where array_index="+i;
            rs=stmt.executeUpdate(sql);
        }
    }
    
    //Only for those links processed
    public static int calculate_avg_outdegree() throws SQLException{ 
            int count = 0;
            int sum=0;
        
            String sql;
          
            sql="select out_degree from urlvisited where out_degree>0";
            res=stmt.executeQuery(sql);
            while(res.next()){
                count++;
                sum+=res.getInt("out_degree");
            }
           
            if(count!=0)
            return sum/count;
            else 
                return 0;
    }
        public static int calculate_avg_indegree() throws SQLException{
              
            int sum=0;
        
            String sql;
          
            sql="select in_degree from urlvisited ";
            res=stmt.executeQuery(sql);
            while(res.next()){
                
                sum+=res.getInt("in_degree");
            }
           
           
            return sum/(arrayPointer+1);
           
         }
        public static double calculate_avg_pagerank() throws SQLException{
         
            double sum=0;
            double avg=0;
            String sql;
          
            sql="select avg(page_rank) from urlvisited ";
            res=stmt.executeQuery(sql);
            while(res.next()){
            avg=res.getDouble("avg(page_rank)");
              
            }
           
            
            return avg;
           
        }
        
       public static void displayexcel(int l) throws SQLException, FileNotFoundException{
          
             String sql ;
             
              try{ 
              FileOutputStream fileOut = new FileOutputStream("poi-test"+l+".xls");
		HSSFWorkbook wb = new HSSFWorkbook() ;
               HSSFSheet worksheet = wb.createSheet("sheet1"); 
              
               for(int j=0;j<=arrayPointer;j++){ 
                   HSSFRow row = worksheet.createRow(j+1);
                for(int i=0;i<point.length;i++){
                    
                   
                    int temp=a[point[i]][j];
                       
                        HSSFCell cellA1 = row.createCell(i+1);
                  
                     cellA1.setCellValue(temp);
                     worksheet.autoSizeColumn(i+1);
                            }
                }
                for(int i=0;i<=arrayPointer;i++){
                    int j=1;
                    HSSFRow row = worksheet.getRow(i+1);
                  sql = "select urls from urlvisited where array_index="+i;
                    res=stmt.executeQuery(sql);
                  
                    HSSFCell cellA1 = row.createCell(0);
                    while(res.next())
                        cellA1.setCellValue(res.getString("urls"));
                  HSSFCreationHelper createHelper = wb.getCreationHelper();
                  HSSFCellStyle cellStyle = wb.createCellStyle();
                   worksheet.autoSizeColumn(0);
                   
                  
            
                } 
                 HSSFRow row = worksheet.createRow(0);
               for(int i=0;i<point.length;i++){
                  
                  HSSFCell cellA1 = row.createCell(i+1);
                     sql="select urls from urlvisited where array_index="+point[i];
                     res=stmt.executeQuery(sql);
                     while(res.next()){
                       
                         cellA1.setCellValue(res.getString("urls"));
                         worksheet.autoSizeColumn(i+1);
                     }
               }
               int j=0;
               for(int i=0;i<6;i++){
                  
                  row = worksheet.createRow(i+arrayPointer+5+j);
                  HSSFCell cell=row.createCell(0);
                  if(i==0)
                  cell.setCellValue("Average in degree = "+calculate_avg_indegree());
                  else if(i==1)
                      cell.setCellValue("Average out degree = "+calculate_avg_outdegree());
                  else if(i==2){
                      cell.setCellValue("Average page rank = "+calculate_avg_pagerank());
                  }
                  else if (i==3){
                      
                      sql ="select urls from urlvisited  where in_degree in (select max(in_degree) from urlvisited)";
                      res=stmt.executeQuery(sql) ;
                      String temp = null ;
                      cell.setCellValue("Urls with max in-degree are:");
                      while(res.next()){
                         j++;
                        row=worksheet.createRow(i+arrayPointer+5+j);
                        cell=row.createCell(0);
                        
                        temp=res.getString("urls");
                        cell.setCellValue(temp);
                        
                      }
                      
                  }
                  
                  else if (i==4){
                      sql ="select urls from urlvisited  where out_degree in (select max(out_degree) from urlvisited)";
                      res=stmt.executeQuery(sql) ;
                      String temp = null ;
                      cell.setCellValue("Urls with max out-degree are:");
                      while(res.next()){
                           j++;
                        row=worksheet.createRow(i+arrayPointer+5+j);
                        cell=row.createCell(0);
                      temp=res.getString("urls");
                      cell.setCellValue(temp);
                      }
                      
                  }
                  else {
                      sql ="select urls from urlvisited  where page_rank in (select max(page_rank) from urlvisited)";
                      res=stmt.executeQuery(sql) ;
                      String temp = null ;
                      cell.setCellValue("Urls with max page_rank are:");
                      while(res.next()){
                      temp=res.getString("urls");
                       j++;
                        row=worksheet.createRow(i+arrayPointer+5+j);
                        cell=row.createCell(0);
                        cell.setCellValue(temp);
                      }
                      
                  }
                  
               }
               
               
               
    
   
                        wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
             
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        System.out.println( "File created!" );
	}
       
    public static void webCrawler(int m) throws SQLException, IOException, ClassNotFoundException{
        ResultSet res;
        int i;
        for( i=0;i<3;i++){
            int count=0;    //It is the no of links scanned in the webpage
            int temp;
            temp=arrayPointer;
            point[i]=temp;
            
            String sql = "select urls from urlvisited where array_index="+temp;
            res=stmt.executeQuery(sql);
            
            String url= null;
            while(res.next())
                url = res.getString("urls");
            Document document = Jsoup.connect(url).get();
            
            //Scanning all links in the webpage
            Elements links= document.select("#mw-content-text a[href]");
            for(Element l : links){
            
                if(l.text().length()>20&&!l.text().contains("'")){
                    System.out.println(l.absUrl("href"));
                    count++;
                    String url2 = l.absUrl("href");
                    update(url2,temp,l.text());

                }
        
            }
            
            sql = "update urlvisited set out_degree="+count+" where array_index="+temp;
            rs=stmt.executeUpdate(sql);
        }
         //Compute 1/N
        //set initial value to page rank;
        double num= 1.0/(arrayPointer+1);
        String sql = "update urlvisited set page_rank="+num;
        rs=stmt.executeUpdate(sql);
           
        //Compute (1-d)/N where N=arrayPointer+1 and d=0.85
        double constant=(1.0-0.85)/(arrayPointer+1);
        computePageRank(constant);
        
        BFS travel=new BFS();
        travel.BFS(a,arrayPointer);
       // DFS travel1 = new DFS();
        //travel1.DFS(arrayPointer+1, a);
     
        displayexcel(m);
       
    }
}
        
        
        
    

