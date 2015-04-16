package datx021512.chalmers.se.greenme.ICA;


import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class IcaAPI {
    private String mItem;
    private int mOCRNr;
    private final String URL = "http://api.ica.se/api/upclookup/?upc=";

    public IcaAPI(int OCRNR)
    {
        this.mOCRNr = OCRNR;
    }

    public String getItem()
    {
        return this.mItem;
    }

    public String getStringFromAPI()
    {
        if(this.mOCRNr == 0)
            return "";

        URL url = null;
        try
        {
            url = new URL(this.URL + Integer.toString(this.mOCRNr));
            if(url != null)
            {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("UpcArticleDto");
                Log.d("API","node data: " + nodeList.item(1).getTextContent());
                return nodeList.item(1).getTextContent();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }

}
