package gui.dialogue;

public class SegDialog extends Dialog {
	public SegDialog() {
		super("Segregation Model");
	}

	@Override
	protected void addTexts() {
		super.addTexts();
		addTextField("percentA", "% Type A Cells:", "0.45", "% Type A Cells", "Double between 0 and 1");
		addTextField("percentB", "% Type B Cells:", "0.45", "% Type A Cells", "Double between 0 and 1");
		addTextField("similarity", "Similarity threshold:", "0.5", "Similarity threshold", "Double between 0 and 1");
	};

	@Override
	protected boolean validate() {
		try {
			int width = Integer.parseInt(myTexts.get("width").getText().trim());
			int height = Integer.parseInt(myTexts.get("height").getText().trim());
			double pA = Double.parseDouble(myTexts.get("percentA").getText().trim());
			double pB = Double.parseDouble(myTexts.get("percentB").getText().trim());
			double pS = Double.parseDouble(myTexts.get("similarity").getText().trim());

			return width <= 100 && width > 0 && height <= 100 && height > 0 
					&& pA >= 0 && pB >= 0 && pA + pB <= 1 && pS >= 0 && pS <= 1;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	protected String getName() {
		return "SegModel";
	}

}

