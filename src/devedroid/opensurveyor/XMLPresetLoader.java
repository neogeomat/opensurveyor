package devedroid.opensurveyor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class XMLPresetLoader {
	private static final String ns = null;

	public List<BasePreset> loadPresets(InputStream in) throws IOException,
			XmlPullParserException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readPreset(parser);
		} finally {
			in.close();
		}

	}

	private List<BasePreset> readPreset(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<BasePreset> prs = new ArrayList<BasePreset>();
		parser.require(XmlPullParser.START_TAG, ns, "preset");
		for (int i = 0; i < parser.getAttributeCount(); i++) {
			String attr = parser.getAttributeName(i);
		}
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("button")) {
				prs.add(readButton(parser));
			} else {
				skip(parser);
			}
		}
		return prs;
	}

	private BasePreset readButton(XmlPullParser parser) throws XmlPullParserException, IOException {
		POIPreset res = null;
		parser.require(XmlPullParser.START_TAG, ns, "button");
		String title = parser.getAttributeValue(ns, "label");
		String sDir = parser.getAttributeValue(ns, "directional");
		String sToggle = parser.getAttributeValue(ns, "toggle");
		String icon = parser.getAttributeValue(ns, "icon");
		String type;		
		
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("poi")) {
				type = parser.getAttributeValue(ns, "type");
				res = new POIPreset(title, type, icon);
				if(sDir!=null) res.setDirected( sDir.equals("yes") );
				if(sToggle!=null) res.setToggleButton( sToggle.equals("yes" ));
				while(parser.next() != XmlPullParser.END_TAG ) ; 
			} else if (name.equals("properties")) {
				readProperties(parser, res);
			} else {
				skip(parser);
			}
		}
		

		return res;
	}
	
	private void readProperties(XmlPullParser parser, POIPreset prs) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "properties");
		
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("property")) {
				prs.addProperty( parser.getAttributeValue(ns, "k"));
				skip(parser);
				//while(parser.next() != XmlPullParser.END_TAG ) ;
				parser.require(XmlPullParser.END_TAG, ns, "property");
			} else {
				skip(parser);
			}
		}
		parser.require(XmlPullParser.END_TAG, ns, "properties");

	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

}
