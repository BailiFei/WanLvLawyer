package com.m7.imkfsdk.utils;

import android.webkit.MimeTypeMap;

import java.util.HashMap;
import java.util.Map;

public class MimeTypes {

	private Map<String, String> mMimeTypes;
	private Map<String, Integer> mIcons;

	public MimeTypes() {
		mMimeTypes = new HashMap<String,String>();
		mIcons = new HashMap<String,Integer>();
	}
	
	/* I think the type and extension names are switched (type contains .png, extension contains x/y),
	 * but maybe it's on purpouse, so I won't change it.
	 */
	public void put(String type, String extension, int icon){
		put(type, extension);
		mIcons.put(extension, icon);
	}
	
	public void put(String type, String extension) {
		// Convert extensions to lower case letters for easier comparison
		extension = extension.toLowerCase();
		
		mMimeTypes.put(type, extension);
	}
	
	public String getMimeType(String filename) {
		
		String extension = FileUtils.getExtension(filename);
		
		// Let's check the official map first. Webkit has a nice extension-to-MIME map.
		// Be sure to remove the first character from the extension, which is the "." character.
		if (extension.length() > 0) {
			String webkitMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
		
			if (webkitMimeType != null) {
				// Found one. Let's take it!
				return webkitMimeType;
			}
		}
		
		// Convert extensions to lower case letters for easier comparison
		extension = extension.toLowerCase();
		
		String mimetype = mMimeTypes.get(extension);
		
		if(mimetype==null) mimetype = "*/*";
		
		return mimetype;
	}
	
	public int getIcon(String mimetype){
		Integer iconResId = mIcons.get(mimetype);
		if(iconResId == null)
			return 0; // Invalid identifier
		return iconResId;
	}

}
