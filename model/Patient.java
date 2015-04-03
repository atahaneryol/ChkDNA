package chkdna.model;

public class Patient {

	private String vcfFilePath;
	private static Patient instance;

	/**
	 * 
	 * @param vcfFilePath
	 */
	protected Patient(String vcfFilePath) {
		throw new UnsupportedOperationException();
	}

	public static Patient getInstance() {
		return Patient.instance;
	}

}