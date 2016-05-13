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

	}

