package com.blackstrawai;

import com.github.opendevl.JFlat;

/**
 * Utility class for JSON type
 * 
 * @author adityabharadwaj
 *
 */
public class JSONHelper {
	private static JSONHelper jsonHelper;
	
	/**
	 * Return singleton instance of JSONHelper
	 * 
	 * @return JSONHelper instance
	 */
	public static JSONHelper getInstance() {
		if (jsonHelper == null) {
			jsonHelper = new JSONHelper();
		}
		return jsonHelper;
	}
	
	/**
	 * Exports JSON string to CSV
	 * 
	 * @param jsonString - JSON String to be exported
	 * @param attachmentsPath - Path where the CSV file is to be saved
	 * @throws ApplicationException
	 */
	public void exportJsonToCsv(String jsonString, String attachmentsPath) throws ApplicationException {
		JFlat flatMe = new JFlat(jsonString);

        try {
        	//get the 2D representation of JSON document
			flatMe.json2Sheet().headerSeparator("/").getJsonAsSheet();
			//write the 2D representation in csv format
	        flatMe.write2csv(attachmentsPath);
		} catch (Exception e) {
			throw new ApplicationException("Error during export of json to csv", e);
		}
	}
}
