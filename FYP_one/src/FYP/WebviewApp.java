package FYP;

import java.awt.Color;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.teamdev.jxbrowser.chromium.LoggerProvider;
import java.util.logging.*;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.bayes.*;
import weka.classifiers.trees.*;
import weka.classifiers.functions.SMO;

import com.alee.laf.label.WebLabel;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.NotificationStyle;
import com.alee.managers.notification.WebNotification;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.ContextMenuHandler;
import com.teamdev.jxbrowser.chromium.ContextMenuParams;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.dom.DOMNode;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;


import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;

import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.ADTree;
import weka.core.Instances;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Demonstrates how to embed Browser instance into JavaFX application.
 */


public class WebviewApp {
	public enum Classifier {
	    ADtree, NaiveBayes, RandomForest, J48,
	    SMO
	}
	static Classifier classifier = Classifier.ADtree; 
	private ARFF arff;
	double result;
	Instances dataSet;
    static DOMDocument document;
    static List<DOMElement> imgs;
    List<DOMElement> iframes;
    static Set<Long> framesIds;
    static boolean isfinishedloading;
    AttributeSelectedClassifier ASclassifier;
    public WebviewApp() throws Exception {
    	JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    	// BufferedReader reader = new BufferedReader(
         //        new FileReader("D:\\FYP\\FYP_one\\AdwholeBackUp3.arff"));
    	// dataSet = new Instances(reader);
    	// reader.close();
    	 //dataSet.setClassIndex(dataSet.numAttributes()-1);
    	 System.setProperty("teamdev.license.info", "true");
        Browser browser = new Browser();
        //browser.getCacheStorage().clearCache();
        //PrintWriter fileout = new PrintWriter("classifiermodel.txt");
        LoggerProvider.setLevel(Level.OFF);

//        AttributeSelectedClassifier ASclassifier = new AttributeSelectedClassifier();
//        PrincipalComponents pca = new PrincipalComponents();
//        Ranker ranker = new Ranker();
//        NaiveBayes NB = new NaiveBayes();
//        ASclassifier.setClassifier(NB);
//        ASclassifier.setEvaluator(pca);
//        ASclassifier.setSearch(ranker);
//        ASclassifier.buildClassifier(dataSet);
//        
//        fileout.print(ASclassifier.toString());
//        fileout.close();
        
        ActionListener RawDataActionListener = new ActionListener(){

			@Override
				// TODO Auto-generated method stub
		          public void actionPerformed(ActionEvent e)
		          {
		              File file =new File("./data/rawdata.txt");
		        	    if(!file.exists()){
		        	 	try {
							file.createNewFile();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		        	    }
		        	    FileWriter fw;
						try {
							fw = new FileWriter(file,true);
				      	     BufferedWriter bw = new BufferedWriter(fw);
				      	     PrintWriter fileout1 = new PrintWriter(bw);
				      	     
				      	   if(isfinishedloading){
				        		  for(DOMElement img : imgs){

				        			 img.setAttribute("id", "noH&W"+img.hashCode());
							         JSValue jwidth = browser.executeJavaScriptAndReturnValue(
							                    
							                  "document.getElementById('" + img.getAttribute("id") + "').width;"
							                  );
							         JSValue jheight = browser.executeJavaScriptAndReturnValue(
						
							                  "document.getElementById('" + img.getAttribute("id") + "').height;"
							                  );
				        			  fileout1.print("\r\n"+img.getAttribute("alt")+
				        					  ","+img.getAttribute("src")+
				        					  ","+img.getAttribute("width")+
				        					  ","+img.getAttribute("height")+
				        					  ","+img.getAttribute("style") +
				        					  ","+jwidth.getNumber()+
				        					  ","+jheight.getNumber()
				    						  );
				        			  
				        			  if(img.getParent()!=null){
				        				 DOMNode pNode= img.getParent();
				        				 DOMElement pEle = (DOMElement) pNode;
				        			   if(pNode.getNodeName().equalsIgnoreCase("a")){
				        						  fileout1.print(","+pEle.getAttribute("href")+
				        						  ","+pEle.getAttribute("id")+
				        						  ","+pEle.getAttribute("class")  
				        						  );
				        						  } 
				        			   }
				        			  }
				        		  fileout1.print("\r\n");

				        		  for (Long framesId : framesIds) {
				        			  if(browser.getDocument(framesId)!=null){
				        			   DOMDocument frameDOM = browser.getDocument(framesId);
				                       List<DOMElement> innerframes = frameDOM.findElements(By.tagName("iframe"));
				                       for(DOMElement iframe:innerframes ){
				                    	  if(!iframe.getAttribute("src").isEmpty()||!iframe.getAttribute("height").isEmpty()||!iframe.getAttribute("width").isEmpty()){
				                    		  fileout1.print(iframe.getAttribute("height")+
				            						  ","+iframe.getAttribute("width")+
				            						  ","+iframe.getAttribute("id")+
				            						  ","+iframe.getAttribute("name")+
				            						  ","+iframe.getAttribute("src")
				            						  ); 
					                    	  fileout1.print("\r\n");
					                    	  }
				                    	  }
				                       }
				        			  }
				        		  fileout1.close();	
					        		 WebNotification popup = new WebNotification ( NotificationStyle.mac );
					                 popup.setContent ( "Successfully Parsed Raw Data" ); 
					                 popup.setDisplayTime ( 3000 );
					                 NotificationManager.showNotification (popup);
				        		  }
				      	         
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		          }
        };
        ActionListener ClassifActionListener = new ActionListener(){
			@Override
        	public void actionPerformed(ActionEvent e)
            {   
        	   DataSource source;
			   DOMNode pNode = null;
			   DOMElement pEle = null;
			   try {
					arff = new ARFF();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                arff.SetLabel();
                arff.SetRelationName();

        		if(isfinishedloading == true){
            		try {
						source = new DataSource("./data/Model.arff");
						dataSet = source.getDataSet();
				    	dataSet.setClassIndex(dataSet.numAttributes()-1);					
				        ASclassifier = new AttributeSelectedClassifier();
				        PrincipalComponents pca = new PrincipalComponents();
				        Ranker ranker = new Ranker();
				        //NaiveBayes NB = new NaiveBayes();
				        
				        switch(classifier){
				        case ADtree:
				        	ADTree adtree = new ADTree();
				        	ASclassifier.setClassifier(adtree);
				        	break;
				        case NaiveBayes:
				        	NaiveBayes NB = new NaiveBayes();
				        	ASclassifier.setClassifier(NB);
				        	break;
				        case RandomForest:
				        	RandomForest RF = new RandomForest();
				        	ASclassifier.setClassifier(RF);
				        	break;
				        case J48:
				        	J48 j48 = new J48();
				        	ASclassifier.setClassifier(j48);
				        	break;
				        case SMO:
				        	SMO smo = new SMO();
				        	ASclassifier.setClassifier(smo);
				        	break;
						default:
							ADTree adtree1 = new ADTree();
					        ASclassifier.setClassifier(adtree1);
					        break;
				        }
				        
				        ASclassifier.setEvaluator(pca);
				        ASclassifier.setSearch(ranker);
				        ASclassifier.buildClassifier(dataSet);
				        //adtree.getNumOfBoostingIterations();
				    	
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            		
        			for (DOMElement img : imgs) {
        				arff.createInsatnce();
				    	String height = img.getAttribute("height");
				    	if(!height.isEmpty())
				    	{				    	
				    		Pattern p = Pattern.compile("-?\\d+");
				    	Matcher m = p.matcher(height);
				    	while (m.find()) {
					        arff.addfeatures("height", Double.parseDouble(m.group()));
				    		}
				        }else{
				        	img.setAttribute("id", "noH&W"+img.hashCode());
				        	 JSValue jheight = browser.executeJavaScriptAndReturnValue("document.getElementById('" + img.getAttribute("id") + "').height;");
	
						    	if(jheight!=null){
						    		jheight.getNumber();
						    		arff.addfeatures("height", jheight.getNumber());
						    	}
						    	else{
					        	arff.setMissingValueHeight();
						    	}
				        }
				    	String width = img.getAttribute("width");
				    	if(!width.isEmpty()){
				    	Pattern p = Pattern.compile("-?\\d+");
				    	Matcher m = p.matcher(width);
				    	while (m.find()) {
					        arff.addfeatures("width", Double.parseDouble(m.group()));
				    		}
                        }else{
    			        	img.setAttribute("id", "noH&W"+img.hashCode());
    			            JSValue jwidth = browser.executeJavaScriptAndReturnValue(
    				                   "document.getElementById('" + img.getAttribute("id") + "').width;"
    				                  );
    					    	
    					    	if(jwidth!=null){
    					    		jwidth.getNumber();
    					    		arff.addfeatures("width", jwidth.getNumber());
    					    	}
    					    	else{
    				        	arff.setMissingValueWidth();
    					    	}
                        }
					 for(int i =0; i<arff.strval.length;i++){
						 if(img.getParent()!=null){
							   pNode = img.getParent();
							   pEle =(DOMElement) pNode;
							 }
						 if(pEle!=null){
						 if(img.getAttribute("alt").contains(arff.strval[i])||img.getAttribute("src").contains(arff.strval[i])||img.getAttribute("title").contains(arff.strval[i])||pEle.getAttribute("href").contains(arff.strval[i])||pEle.getAttribute("id").contains(arff.strval[i])||pEle.getAttribute("class").contains(arff.strval[i]))
						 {
							arff.addfeatures(arff.strval[i],arff.strval[i]); 
						 }
						 else{
							 arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]); 
						 }
						 }
						 else{
							 if(img.getAttribute("alt").contains(arff.strval[i])||img.getAttribute("src").contains(arff.strval[i])||img.getAttribute("title").contains(arff.strval[i]))
							 {
								arff.addfeatures(arff.strval[i],arff.strval[i]); 
							 }
							 else{
								 arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]); 
							 }
						 }

						 }
					   arff.addfeatures("frameOrimage", "image");
					   arff.AddtoData();
				         try {
								//Classifier cls_co = (Classifier) weka.core.SerializationHelper.read("D:\\FYP\\FYP_one\\NaiveBayesOriginal.model");
								arff.getInstances().firstInstance();
								arff.SetLabel();
								//fileout.println(arff.getInstances().toString());
								result = ASclassifier.classifyInstance(arff.getInstances().firstInstance());
							} catch (Exception e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
					   if(arff.getInstances().classAttribute().value((int) result) == "AD"){
						  img.setAttribute("style", "visibility:hidden");
						  //fileout.println(arff.getInstances().firstInstance().toString());
						  //fileout.println(arff.getInstances().classAttribute().value((int) result));
					   }
					   arff.delteInstances();
				    }
        			
    	        	for (Long framesId : framesIds) {
    		        	
    		        			DOMDocument frameDOM = browser.getDocument(framesId);
    		                    List<DOMElement> innerframes = frameDOM.findElements(By.tagName("iframe"));
    					    for (DOMElement iframe : innerframes) {	
    					    	arff.createInsatnce();
    					        arff.SetLabel();
    					    	String height = iframe.getAttribute("height");
    					    	if(!height.isEmpty()){
    						    	if(height.contains("%")){
    						    		arff.setMissingValueHeight();
    						    	}
    						    	else{
    							    	Pattern p = Pattern.compile("-?\\d+");
    							    	Matcher m = p.matcher(height);
    							    	while (m.find()) {
    								        arff.addfeatures("height", Double.parseDouble(m.group()));
    							    		}
    						    	}
    		
    					        }
    					    	else{
    					    		arff.setMissingValueHeight();
    					    	}
    					    	String width = iframe.getAttribute("width");
    					    	if(!width.isEmpty()){
    					    	   if(width.contains("%")){
    					    		arff.setMissingValueWidth();	
    					    		}
    					    	   else{
    					    		 Pattern p = Pattern.compile("-?\\d+");
    							    	Matcher m = p.matcher(width);
    							    	while (m.find()) {
    								        arff.addfeatures("width", Double.parseDouble(m.group()));
    								        }
    							    	}
    					    	   }
    					    	else{
    					    	arff.setMissingValueWidth();
    					    	}
                                 for(int i =0; i<arff.strval.length;i++) {
    								 arff.SetLabel();
    								 if(iframe.getAttribute("onload").contains(arff.strval[i])||iframe.getAttribute("id").contains(arff.strval[i])||iframe.getAttribute("src").contains(arff.strval[i])||iframe.getAttribute("class").contains(arff.strval[i])||iframe.getAttribute("name").contains(arff.strval[i])){
    									arff.addfeatures(arff.strval[i],arff.strval[i]); 
    								 }
    								 else {  
    									arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]);
    								 }

    							 }
    							   arff.addfeatures("frameOrimage", "iframe");
    							   arff.AddtoData();
    						         try {
    										//Classifier cls_co = (Classifier) weka.core.SerializationHelper.read("D:\\FYP\\FYP_one\\NaiveBayesOriginal.model");
    										result = ASclassifier.classifyInstance(arff.getInstances().firstInstance());
    										} 
    						         catch (Exception e3) {
    										// TODO Auto-generated catch block
    										e3.printStackTrace();
    										}
    							   if(arff.getInstances().classAttribute().value((int) result) == "AD"){
    								  iframe.setAttribute("style", "visibility:hidden");
    							   }
    							   
    	                        arff.delteInstances();
    	                        }
    					    
    		        	  }
    	        	
    	        	}
        		 WebNotification popup = new WebNotification ( NotificationStyle.mac );
                 popup.setContent ( "Successfully Classified Using " ); 
                 popup.setDisplayTime ( 3000 );
                 NotificationManager.showNotification (popup);
        		}

        };
        ActionListener ARFFActionListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
	          {
				   DOMNode pNode = null;
				   DOMElement pEle = null;
	        	  DataSource source;
	        	  Instances inst1;
				try {
					source = new DataSource("./data/Model.arff");
		        	inst1 = source.getDataSet();
		       
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					source=null;
					inst1=null;
				}
	        	  
	        	try {
					arff = new ARFF();
					arff.SetLabel();
					arff.SetRelationName();
					for(DOMElement img:imgs){
						
						arff.createInsatnce();
				    	String height = img.getAttribute("height");
				    	if(!height.isEmpty())
				    	{				    	
				    		Pattern p = Pattern.compile("-?\\d+");
				    	Matcher m = p.matcher(height);
				    	while (m.find()) {
					        arff.addfeatures("height", Double.parseDouble(m.group()));
				    		}
				        }else{
				        	img.setAttribute("id", "noH&W"+img.hashCode());
				        	System.out.println(img.getAttribute("id"));
					    	//JSValue jheight = browser.executeJavaScriptAndReturnValue("$('#noH&W"+img.hashCode()+"').height()");
				            JSValue jheight = browser.executeJavaScriptAndReturnValue("document.getElementById('" + img.getAttribute("id") + "').height;");
				        	
					    	
					    	if(jheight!=null){
					    		jheight.getNumber();
					    		arff.addfeatures("height", jheight.getNumber());
					    	}
					    	else{
				        	arff.setMissingValueHeight();
					    	}
				        }

				    	
				    	String width = img.getAttribute("width");
				    	if(!width.isEmpty()){
				    	Pattern p = Pattern.compile("-?\\d+");
				    	Matcher m = p.matcher(width);
				    	while (m.find()) {
					        arff.addfeatures("width", Double.parseDouble(m.group()));
				    		}
	                    }else{
				        	img.setAttribute("id", "noH&W"+img.hashCode());
					    	//JSValue jwidth = browser.executeJavaScriptAndReturnValue("$('#noH&W"+img.hashCode()+"').weight()");
				            JSValue jwidth = browser.executeJavaScriptAndReturnValue(
				                   "document.getElementById('" + img.getAttribute("id") + "').width;"
				                  );
					    	
					    	if(jwidth!=null){
					    		jwidth.getNumber();
					    		arff.addfeatures("width", jwidth.getNumber());
					    	}
					    	else{
				        	arff.setMissingValueWidth();
					    	}
	                    }
				    	
						 for(int i =0; i<arff.strval.length;i++)
						 {
							 if(img.getParent()!=null){
							   pNode = img.getParent();
							   pEle =(DOMElement) pNode;
							 }
							 if(pEle!=null){
							 if(img.getAttribute("alt").contains(arff.strval[i])||img.getAttribute("src").contains(arff.strval[i])||img.getAttribute("title").contains(arff.strval[i])||pEle.getAttribute("href").contains(arff.strval[i])||pEle.getAttribute("id").contains(arff.strval[i])||pEle.getAttribute("class").contains(arff.strval[i]))
							 {
								arff.addfeatures(arff.strval[i],arff.strval[i]); 
							 }
							 else{
								 arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]); 
							 }
							 }
							 else{
								 if(img.getAttribute("alt").contains(arff.strval[i])||img.getAttribute("src").contains(arff.strval[i])||img.getAttribute("title").contains(arff.strval[i]))
								 {
									arff.addfeatures(arff.strval[i],arff.strval[i]); 
								 }
								 else{
									 arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]); 
								 }
							 }
								 
							 }

	                     arff.addfeatures("frameOrimage", "image");
						 
	                     if(img.getAttribute("alt").contains("advertisment")||pEle.getAttribute("href").contains("redirect")){
							 arff.addfeatures("targetvalue","AD"); 
							 //arff.getInstance().setClassValue(0);
							 }else{
							 arff.addfeatures("targetvalue","NonAD"); 
							 //arff.getInstance().setClassValue(1);
							 }
	                     
						   arff.AddtoData();
						   arff.deleteInstance();
						   }
					
		        	for (Long framesId : framesIds) {		        	  
			        			DOMDocument frameDOM = browser.getDocument(framesId);
			                    List<DOMElement> innerframes = frameDOM.findElements(By.tagName("iframe"));
						    for (DOMElement iframe : innerframes) {				    
						    	arff.createInsatnce();
						    	String height = iframe.getAttribute("height");
						    	if(!height.isEmpty()){
							    	if(height.contains("%")){
							    		arff.setMissingValueHeight();
							    		}
							    	else{
								    	Pattern p = Pattern.compile("-?\\d+");
								    	Matcher m = p.matcher(height);
								    	while (m.find()) {
									        arff.addfeatures("height", Double.parseDouble(m.group()));
									        }
								    	}
							    	}
						    	else{arff.setMissingValueHeight();}
						    	String width = iframe.getAttribute("width");
						    	if(!width.isEmpty()){
						    	 if(width.contains("%")){
						    		arff.setMissingValueWidth();	
						    		}
						    	 else{
						    		 Pattern p = Pattern.compile("-?\\d+");
								    	Matcher m = p.matcher(width);
								    	while (m.find()) {
									        arff.addfeatures("width", Double.parseDouble(m.group()));
								    		}
						    		 }
						    	 }
						    	else
						    	{
						    	arff.setMissingValueWidth();
						    	}
	                           for(int i =0; i<arff.strval.length;i++)
								 {
									 arff.SetLabel();
									 if(iframe.getAttribute("onload").contains(arff.strval[i])||iframe.getAttribute("id").contains(arff.strval[i])||iframe.getAttribute("src").contains(arff.strval[i])||iframe.getAttribute("class").contains(arff.strval[i])||iframe.getAttribute("name").contains(arff.strval[i])){
										arff.addfeatures(arff.strval[i],arff.strval[i]); 
									 }
									 else {  
										arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]);
									 }

								 }
	      					 if(iframe.getAttribute("name").contains("Advertisment")||iframe.getAttribute("id").contains("frame")||iframe.getAttribute("src").contains("google")){
	    						 arff.addfeatures("targetvalue","AD"); 
	    						 //arff.getInstance().setClassValue(0);
	    						 }
	      					 else{
	    						 arff.addfeatures("targetvalue","NonAD"); 
	    						 //arff.getInstance().setClassValue(1);
	    						 }
								   arff.addfeatures("frameOrimage", "iframe");
								   arff.AddtoData();
		                           arff.deleteInstance();
		                        }
			        	  }
					
					} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					} 
	        	ArffSaver saver = new ArffSaver();
	        	Instances OutputInst = null;
	        	if(inst1!=null){
	        		System.out.println(arff.getInstances().toString());
	        		System.out.println(inst1);
	        		//String 
	        		//OutputInst =  Instances.main("append","<filename1>"," <filename2>");
	        		inst1.addAll(arff.getInstances());
	        		OutputInst=inst1;      		
	        		}
	        	else{
	        		OutputInst = arff.getInstances();
	        		}
	        	
	   		    saver.setInstances(OutputInst);
	   		    try {
					saver.setFile(new File("./data/Model.arff"));
					saver.writeBatch();

	        		 WebNotification popup = new WebNotification ( NotificationStyle.mac );
	                 popup.setContent ( "Successfully Updated/Generated" ); 
	                 popup.setDisplayTime ( 3000 );
	                 NotificationManager.showNotification (popup);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	arff = null;
	        	
	        	}
        	
        };
        
        ImageIcon GoIcon = new ImageIcon((new ImageIcon("./icons/next-page.png")).getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH));
        Image BrowserIcon = (new ImageIcon("./icons/browser.png")).getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        ImageIcon appsIcon = new ImageIcon((new ImageIcon("./icons/apps.png")).getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH));
        ImageIcon noteIcon = new ImageIcon((new ImageIcon("./icons/note.png")).getImage().getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH));
        WebMenuBar menuBar = new WebMenuBar ();
        WebMenu menu1 = new WebMenu("Classifier",noteIcon) ;

        WebMenu menu2 = new WebMenu("Functionailties",appsIcon);
        WebMenuItem menu2Item1 = new WebMenuItem("Raw Data Extraction");
        WebMenuItem menu2Item2 = new WebMenuItem("ARFF Update/Generate");
        WebMenuItem menu2Item3 = new WebMenuItem("Classify Advertisement");
        
        menu2Item1.addActionListener(RawDataActionListener);
        menu2Item2.addActionListener(ARFFActionListener);
        menu2Item3.addActionListener(ClassifActionListener);
        
        menu2.add(menu2Item1);
        menu2.add(menu2Item2);
        menu2.add(menu2Item3);
        WebLabel currentcfer = new WebLabel("	Current classifier: "+ classifier.toString());
        
        WebMenuItem menu1Item1 = new WebMenuItem("ADTree");
        menu1Item1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			classifier = Classifier.ADtree;
			currentcfer.setText("	Current Classifier: " + classifier);
			}
        	
        });

        WebMenuItem menu1Item2 = new WebMenuItem("Naive Bayes");
        menu1Item2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			classifier = Classifier.NaiveBayes;
			currentcfer.setText("	Current Classifier: " + classifier);
			}
        	
        });        
        WebMenuItem menu1Item3 = new WebMenuItem("Random Forest");
        menu1Item3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			classifier = Classifier.RandomForest;
			currentcfer.setText("	Current Classifier: " + classifier);
			}
        	
        });
        WebMenuItem menu1Item4 = new WebMenuItem("J48");
        menu1Item4.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			classifier = Classifier.J48;
			currentcfer.setText("	Current Classifier: " + classifier);
			}
        	
        });
        WebMenuItem menu1Item5 = new WebMenuItem("Sequential Minimal Optimization (Support Vector Machine)");
        menu1Item5.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
			classifier = Classifier.SMO;
			currentcfer.setText("	Current Classifier: " + classifier);
			}
        	
        });
        currentcfer.setAlignmentX(menu1Item1.getAlignmentX());
        
       
        menu1.add(currentcfer);
        menu1.add(menu1Item1);
        menu1.add(menu1Item2);
        menu1.add(menu1Item3);
        menu1.add(menu1Item4);
        menu1.add(menu1Item5);

        
        
        menuBar.add(menu1);
        menuBar.add(menu2);
        //setJMenuBar ( menuBar );
        
        JFrame frame = new JFrame("FYP Demo");
        //frame.setTitle("FYP Demo");
        frame.setIconImage(BrowserIcon);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel paneltop = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        frame.setJMenuBar ( menuBar );
        JTextField URLtextfield = new JTextField("http://www.google.com");
        c.weightx = 0.9;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        paneltop.add(URLtextfield,c);
        
        JButton URLButton = new JButton(GoIcon);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.025;
        c.gridx = 1;
        c.gridy = 0;
        paneltop.add(URLButton,c);
        
        JButton URLButton2 = new JButton("RawData");

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.02;
        c.gridx = 2;
        c.gridy = 0;
        //paneltop.add(URLButton2,c);
        
        JButton URLButton3 = new JButton("Classify");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.02;
        c.gridx = 3;
        c.gridy = 0;
        //paneltop.add(URLButton3,c);
        
        JButton URLButton4 = new JButton("ARFF");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.02;
        c.gridx = 4;
        c.gridy = 0;
        //paneltop.add(URLButton4,c);
        
       
        
       BrowserView browserView = new BrowserView(browser);
       //frame.getContentPane().setComponentZOrder(browserView,frame.getComponentCount() );


        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.gridwidth = 5;
        c.gridx = 0;
        c.gridy = 1;
        paneltop.add(browserView,c);
        
        URLtextfield.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
          	  browser.loadURL(URLtextfield.getText());
          	  URLtextfield.setText(browser.getURL());
            }
        });
        
        URLButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
        	  browser.loadURL(URLtextfield.getText());
        	  URLtextfield.setText(browser.getURL());
          }
        });
        
        URLButton2.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
              File file =new File("./data/rawdata.txt");
        	    if(!file.exists()){
        	 	try {
					file.createNewFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	    }
        	    FileWriter fw;
				try {
					fw = new FileWriter(file,true);
		      	     BufferedWriter bw = new BufferedWriter(fw);
		      	     PrintWriter fileout1 = new PrintWriter(bw);
		      	     
		      	   if(isfinishedloading){
		        		  for(DOMElement img : imgs){

		        			 img.setAttribute("id", "noH&W"+img.hashCode());
					         JSValue jwidth = browser.executeJavaScriptAndReturnValue(
					                    
					                  "document.getElementById('" + img.getAttribute("id") + "').width;"
					                  );
					         JSValue jheight = browser.executeJavaScriptAndReturnValue(
				
					                  "document.getElementById('" + img.getAttribute("id") + "').height;"
					                  );
		        			  fileout1.print("\r\n"+img.getAttribute("alt")+
		        					  ","+img.getAttribute("src")+
		        					  ","+img.getAttribute("width")+
		        					  ","+img.getAttribute("height")+
		        					  ","+img.getAttribute("style") +
		        					  ","+jwidth.getNumber()+
		        					  ","+jheight.getNumber()
		    						  );
		        			  
		        			  if(img.getParent()!=null){
		        				 DOMNode pNode= img.getParent();
		        				 DOMElement pEle = (DOMElement) pNode;
		        			   if(pNode.getNodeName().equalsIgnoreCase("a")){
		        						  fileout1.print(","+pEle.getAttribute("href")+
		        						  ","+pEle.getAttribute("id")+
		        						  ","+pEle.getAttribute("class")  
		        						  );
		        						  } 
		        			   }
		        			  }
		        		  fileout1.print("\r\n");

		        		  for (Long framesId : framesIds) {
		        			  if(browser.getDocument(framesId)!=null){
		        			   DOMDocument frameDOM = browser.getDocument(framesId);
		                       List<DOMElement> innerframes = frameDOM.findElements(By.tagName("iframe"));
		                       for(DOMElement iframe:innerframes ){
		                    	  if(!iframe.getAttribute("src").isEmpty()||!iframe.getAttribute("height").isEmpty()||!iframe.getAttribute("width").isEmpty()){
		                    		  fileout1.print(iframe.getAttribute("height")+
		            						  ","+iframe.getAttribute("width")+
		            						  ","+iframe.getAttribute("id")+
		            						  ","+iframe.getAttribute("name")+
		            						  ","+iframe.getAttribute("src")
		            						  ); 
			                    	  fileout1.print("\r\n");
			                    	  }
		                    	  }
		                       }
		        			  }
		        		  fileout1.close();	
		        		  }
		      	         
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
          }
        });
        
        URLButton3.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e)
            {   
        	   DataSource source;
			   DOMNode pNode = null;
			   DOMElement pEle = null;
			   int instNo =0;
        		try {
					arff = new ARFF();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                arff.SetLabel();
                arff.SetRelationName();

        		if(isfinishedloading == true){
            		try {
						source = new DataSource("./data/Model.arff");
						dataSet = source.getDataSet();
				    	dataSet.setClassIndex(dataSet.numAttributes()-1);					
				        ASclassifier = new AttributeSelectedClassifier();
				        PrincipalComponents pca = new PrincipalComponents();
				        Ranker ranker = new Ranker();
				        //NaiveBayes NB = new NaiveBayes();
				        ADTree adtree = new ADTree();
				        ASclassifier.setClassifier(adtree);
				        ASclassifier.setEvaluator(pca);
				        ASclassifier.setSearch(ranker);
				        ASclassifier.buildClassifier(dataSet);
				        //adtree.getNumOfBoostingIterations();
				    	
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            		
        			for (DOMElement img : imgs) {
        				instNo ++;
				    	arff.createInsatnce();
				    	String height = img.getAttribute("height");
				    	if(!height.isEmpty())
				    	{				    	
				    		Pattern p = Pattern.compile("-?\\d+");
				    	Matcher m = p.matcher(height);
				    	while (m.find()) {
					        arff.addfeatures("height", Double.parseDouble(m.group()));
				    		}
				        }else{
				        	img.setAttribute("id", "noH&W"+img.hashCode());
				        	 JSValue jheight = browser.executeJavaScriptAndReturnValue("document.getElementById('" + img.getAttribute("id") + "').height;");
	
						    	if(jheight!=null){
						    		jheight.getNumber();
						    		arff.addfeatures("height", jheight.getNumber());
						    	}
						    	else{
					        	arff.setMissingValueHeight();
						    	}
				        }
				    	String width = img.getAttribute("width");
				    	if(!width.isEmpty()){
				    	Pattern p = Pattern.compile("-?\\d+");
				    	Matcher m = p.matcher(width);
				    	while (m.find()) {
					        arff.addfeatures("width", Double.parseDouble(m.group()));
				    		}
                        }else{
    			        	img.setAttribute("id", "noH&W"+img.hashCode());
    			            JSValue jwidth = browser.executeJavaScriptAndReturnValue(
    				                   "document.getElementById('" + img.getAttribute("id") + "').width;"
    				                  );
    					    	
    					    	if(jwidth!=null){
    					    		jwidth.getNumber();
    					    		arff.addfeatures("width", jwidth.getNumber());
    					    	}
    					    	else{
    				        	arff.setMissingValueWidth();
    					    	}
                        }
					 for(int i =0; i<arff.strval.length;i++){
						 if(img.getParent()!=null){
							   pNode = img.getParent();
							   pEle =(DOMElement) pNode;
							 }
						 if(pEle!=null){
						 if(img.getAttribute("alt").contains(arff.strval[i])||img.getAttribute("src").contains(arff.strval[i])||img.getAttribute("title").contains(arff.strval[i])||pEle.getAttribute("href").contains(arff.strval[i])||pEle.getAttribute("id").contains(arff.strval[i])||pEle.getAttribute("class").contains(arff.strval[i]))
						 {
							arff.addfeatures(arff.strval[i],arff.strval[i]); 
						 }
						 else{
							 arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]); 
						 }
						 }
						 else{
							 if(img.getAttribute("alt").contains(arff.strval[i])||img.getAttribute("src").contains(arff.strval[i])||img.getAttribute("title").contains(arff.strval[i]))
							 {
								arff.addfeatures(arff.strval[i],arff.strval[i]); 
							 }
							 else{
								 arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]); 
							 }
						 }

						 }
					   arff.addfeatures("frameOrimage", "image");
					   arff.AddtoData();
				         try {
								//Classifier cls_co = (Classifier) weka.core.SerializationHelper.read("D:\\FYP\\FYP_one\\NaiveBayesOriginal.model");
								arff.getInstances().firstInstance();
								arff.SetLabel();
								//fileout.println(arff.getInstances().toString());
								result = ASclassifier.classifyInstance(arff.getInstances().firstInstance());
							} catch (Exception e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
					   if(arff.getInstances().classAttribute().value((int) result) == "AD"){
						  img.setAttribute("style", "visibility:hidden");
						  //fileout.println(arff.getInstances().firstInstance().toString());
						  //fileout.println(arff.getInstances().classAttribute().value((int) result));
					   }
					   arff.delteInstances();
				    }
        			
    	        	for (Long framesId : framesIds) {
    		        	
    		        			DOMDocument frameDOM = browser.getDocument(framesId);
    		                    List<DOMElement> innerframes = frameDOM.findElements(By.tagName("iframe"));
    					    for (DOMElement iframe : innerframes) {	
    					    	instNo++;
    					    	arff.createInsatnce();
    					        arff.SetLabel();
    					    	String height = iframe.getAttribute("height");
    					    	if(!height.isEmpty()){
    						    	if(height.contains("%")){
    						    		arff.setMissingValueHeight();
    						    	}
    						    	else{
    							    	Pattern p = Pattern.compile("-?\\d+");
    							    	Matcher m = p.matcher(height);
    							    	while (m.find()) {
    								        arff.addfeatures("height", Double.parseDouble(m.group()));
    							    		}
    						    	}
    		
    					        }
    					    	else{
    					    		arff.setMissingValueHeight();
    					    	}
    					    	String width = iframe.getAttribute("width");
    					    	if(!width.isEmpty()){
    					    	   if(width.contains("%")){
    					    		arff.setMissingValueWidth();	
    					    		}
    					    	   else{
    					    		 Pattern p = Pattern.compile("-?\\d+");
    							    	Matcher m = p.matcher(width);
    							    	while (m.find()) {
    								        arff.addfeatures("width", Double.parseDouble(m.group()));
    								        }
    							    	}
    					    	   }
    					    	else{
    					    	arff.setMissingValueWidth();
    					    	}
                                 for(int i =0; i<arff.strval.length;i++) {
    								 arff.SetLabel();
    								 if(iframe.getAttribute("onload").contains(arff.strval[i])||iframe.getAttribute("id").contains(arff.strval[i])||iframe.getAttribute("src").contains(arff.strval[i])||iframe.getAttribute("class").contains(arff.strval[i])||iframe.getAttribute("name").contains(arff.strval[i])){
    									arff.addfeatures(arff.strval[i],arff.strval[i]); 
    								 }
    								 else {  
    									arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]);
    								 }

    							 }
    							   arff.addfeatures("frameOrimage", "iframe");
    							   arff.AddtoData();
    						         try {
    										//Classifier cls_co = (Classifier) weka.core.SerializationHelper.read("D:\\FYP\\FYP_one\\NaiveBayesOriginal.model");
    										result = ASclassifier.classifyInstance(arff.getInstances().firstInstance());
    										} 
    						         catch (Exception e3) {
    										// TODO Auto-generated catch block
    										e3.printStackTrace();
    										}
    							   if(arff.getInstances().classAttribute().value((int) result) == "AD"){
    								  iframe.setAttribute("style", "visibility:hidden");
    							   }
    							   
    	                        arff.delteInstances();
    	                        }
    					    
    		        	  }
    	        	URLButton3.setText("instances no: "+instNo);
    	        	}
        		}  
        	});

        URLButton4.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
			   DOMNode pNode = null;
			   DOMElement pEle = null;
        	  DataSource source;
        	  Instances inst1;
			try {
				source = new DataSource("./data/Model.arff");
	        	inst1 = source.getDataSet();
	       
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				source=null;
				inst1=null;
			}
        	  
        	try {
				arff = new ARFF();
				arff.SetLabel();
				arff.SetRelationName();
				for(DOMElement img:imgs){
					
					arff.createInsatnce();
			    	String height = img.getAttribute("height");
			    	if(!height.isEmpty())
			    	{				    	
			    		Pattern p = Pattern.compile("-?\\d+");
			    	Matcher m = p.matcher(height);
			    	while (m.find()) {
				        arff.addfeatures("height", Double.parseDouble(m.group()));
			    		}
			        }else{
			        	img.setAttribute("id", "noH&W"+img.hashCode());
			        	System.out.println(img.getAttribute("id"));
				    	//JSValue jheight = browser.executeJavaScriptAndReturnValue("$('#noH&W"+img.hashCode()+"').height()");
			            JSValue jheight = browser.executeJavaScriptAndReturnValue("document.getElementById('" + img.getAttribute("id") + "').height;");
			        	
				    	
				    	if(jheight!=null){
				    		jheight.getNumber();
				    		arff.addfeatures("height", jheight.getNumber());
				    	}
				    	else{
			        	arff.setMissingValueHeight();
				    	}
			        }

			    	
			    	String width = img.getAttribute("width");
			    	if(!width.isEmpty()){
			    	Pattern p = Pattern.compile("-?\\d+");
			    	Matcher m = p.matcher(width);
			    	while (m.find()) {
				        arff.addfeatures("width", Double.parseDouble(m.group()));
			    		}
                    }else{
			        	img.setAttribute("id", "noH&W"+img.hashCode());
				    	//JSValue jwidth = browser.executeJavaScriptAndReturnValue("$('#noH&W"+img.hashCode()+"').weight()");
			            JSValue jwidth = browser.executeJavaScriptAndReturnValue(
			                   "document.getElementById('" + img.getAttribute("id") + "').width;"
			                  );
				    	
				    	if(jwidth!=null){
				    		jwidth.getNumber();
				    		arff.addfeatures("width", jwidth.getNumber());
				    	}
				    	else{
			        	arff.setMissingValueWidth();
				    	}
                    }
			    	
					 for(int i =0; i<arff.strval.length;i++)
					 {
						 if(img.getParent()!=null){
						   pNode = img.getParent();
						   pEle =(DOMElement) pNode;
						 }
						 if(pEle!=null){
						 if(img.getAttribute("alt").contains(arff.strval[i])||img.getAttribute("src").contains(arff.strval[i])||img.getAttribute("title").contains(arff.strval[i])||pEle.getAttribute("href").contains(arff.strval[i])||pEle.getAttribute("id").contains(arff.strval[i])||pEle.getAttribute("class").contains(arff.strval[i]))
						 {
							arff.addfeatures(arff.strval[i],arff.strval[i]); 
						 }
						 else{
							 arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]); 
						 }
						 }
						 else{
							 if(img.getAttribute("alt").contains(arff.strval[i])||img.getAttribute("src").contains(arff.strval[i])||img.getAttribute("title").contains(arff.strval[i]))
							 {
								arff.addfeatures(arff.strval[i],arff.strval[i]); 
							 }
							 else{
								 arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]); 
							 }
						 }
							 
						 }

                     arff.addfeatures("frameOrimage", "image");
					 
                     if(img.getAttribute("alt").contains("advertisment")){
						 arff.addfeatures("targetvalue","AD"); 
						 //arff.getInstance().setClassValue(0);
						 }else{
						 arff.addfeatures("targetvalue","NonAD"); 
						 //arff.getInstance().setClassValue(1);
						 }
                     
					   arff.AddtoData();
					   arff.deleteInstance();
					   }
				
	        	for (Long framesId : framesIds) {		        	  
		        			DOMDocument frameDOM = browser.getDocument(framesId);
		                    List<DOMElement> innerframes = frameDOM.findElements(By.tagName("iframe"));
					    for (DOMElement iframe : innerframes) {				    
					    	arff.createInsatnce();
					    	String height = iframe.getAttribute("height");
					    	if(!height.isEmpty()){
						    	if(height.contains("%")){
						    		arff.setMissingValueHeight();
						    		}
						    	else{
							    	Pattern p = Pattern.compile("-?\\d+");
							    	Matcher m = p.matcher(height);
							    	while (m.find()) {
								        arff.addfeatures("height", Double.parseDouble(m.group()));
								        }
							    	}
						    	}
					    	else{arff.setMissingValueHeight();}
					    	String width = iframe.getAttribute("width");
					    	if(!width.isEmpty()){
					    	 if(width.contains("%")){
					    		arff.setMissingValueWidth();	
					    		}
					    	 else{
					    		 Pattern p = Pattern.compile("-?\\d+");
							    	Matcher m = p.matcher(width);
							    	while (m.find()) {
								        arff.addfeatures("width", Double.parseDouble(m.group()));
							    		}
					    		 }
					    	 }
					    	else
					    	{
					    	arff.setMissingValueWidth();
					    	}
                           for(int i =0; i<arff.strval.length;i++)
							 {
								 arff.SetLabel();
								 if(iframe.getAttribute("onload").contains(arff.strval[i])||iframe.getAttribute("id").contains(arff.strval[i])||iframe.getAttribute("src").contains(arff.strval[i])||iframe.getAttribute("class").contains(arff.strval[i])||iframe.getAttribute("name").contains(arff.strval[i])){
									arff.addfeatures(arff.strval[i],arff.strval[i]); 
								 }
								 else {  
									arff.addfeatures(arff.strval[i],"Non"+arff.strval[i]);
								 }

							 }
      					 if(iframe.getAttribute("name").contains("Advertisment")||iframe.getAttribute("id").contains("frame")||iframe.getAttribute("src").contains("google")){
    						 arff.addfeatures("targetvalue","AD"); 
    						 //arff.getInstance().setClassValue(0);
    						 }
      					 else{
    						 arff.addfeatures("targetvalue","NonAD"); 
    						 //arff.getInstance().setClassValue(1);
    						 }
							   arff.addfeatures("frameOrimage", "iframe");
							   arff.AddtoData();
	                           arff.deleteInstance();
	                        }
		        	  }
				
				} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				} 
        	ArffSaver saver = new ArffSaver();
        	Instances OutputInst = null;
        	if(inst1!=null){
        		System.out.println(arff.getInstances().toString());
        		System.out.println(inst1);
        		//String 
        		//OutputInst =  Instances.main("append","<filename1>"," <filename2>");
        		inst1.addAll(arff.getInstances());
        		OutputInst=inst1;
        		}
        	else{
        		OutputInst = arff.getInstances();
        		}
   		 
   		    saver.setInstances(OutputInst);
   		    try {
				saver.setFile(new File("./data/Model.arff"));
				saver.writeBatch();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	arff = null;
        	
        	}
          
        });
        
        frame.setContentPane(paneltop);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        browser.setContextMenuHandler(new MyContextMenuHandler(browserView));

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        
        browser.addLoadListener(new LoadAdapter() {
        	
        	@Override
        	public void onStartLoadingFrame(StartLoadingEvent event){
        		if (event.isMainFrame()){
        			isfinishedloading =false;
        		}
        	}
                   	
        	@Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
				if (event.isMainFrame()) {
					isfinishedloading =true;
				    document = event.getBrowser().getDocument();
				    imgs = document.findElements(By.cssSelector("a>img"));
				    framesIds = browser.getFramesIds();
		        	URLtextfield.setText(browser.getURL());

				    }
       }
        });
        browser.loadURL("www.google.com");
    }

    private static class MyContextMenuHandler implements ContextMenuHandler {
		 
        private final JComponent component;
 
        private MyContextMenuHandler(JComponent parentComponent) {
            this.component = parentComponent;
        }
 
        public void showContextMenu(final ContextMenuParams params) {
        	String linkURLFrame = params.getFrameURL();
            String SrcURL = params.getSrcURL();
            String linkURL = params.getSrcURL();
            String href = params.getLinkURL();
            
            final JPopupMenu popupMenu = new JPopupMenu();
//            if (!params.getLinkText().isEmpty()) {
//                popupMenu.add(createMenuItem("Open link in new window", new Runnable() {
//                    public void run() {
//                        String linkURL = params.getLinkURL();
//                        System.out.println("linkURL = " + linkURL);
//                    }
//                }));
//            }
 
            final Browser browser = params.getBrowser();
            popupMenu.add(createMenuItem("Reload", new Runnable() {
                public void run() {
                    browser.reload();
                }
            }));
 
            final Point location = params.getLocation();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    popupMenu.show(component, location.x, location.y);
                }
            });
            
            if(!linkURLFrame.isEmpty()||!SrcURL.isEmpty()||!href.isEmpty()){
            	popupMenu.add(createMenuItem("SettoAdvertisment", new Runnable() {
                    public void run() {
//                        String linkURLFrame = params.getFrameURL();
//                        String SrcURL = params.getSrcURL();
//                        String linkURL = params.getSrcURL();
//                        String href = params.getLinkURL();
                        boolean isAD = false;
                       Set<Long> framesIds = browser.getFramesIds();    
        	        	for (Long framesId : framesIds) {
        	        			DOMDocument frameDOM = browser.getDocument(framesId);
        	                    List<DOMElement> innerframes = frameDOM.findElements(By.tagName("iframe"));
        	                    for(DOMElement iframe:innerframes ){
        	                    	String tempsrc= iframe.getAttribute("src");
        	                    	String tempname = iframe.getAttribute("name");
        	                    	if(tempsrc.equalsIgnoreCase(linkURLFrame)||tempsrc.equalsIgnoreCase(SrcURL)){
       	                    		if(!tempname.contains("Advertisment")){
        	                    		iframe.setAttribute("name",tempname+"Advertisment");
        	                    		System.out.println("success");
        	                    		isAD = true;
        	                    		break;
        	                    		}
       	                    		
       	                    		else{
       	                    			    System.out.println(tempsrc);
        	                    			System.out.println(tempname);
        	                    		isAD = true;
        	                    		break;
        	                    		}
        	                    	}
       	                    		System.out.println(framesId);
            	                    //if(framesId.equals(Browser.MAIN_FRAME_ID)){
        	                    		//iframe.getParent().getChildren()
            	                   		 List<DOMNode> Nodes = iframe.getParent().getChildren();
                                		 for(DOMNode node:Nodes){
                                			 if(node.getNodeName().equalsIgnoreCase("a")){
                                				DOMElement tempele = (DOMElement) node ;
                                				if(tempele.getAttribute("href").equalsIgnoreCase(href)){
                    	                    		iframe.setAttribute("name",tempname+"Advertisment");
                    	                    		System.out.println("success");
                    	                    		isAD = true;
                    	                    		break;	
                                				}
                                			 }
                                		 }
            	                    //}

  		                    	  }

        	                    
        	                    
        	                    
        	                    }
        	        	
                        for(DOMElement img:imgs){
                     	   
                     	   if(linkURL.contains(img.getAttribute("src"))){
                     		   
                         	   if(!img.getAttribute("alt").contains("advertisment")){
                                String original = img.getAttribute("alt");
                                img.setAttribute("alt",original +"advertisment" );
                                System.out.println(img.getAttribute("src"));
                                isAD = true;
                                break;
                         	   }
                         	   else{
                         		   System.out.println("Added");	
                         		   System.out.println(img.getAttribute("src"));
                         		   isAD = true;
                         		break;   
                         	   }
                         	   
                     	   }
                     	   if(img.getParent()!=null){
                     	   if(img.getParent().getNodeName().equalsIgnoreCase("a")&&!href.isEmpty()){
                     		   
                     		   DOMElement pEle = (DOMElement)img.getParent(); 
                     		   if(pEle.getAttribute("href").equalsIgnoreCase(href)){
                                    String original = img.getAttribute("alt");
                                    img.setAttribute("alt",original +"advertisment" ); 
                                    System.out.println("success");
                                    isAD = true;
                                    break;
                     		   }else{
                     			   System.out.println("href is wrong");
                     		   }

             	            }else{

             	            	System.out.println(img.getAttribute("href is empty~"));
             	            }
                    		 List<DOMNode> Nodes = img.getParent().getChildren();
                    		 for(DOMNode node:Nodes){
                    			 if(node.getNodeName().equalsIgnoreCase("a")){
                    				DOMElement tempele = (DOMElement) node ;
                    				if(tempele.getAttribute("href").equalsIgnoreCase(href)){
                                       String original = img.getAttribute("alt");
                                       img.setAttribute("alt",original +"advertisment" ); 
                                       System.out.println("success");
                                       isAD = true;
                                       break;	
                    				}
                    			 }
                    		 }
                     	   System.out.println(img.getAttribute("src"));
                     	   }
                  	   
                        }
        	        	
                        if(!isAD){
                     	   System.out.println("this is possibly a relative link which indicates not Advertisment");
                     	  System.out.println(linkURL + " , " + linkURLFrame + " , "+ href);
                     	   }
                        else{
                           System.out.println("Successfully tag it as Advertisment");	
                     	   }
                        
                        WebNotification popup = new WebNotification ( NotificationStyle.mac );
                        if(isAD){
                        	popup.setContent ( "Successfully tagged" ); 
                        }else
                        {
                        	popup.setContent("Failed to tag");
                        }
                        popup.setDisplayTime ( 3000 );
                        NotificationManager.showNotification (popup);
                    }
                }));
            }           
            
        }
   
        
 
        private static JMenuItem createMenuItem(String title, final Runnable action) {
            JMenuItem reloadMenuItem = new JMenuItem(title);
            reloadMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    action.run();
                }
            });
            return reloadMenuItem;
        }
    }
    

}
