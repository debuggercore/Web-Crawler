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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class ADA {

    public static void main(String[] args) throws Exception {
        String url = "http://en.wikipedia.org/wiki/PES_University";
        Document document = Jsoup.connect(url).get();
        Elements links= document.select("#mw-content-text a[href]");
        
       /* Elements links= document.select("#answers .post-text [href]");
        

        /*String question = document.select("#question .post-text").text();
        System.out.println("Question: " + question);

        Elements answerers = document.select("#answers .user-details a");
        for (Element answerer : answerers) {
            System.out.println("Answerer: " + answerer.text());
        }*/
       




     
       for(Element l : links){
            if(l.text().length()>10){
                System.out.println("Links: "+l.text());
            System.out.println("URL: "+l.absUrl("href"));
            String url2 = l.absUrl("href");
                    document = Jsoup.connect(url2).get();
                    
                    Elements links2= document.select("#mw-content-text a[href]");
                 for(Element l2 : links2){
            if(l2.text().length()>10){
                System.out.println("\t Links: "+l2.text());
            System.out.println("\t\tURL: "+l2.absUrl("href"));
            System.out.println("\b\b");
                   
                    
            } 
              }
          
                    
            }          
            
        }
        
    }
    
    
    
    
    
    
        
        

}