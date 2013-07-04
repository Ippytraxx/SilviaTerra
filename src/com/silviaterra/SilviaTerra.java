package com.silviaterra;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


/**
 *
 * @author vincent
 */
public class SilviaTerra
{
    static double DBHLength = 59;
    static double DBHPixels;
    static double VFOV;
    static double HFOV;
    static double verticalTiltAngle = 14.5;
    static double HRes = 2448;
    static double VRes = 3264;
    static double highestDBHHeight;
    static double imagePlateScale;
    static double horizontalDistance;
    
    static ArrayList<ArrayList<Double>> pixelList = new ArrayList();
    static ArrayList<HashMap<String, ArrayList<Double>>> rawDBHPixelList;
    static ArrayList<HashMap<String, ArrayList<Double>>> rawBolePixelList;
    static ArrayList<ArrayList<Double>> dbhPixelList = new ArrayList();
    static ArrayList<ArrayList<Double>> bolePixelList = new ArrayList();
    
    public static void main(String[] args)
    {
        JSONParser jsonParser = new JSONParser();
        
        try
        {
            Object obj = jsonParser.parse(new FileReader("/home/vincent/Development/Java_Projects/SilviaTerra/examples/global_info.txt"));
            JSONObject jsonObject1 = (JSONObject) obj;
            
            obj = jsonParser.parse(new FileReader("/home/vincent/Development/Java_Projects/SilviaTerra/examples/tree_info.txt"));
            JSONObject jsonObject2 = (JSONObject) obj;
            
            obj = jsonParser.parse(new FileReader("/home/vincent/Development/Java_Projects/SilviaTerra/examples/pixels1.txt"));
            JSONObject jsonObject3 = (JSONObject) obj;
            
//            DBHLength = (Double.parseDouble((String) jsonObject2.get("dbh"))) * 2.54;
            VFOV = (double) jsonObject1.get("horizontalViewAngle");
            HFOV = (double) jsonObject1.get("verticalViewAngle");
//            verticalTiltAngle = ((double) (jsonObject1.get("bolePitch"))) + 90.0;
            rawDBHPixelList = (ArrayList) jsonObject3.get("dbh");
            rawBolePixelList = (ArrayList) jsonObject3.get("bole");
            
            getDBHPixels();
            getHorizontalDistance();
            getImagePlateScale();
            getPixelWidths(rawDBHPixelList, dbhPixelList);
            getPixelHeights(rawDBHPixelList, dbhPixelList, true);
            getHeights(dbhPixelList, true);
            getWidths(dbhPixelList);
            
            getHighestDBHHeight();
            getPixelWidths(rawBolePixelList, bolePixelList);
            getPixelHeights(rawBolePixelList, bolePixelList, false);
            getHeights(bolePixelList, false);
            getWidths(bolePixelList);
            
            System.out.println(horizontalDistance);
            for(int i = 0; i < bolePixelList.size(); i++)
            {
                System.out.println(bolePixelList.get(i).get(0) + " : " + bolePixelList.get(i).get(1) + " : " + bolePixelList.get(i).get(2) + " : " + bolePixelList.get(i).get(3) + " : " + bolePixelList.get(i).get(4));
            }
            
            for(int i = 0; i < dbhPixelList.size(); i++)
            {
                System.out.println(dbhPixelList.get(i).get(0) + " : " + dbhPixelList.get(i).get(1) + " : " + dbhPixelList.get(i).get(2) + " : " + dbhPixelList.get(i).get(3) + " : " + dbhPixelList.get(i).get(4));
            }
            
            integrateTrapezoids();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void getDBHPixels()
    {
        for(int i = 0; i < rawDBHPixelList.size(); i++)
        {
            if(rawDBHPixelList.get(i).containsKey("dbh"))
            {
                DBHPixels = rawDBHPixelList.get(i).get("right").get(0) - rawDBHPixelList.get(i).get("left").get(0);
            }
        }
    }
    
    public static void getPixelWidths(ArrayList<HashMap<String, ArrayList<Double>>> list, ArrayList<ArrayList<Double>> list1)
    {
        for(int i = 0; i < list.size(); i++)
        {
            list1.add(new ArrayList());
            list1.get(i).add(list.get(i).get("right").get(0) - list.get(i).get("left").get(0));
        }
    }
    
    public static void getPixelHeights(ArrayList<HashMap<String, ArrayList<Double>>> list, ArrayList<ArrayList<Double>> list1, boolean isDBH)
    {
        if(isDBH)
        {
            for(int i = 0; i < list.size(); i++)
            {
                list1.get(i).add((VRes / 2) - list.get(i).get("right").get(1));
            }
        }
        else
        {
            for(int i = 0; i < list.size(); i++)
            {
                list1.get(i).add(VRes - list.get(i).get("right").get(1));
            }
        }
    }
    
    public static void getWidths(ArrayList<ArrayList<Double>> list)
    {
        double distanceToWidth;
        double imageCmPerPx;
        
        for(int i = 0; i < list.size(); i++)
        {
            distanceToWidth = horizontalDistance / Math.cos(Math.toRadians(list.get(i).get(2)));
            imageCmPerPx = ((Math.tan(Math.toRadians(HFOV / 2)) * distanceToWidth) * 2) / HRes;
            list.get(i).add(imageCmPerPx * list.get(i).get(0));
        }
    }
    
    public static void getHeights(ArrayList<ArrayList<Double>> list, boolean isDBH)
    {
        double angleToHeight;
        
        if(isDBH)
        {
            for(int i = 0; i < list.size(); i++)
            {
                list.get(i).add(list.get(i).get(1) / imagePlateScale);
                list.get(i).add(Math.tan(Math.toRadians(list.get(i).get(2))) * horizontalDistance);
            }
        }
        else
        {
            for(int i = 0; i < list.size(); i++)
            {
                list.get(i).add((list.get(i).get(1) / imagePlateScale) + (verticalTiltAngle - (VFOV / 2)));
                if(Math.tan(Math.toRadians(list.get(i).get(2))) * horizontalDistance > highestDBHHeight * 1.1)
                {
                    list.get(i).add(Math.tan(Math.toRadians(list.get(i).get(2))) * horizontalDistance);
                }
                else
                {
                    list.remove(i);
                    i--;
                }
            }
        }
    }
    
    public static void getHighestDBHHeight()
    {
        highestDBHHeight = dbhPixelList.get(dbhPixelList.size() - 1).get(3);
    }
    
    public static void getImagePlateScale()
    {
        imagePlateScale = VRes / VFOV;
    }
    
    public static void getHorizontalDistance()
    {
        horizontalDistance = (((DBHLength / DBHPixels) * HRes) / 2) / Math.tan(Math.toRadians(HFOV / 2));
    }
    
    public static void integrateTrapezoids()
    {
        double h1;
        double h2;
        double b;
        double volume = 0;
        
        pixelList.addAll(dbhPixelList);
        pixelList.addAll(bolePixelList);
        
        for(int i = 0; i < pixelList.size() - 1; i++)
        {
            h1 = Math.pow(pixelList.get(i).get(4) / 2, 2) * Math.PI;
            h2 = Math.pow(pixelList.get(i+1).get(4) / 2, 2) * Math.PI;
            b = pixelList.get(i+1).get(3) - pixelList.get(i).get(3);
            
           volume += (0.5 * (h1 + h2)) * b;
        }
        System.out.println(volume);
    }
}
 