package gui.dialogue;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

public abstract class Dialog extends javafx.scene.control.Dialog<Map<String, String>> {
	private GridPane myGrid;
	protected Map<String,TextField> myTexts;
	protected int myNumEntries;
	
	public static Dialog getDialog(String modelName){
		switch (modelName) {
		case "GOLModel":
			return new GOLDialog();
		case "SegModel":	
			return new SegDialog();
		case "PredModel":
			return new PredDialog();
		case "FireModel":	
			return new FireDialog();
		case "SugarModel":	
			return new SugarDialog();
		case "AntModel":	
			return new AntDialog();
		default:
			return null;
		}
	}
	public Dialog(String title) {
		setTitle("New "+title);
		setHeaderText("Parameters for "+title+":");
		ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
		getDialogPane().getButtonTypes().addAll(ok, ButtonType.CANCEL);
		myTexts = new HashMap<>();

		myGrid = new GridPane();
		myGrid.setHgap(10);
		myGrid.setVgap(10);
		myGrid.setPadding(new Insets(20, 150, 10, 10));

		addTexts();
		
		Node okButton = getDialogPane().lookupButton(ok);
		okButton.setDisable(true);
		
		getDialogPane().setContent(myGrid);
		
		myTexts.forEach((k,v)->{
			v.textProperty().addListener((ob,oV,nV)->{
				okButton.setDisable(nV.trim().isEmpty() || !validate());
			});
		});

		Platform.runLater(()->myTexts.get("width").requestFocus());

		setResultConverter(dialogButton -> {
			if (dialogButton == ok) {
				Map<String,String> map = new HashMap<>();
				myTexts.forEach((k,v)->{map.put(k, v.getText());});
				map.put("name", getName());
				return map;
			}
			return null;
		});
	}
	
	protected void addTextField(String name,String label,String defaultValue,String prompt, String range){
		TextField tf = new TextField(defaultValue);
		tf.setPromptText(prompt);
		tf.setTooltip(new Tooltip(prompt+"\n"+range));
		myGrid.add(new Label(label), 0, myNumEntries);
		myGrid.add(tf, 1, myNumEntries);
		myTexts.put(name, tf);
		myNumEntries++;
	}
	
	protected void addTexts(){
		addTextField("width", "Width:", "10", "Number of Columns", "Integer between 1 and 100");
		addTextField("height", "Height:", "10", "Number of Rows", "Integer between 1 and 100");
	}
	
	protected abstract boolean validate();
	
	protected abstract String getName();

}

