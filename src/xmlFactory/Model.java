package xmlFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Model {
	
	private final int myWidth,myHeight;
	
	private Map<String,String> myModelMap;
	
	Model(String name, int width, int height){
		myModelMap = new HashMap<>();
		myWidth = width;
		myHeight = height;
		myModelMap.put("name", name);
		myModelMap.put("width", Integer.toString(width));
		myModelMap.put("height", Integer.toString(height));
		myModelMap.put("author", "CellSociety_Team11");
	}
	
	public void setAuthor(String name){
		addModelParameter("name",name);
	}
	
	public void addModelParameter(Object key, Object value){
		myModelMap.put(key.toString(), value.toString());
	}
	
	public Map<String,String> getModel(){
		return myModelMap;
	}
	
	public abstract List<Map<String,String>> getCells();

	public int getWidth() {
		return myWidth;
	}

	public int getHeight() {
		return myHeight;
	}
}
