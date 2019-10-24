package com.m7.imkfsdk.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;

import com.m7.imkfsdk.R;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

public class MimeTypesTools {
	
	private static boolean hasLoadMimeType = false;
	
	public static String getMimeType(Context context, String fileName) {
		if (!TextUtils.isEmpty(fileName)) {
			fileName = fileName.toLowerCase();

			MimeTypes mimeTypes = getMimeTypes(context);
			String extension = FileUtils.getExtension(fileName);
			return mimeTypes.getMimeType(extension);
		}

		return null;
	}

	private static MimeTypes getMimeTypes(Context context) {
		return loadMimeTypes(context);
	}

	private static MimeTypes loadMimeTypes(Context context) {
		MimeTypeParser parser = null;
		XmlResourceParser xmlResourceParser = null;
		if (!hasLoadMimeType) {
			try {
				parser = new MimeTypeParser(context, context.getPackageName());
				xmlResourceParser = context.getResources().getXml(R.xml.mimetypes);

				return parser.fromXmlResource(xmlResourceParser);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			hasLoadMimeType = true;
		}

		return null;
	}
}
