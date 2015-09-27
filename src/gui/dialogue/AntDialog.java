package gui.dialogue;

public class AntDialog extends Dialog {
	public AntDialog() {
		super("AntModel");
	}

	@Override
	protected void addTexts() {
		super.addTexts();
		addTextField("percentAnts", "Percent of Ants:", "0.1", "Percent of Ants", "Double between 0 and 1");
		addTextField("evapRate", "Evaporation Rate:", "0.25", "Evaporation Rate", "Double between 0 and 1");
		addTextField("diffusionRate", "Diffusion Rate:", "0.25", "Diffusion Rate", "Double between 0 and 1");
	};

	@Override
	protected boolean validate() {
		try {
			int width = Integer.parseInt(myTexts.get("width").getText().trim());
			int height = Integer.parseInt(myTexts.get("height").getText().trim());
			double percentAnts = Double.parseDouble(myTexts.get("percentAnts").getText().trim());
			double evapRate = Double.parseDouble(myTexts.get("evapRate").getText().trim());
			double diffusionRate = Double.parseDouble(myTexts.get("diffusionRate").getText().trim());

			return width <= 100 && width > 0 && height <= 100 && height > 0 
					&& percentAnts>= 0 && percentAnts <= 1
					&& evapRate>=0 && evapRate <= 1
					&& diffusionRate <= 1 && diffusionRate >= 0;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	protected String getName() {
		return "AntModel";
	}

}
