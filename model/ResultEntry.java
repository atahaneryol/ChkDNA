package chkdna.model;

public class ResultEntry {

	private String type;
	private String title;
	private String message;

	/**
	 * 
	 * @param title
	 * @param message
	 * @param type
	 */
	public ResultEntry(String title, String message, String type) {
		throw new UnsupportedOperationException();
	}

	public String getTitle() {
		return this.title;
	}

	public String getMessage() {
		return this.message;
	}

	public String getType() {
		return this.type;
	}

}