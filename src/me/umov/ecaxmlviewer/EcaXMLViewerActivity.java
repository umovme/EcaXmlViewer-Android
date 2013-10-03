package me.umov.ecaxmlviewer;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EcaXMLViewerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_eca_xml_viewer);
		
		Object context = getIntent().getStringExtra("context");

		String contextData = context != null ? formatXML(context.toString()) : "no eca context data";
		
		EditText xml = (EditText) findViewById(R.id.activity_eca_xml_viewer_text);
		
		xml.setText(contextData != null ? contextData : "could not format xml");
	}
	
	private String formatXML(final String contextData){

		try{
			InputSource is = new InputSource(new StringReader(contextData));
			
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	        Writer out = new StringWriter();
	        transformer.transform(new DOMSource(document), new StreamResult(out));
	        return out.toString();
			
		} catch (Exception e) {
			Log.i("EcaXMLViewer", "ERROR: " + e.toString());
		}
		
		return null;
	}

	@Override
	public void onBackPressed() {
		quitApp();
	}
	
	private void quitApp(){
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int selectedOption) {
		        switch (selectedOption){
		        	case DialogInterface.BUTTON_POSITIVE:
			            finish();
			            break;

		        	case DialogInterface.BUTTON_NEGATIVE:
		        		break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to quit?");
		builder.setPositiveButton("Yes", dialogClickListener);
	    builder.setNegativeButton("No", dialogClickListener);
	    builder.show();
	}
}
