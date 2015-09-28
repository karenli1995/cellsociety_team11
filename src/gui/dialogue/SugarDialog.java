package gui.dialogue;

public class SugarDialog extends Dialog {
	public SugarDialog() {
		super("Sugarscape");
	}

	@Override
	protected void addTexts() {
		super.addTexts();

		addTextField("vision", "Agent Vision:", "5", "Agent vision", "Positive Integer");
		addTextField("metabolism", "Metabolism:", "5", "Metabolism", "Positive Integer");
		addTextField("sugarGrowBackRate", "Sugar Grow Back Rate:", "1", "Sugar Grow Back Rate", "Positive Integer");
		addTextField("sugarGrowBackInterval", "Sugar Grow Back Interval:", "1", "Sugar Grow Back Interval",
				"Positive Integer");
		addTextField("maxSugar", "Max Possible Sugar on Grid:", "10", "Max Possible Sugar on Grid", "Positive Integer");
		addTextField("numAgent", "Number of Agents:", "10", "Number of Agents", "Cannot exceed grid size");
		addTextField("initialAgentSugar", "Agent Initial Sugar:", "10", "Agent Initial Sugar", "Positive Integer");
	};

	@Override
	protected boolean validate() {
		try {
			int width = Integer.parseInt(myTexts.get("width").getText().trim());
			int height = Integer.parseInt(myTexts.get("height").getText().trim());
			int vision = Integer.parseInt(myTexts.get("vision").getText().trim());
			int meta = Integer.parseInt(myTexts.get("metabolism").getText().trim());
			int growRate = Integer.parseInt(myTexts.get("sugarGrowBackRate").getText().trim());
			int growIntvl = Integer.parseInt(myTexts.get("sugarGrowBackInterval").getText().trim());
			int maxSugar = Integer.parseInt(myTexts.get("maxSugar").getText().trim());
			int numAgent = Integer.parseInt(myTexts.get("numAgent").getText().trim());
			int initSugar = Integer.parseInt(myTexts.get("initialAgentSugar").getText().trim());
			return width <= 100 && width > 0 && height <= 100 && height > 0 && vision > 0 && meta > 0 && growRate > 0
					&& growIntvl > 0 && maxSugar > 0 && initSugar > 0 && numAgent <= width * height;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	protected String getName() {
		return "SugarModel";
	}

}
