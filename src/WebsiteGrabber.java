/**
 * Created by William Robert Howerton III 2/21/2016
 */


import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class WebsiteGrabber {
    public static void main(String[] args) {

        File jsFile = new File("C:\\Users\\Howerton\\Documents\\elements.js");
        Hashtable<Integer, String> atomicNumToEntry = new Hashtable<>(10);
        Hashtable<Integer, String> numToArrayEntry = new Hashtable<>(10);
        ArrayList<String> entriesList = new ArrayList<>(10);
        ArrayList<String> arrayEntryList = new ArrayList<>(10);
        Document doc;
        try {
            doc = Jsoup.connect("http://www.science.co.il/PTelements.asp").get();

            StringBuilder elementEntry = new StringBuilder("");

            BufferedWriter writer = new BufferedWriter(new FileWriter(jsFile));

            for (Element table : doc
                    .select("table[class=tabint8]")) {
                for (Element row : table.select("tr")) {
                    Elements tds = row.select("td");
                    if (tds.isEmpty()) { // Header <tr> with only <th>s
                        continue;

                    }else{

                        for(int i = 0; i < tds.size(); i++){
                            // System.out.printf("Index: %d: %s\n", i, tds.get(i).text());

                            String elementName = tds.get(3).text();
                            String atomicNumber = tds.get(0).text();
                            String atomicWeight = tds.get(2).text();
                            String symbol = tds.get(4).text();
                            String group = tds.get(10).text();

                            elementEntry = new StringBuilder("\nvar ").append(elementName)
                                    .append(" = {\n").append("\tnumber:").append(atomicNumber).append(",\n")
                                    .append("\tatomic_weight:").append(atomicWeight).append(",\n")
                                    .append("\tname:'").append(elementName).append("',\n").append("\tsymbol:'")
                                    .append(symbol).append("',\n").append("\tgroup:").append(group).append("\n")
                                    .append("};");

                            numToArrayEntry.putIfAbsent(Integer.parseInt(atomicNumber), "'"+symbol+"': "+elementName+",\n");
                            atomicNumToEntry.putIfAbsent(Integer.parseInt(atomicNumber),
                                    elementEntry.toString());


                        }


                    }

                }
            }

            Enumeration keys = atomicNumToEntry.keys();
            String array = new String("\n\nelements = {");
            while(keys.hasMoreElements()){
                Object key = keys.nextElement();
                entriesList.add(atomicNumToEntry.get(key));
                arrayEntryList.add(numToArrayEntry.get(key));
            }

            Collections.reverse(entriesList);
            Collections.reverse(arrayEntryList);
            for(String s : entriesList){
                writer.write(s);
            }

            for(String s : arrayEntryList){
                array+=s;
            }
            array += "};\n";

            writer.write(array);

            writer.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}