package com.silviaterra;

import java.util.ArrayList;

/**
 *
 * @author vincent
 */
public class SilviaTerra
{
    static double DBHLength;
    static double DBHPixels;
    static double HFOV;
    static double VFOV;
    static double HRes;
    static double VRes;
    static double crosshairModifier;
    static double verticalTiltAngle;
    
    static double horizontalDistance;
    static double horizontalPixelDistance;
    static double topDistance;
    static double widthYPos;
    static double widthPixelLength;
    static double width;
    static double height;
    
    static double[][] pixelCoordinates;
    static double[][] pixelDimensions;
    static ArrayList<Section> treeSections = new ArrayList();
    static ArrayList<ArrayList<Double>> pixelList = new ArrayList();
    
    public static void main(String[] args)
    {
        /*
         * User set variables
         */
        DBHLength = 59;
        DBHPixels = 283;
        HFOV= 47.1;
        VFOV = 60.5;
        HRes = 2448;
        VRes = 3264;
        verticalTiltAngle = 18;
        
//        double[] widths = new double[]{177,299,511,968};
//        double[] heights = new double[]{1667,1316,739,112};
        
        for(int i = 0; i < 6; i++)
        {
            pixelList.add(new ArrayList<Double>());
        }
        
        pixelList.get(0).add(1247.0);
        pixelList.get(0).add(1191.0);
        pixelList.get(0).add(1636.0);
        
        pixelList.get(1).add(1282.0);
        pixelList.get(1).add(1151.0);
        pixelList.get(1).add(1709.0);
        
        pixelList.get(2).add(1305.0);
        pixelList.get(2).add(1129.0);
        pixelList.get(2).add(1785.0);
        
        pixelList.get(3).add(1319.0);
        pixelList.get(3).add(1112.0);
        pixelList.get(3).add(1854.0);
        
        pixelList.get(4).add(1338.0);
        pixelList.get(4).add(1090.0);
        pixelList.get(4).add(1937.0);
        
        pixelList.get(5).add(1363.0);
        pixelList.get(5).add(1069.0);
        pixelList.get(5).add(2001.0);
                
        getHorizontalDistance();
        getHorizontalPixelDistance();
        getTopDistance();
        
        System.out.println(horizontalDistance);
        System.out.println(horizontalPixelDistance);
        System.out.println();
        
        for(int i = 0; i < pixelList.size(); i++)
        {
            getPixelHeights(i);
            getPixelWidths(i);

            getWidth(i);
            getHeight(i);
            
            System.out.println(pixelList.get(i).get(1) + " : " + pixelList.get(i).get(2) + " : " + pixelList.get(i).get(3) + " : " + pixelList.get(i).get(4) + " : " + pixelList.get(i).get(5));
            
            if(i % 2 == 0 && i != 0)
            {
                Section section = new Section(pixelList, i / 2);
                section.getVolumeShape();
                System.out.println(section.getVolume());
                treeSections.add(section);
            }
        }   
    }
    
    public static void getHorizontalDistance()
    {
        double imageWidth;
        
        imageWidth = (DBHLength / DBHPixels) * HRes;
        horizontalDistance = (imageWidth / 2) / Math.tan(Math.toRadians(HFOV / 2));
    }
    
    public static void getHorizontalPixelDistance()
    {
        if(verticalTiltAngle > (VFOV / 2))
        {
            double topAngle;
            double bisectionLengthPixels;
            
            topAngle = 180 - 90 - verticalTiltAngle;
            bisectionLengthPixels = Math.sin(Math.toRadians(topAngle)) / (Math.sin(Math.toRadians(VFOV / 2)) / (VRes / 2));
            
            horizontalPixelDistance = bisectionLengthPixels * Math.cos(Math.toRadians(verticalTiltAngle - (VFOV / 2)));
        }
        else
        {
            horizontalPixelDistance = (VRes / 2) / Math.tan(Math.toRadians(verticalTiltAngle));
        }
    }
    
    public static void getTopDistance()
    {   
        topDistance = horizontalDistance / Math.cos(Math.toRadians(verticalTiltAngle));
    }
    
    public static void getWidth(int i)
    {
        double missingVerticalPixelDistance;
        double angleToWidth;
        double distanceToWidth;
        double imageWidth;
        
        widthYPos = pixelList.get(i).get(3);
        widthPixelLength = pixelList.get(i).get(2);

        if(verticalTiltAngle > (VFOV / 2))
        {
            missingVerticalPixelDistance = horizontalPixelDistance * Math.tan(Math.toRadians(verticalTiltAngle - (VFOV / 2)));
            widthYPos = widthYPos + missingVerticalPixelDistance;

            angleToWidth = Math.toDegrees(Math.atan(widthYPos / horizontalPixelDistance));

            distanceToWidth = horizontalDistance / Math.cos(Math.toRadians(angleToWidth));

            imageWidth = (distanceToWidth * Math.tan(Math.toRadians(HFOV / 2))) * 2;

            pixelList.get(i).add((imageWidth / HRes) * widthPixelLength);
        }
        else
        {
            angleToWidth = Math.toDegrees(Math.atan(widthYPos / horizontalPixelDistance));

            distanceToWidth = horizontalDistance / Math.cos(Math.toRadians(angleToWidth));

            imageWidth = (distanceToWidth * Math.tan(Math.toRadians(HFOV / 2))) * 2;

            pixelList.get(i).add((imageWidth / HRes) * widthPixelLength);
        }
    } 
    
    public static void getHeight(int i)
    {
        double missingVerticalPixelDistance;
        double angleToWidth;
 
        widthYPos = pixelList.get(i).get(3);
        
        if(verticalTiltAngle > (VFOV / 2))
        {
            missingVerticalPixelDistance = horizontalPixelDistance * Math.tan(Math.toRadians(verticalTiltAngle - (VFOV / 2)));
            widthYPos = widthYPos + missingVerticalPixelDistance;

//            angleToWidth = Math.toDegrees(Math.atan(widthYPos / horizontalPixelDistance));
//
//            pixelList.get(i).add(Math.tan(Math.toRadians(angleToWidth)) * horizontalDistance);
            
            pixelList.get(i).add(((Math.tan(Math.toRadians(verticalTiltAngle)) * horizontalDistance) / (pixelList.get(0).get(3)) + missingVerticalPixelDistance) * (widthYPos +  missingVerticalPixelDistance));
            
        }
        else
        {
//            angleToWidth = Math.toDegrees(Math.atan(widthYPos / horizontalPixelDistance));
//
//            pixelList.get(i).add(Math.tan(Math.toRadians(angleToWidth)) * horizontalDistance);
            
            pixelList.get(i).add(((Math.tan(Math.toRadians(verticalTiltAngle)) * horizontalDistance) / pixelList.get(0).get(3)) * widthYPos);
        }
    }
    
    public static void getPixelWidths(int i)
    {
        pixelList.get(i).set(2, pixelList.get(i).get(0) - pixelList.get(i).get(1));
    }
    
    public static void getPixelHeights(int i)
    {
        pixelList.get(i).add(VRes - pixelList.get(i).get(2));
    }
}
