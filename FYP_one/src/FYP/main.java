package FYP;


import java.util.List;

import javax.swing.SwingUtilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alee.laf.WebLookAndFeel;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;



import java.io.*;

public class main {

	
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
			//Parsing();
//			ExcelReader excel = new ExcelReader("C:\\Users\\ABC\\Documents\\training.xlsx");
//			System.out.println(excel.ReadExcel());
//			PrintWriter fileout = new PrintWriter("Adswekatraining.arff");
//			fileout.print(excel.ReadExcel());
//			fileout.close();
		SwingUtilities.invokeLater ( new Runnable ()
        {
			public void run ()
            {
		          try {
		        	  WebLookAndFeel.install ();
					WebviewApp WV = new WebviewApp();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
            }
        });
	
	
	}


	 
	
	
	
	public static void Parsing() throws Exception {
		
		
		
		WebClient  webClient= new  WebClient(BrowserVersion.CHROME ); 
		String url = "http://www.nytimes.com/"; 
		String Filename = url.substring(url.indexOf("/")+2, url.lastIndexOf("m/"));
		Filename.replaceAll("[^a-zA-Z]", "");
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.getOptions().setCssEnabled(true);
	    webClient.getOptions().setTimeout(50000); 
	    webClient.setAjaxController(new NicelyResynchronizingAjaxController());
	    webClient.waitForBackgroundJavaScript(50000);
	    HtmlPage page = webClient.getPage(url);
	    DomNodeList<DomElement> list = page.getElementsByTagName("img");

	    
	    String pageXml = page.asXml(); 
	    Document doc = Jsoup.parse(pageXml);
		Elements imgtag = doc.select("a:has(img):not(:has(div))");
		Elements iframetag = doc.select("iframe");

     	PrintWriter out = new PrintWriter(Filename+".txt");
	    final List<?> imgs = page.getByXPath("//img");
        System.out.println(pageXml);
        for(Object e: imgs){
        }
		out.println("a tag");
		for(Element element:imgtag){
		out.println("taarget href: " + element.attr("href").toString());
		out.println("img src: " + element.child(0).attr("abs:src").toString()+ "|title :" + element.child(0).attr("title").toString() + "|width :" + element.child(0).attr("width").toString()+ "|height :" + element.child(0).attr("height").toString());
		out.println("___________________");
		}
		out.println("Iframe");
		for(Element element:iframetag){
		//out.println(element.toString());
		out.println("ID :"+ element.id().toString() + "|width :"+ element.attr("width").toString()+ "|height :"+ element.attr("height").toString() );
		out.println("src :" + element.attr("abs:src").toString());
		out.println("___________________");
		}

		for(DomElement e:list){
			HtmlElement tempele = (HtmlElement)e;
			ComputedCSSStyleDeclaration style =
				    ((HTMLElement)(tempele).getScriptableObject()).getCurrentStyle();
			System.out.println(style.getAttribute("alt", 1).toString()+"*"+style.getWidth()+"*" + style.getHeight());
		}

		out.close();
	  
	}
    

	
	
	
	
	
}
