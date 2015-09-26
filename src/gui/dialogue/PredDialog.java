package gui.dialogue;

public class PredDialog extends Dialog {
	public PredDialog() {
		super("Predator-prey Model");
	}

	@Override
	protected void addTexts() {
		super.addTexts();
		addTextField("percentFish", "% Fish:", "0.5", "Percentage of Fish", "Double between 0 and 1");
		addTextField("percentShark", "% Shark:", "0.5", "Percentage of Shark", "Double between 0 and 1");
		addTextField("fishEnergy", "Energy of Fish:", "2", "Energy of Fish", "Positive Integer");
		addTextField("maxSharkEnergy", "Max Energy of Shark:", "5", "Max Energy of Shark", "Positive Integer");
		addTextField("livesReproduce", "Reproduce after # Rounds:", "5", "Reproduce after n Rounds",
				"Enter n (Positive Integer)");
	};

	@Override
	protected boolean validate() {
		try {
			int width = Integer.parseInt(myTexts.get("width").getText().trim());
			int height = Integer.parseInt(myTexts.get("height").getText().trim());
			double pFish = Double.parseDouble(myTexts.get("percentFish").getText().trim());
			double pShark = Double.parseDouble(myTexts.get("percentShark").getText().trim());
			int fishEnergy = Integer.parseInt(myTexts.get("fishEnergy").getText().trim());
			int maxSharkEnergy = Integer.parseInt(myTexts.get("maxSharkEnergy").getText().trim());
			int livesReproduce = Integer.parseInt(myTexts.get("livesReproduce").getText().trim());
			return width <= 100 && width > 0 && height <= 100 && height > 0 && pFish >= 0 && pShark >= 0
					&& pFish + pShark <= 1 && fishEnergy > 0 && maxSharkEnergy > 0 && livesReproduce > 0;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	protected String getName() {
		return "PredModel";
	}

}

