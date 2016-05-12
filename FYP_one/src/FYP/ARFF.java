package FYP;


import java.util.ArrayList;
import java.util.List;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;


 public class ARFF {
     private Instances       data;
     private Instance        inst;
     private ArrayList<Attribute> atts;
     //public  String[] strval= {"iframe", "google","ad","ads","aswift","insert","doubleclick","content","uploads","cdn","news","image"};
     public  String[] strval= {"iframe", "google","embed","ads","aswift","autolayout","doubleclick","redirect","banner","content","upload","news","image","staticlayout","icon","photo","home"};
     public  ArrayList<String> IsAD;
     public  ArrayList<String> FrameOrImage;

	 @SuppressWarnings("unchecked")
	public ARFF() throws Exception {
		 final int size =strval.length;
		 List<List<String>> stringlist = new ArrayList<List<String>>(size);
		 atts = new ArrayList<Attribute>();
		 IsAD = new ArrayList<String>();
		 FrameOrImage = new ArrayList<String>();
		 IsAD.add("AD");IsAD.add("NonAD");
		 FrameOrImage.add("iframe");
		 FrameOrImage.add("image");
		 atts.add(new Attribute("width"));
		 atts.add(new Attribute("height"));
		 for(int i =0;i< strval.length;i++) 
		 {
		stringlist.add(new ArrayList<String>());
		stringlist.get(i).add(strval[i]);
		stringlist.get(i).add("Non"+strval[i]);
		 atts.add(new Attribute(strval[i],stringlist.get(i)) );
		 }
		 atts.add(new Attribute("frameOrimage",FrameOrImage));
		 atts.add(new Attribute("targetvalue",IsAD)); 
		 data = new Instances("Advertisment", atts, 999);
	 }

	 
	 public void createInsatnce(){
		 inst = new DenseInstance(data.numAttributes()); 	 
	 } 

	 public void addfeatures(String feature){
      for(Attribute e : atts){
    	  if(e.name()==feature)
    		  inst.setValue(e, feature);
      }
	 }
	 public void addfeatures(String feature,String decision){
	      for(Attribute e : atts){
	    	  if(e.name()==feature)
	    		  inst.setValue(e, decision );
	      }
		 }
	 public void addfeatures(String feature,double dimension){
	      for(Attribute e : atts){
	    	  if(e.name()==feature)
	    		  inst.setValue(e, dimension );
	      }
		 }
	 public void AddtoData(){
		 data.add(inst);
	 }
	 public void setMissingValueHeight(){
		 inst.setMissing(0);
	 }
	 public void setMissingValueWidth(){
		 inst.setMissing(1);
	 }
	 public void SetLabel(){
		 data.setClassIndex(data.numAttributes() - 1);
		 
	 }
	 public void SetRelationName(){
		 data.setRelationName("Advertisment");
		 
	 }
	 public void delteInstances(){
		 inst =null;
		 data.clear();
	 }
	 public void deleteInstance(){
		 inst =null;
	 }
	 public Instances getInstances(){
		 return data;
	 }
	 public Instance getInstance(){
		 return inst;
	 }
	 public ArrayList<Attribute> getAttributes(){
		 return atts;
	 }

}
