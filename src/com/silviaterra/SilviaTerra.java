package com.silviaterra;

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
    
    public static void main(String[] args)
    {
        /*
         * User set variables
         */
        DBHLength = 59;
        DBHPixels = 381;
        HFOV= 47.1;
        VFOV = 60.5;
        HRes = 2448;
        VRes = 3264;
        verticalTiltAngle = 22;
        
        double[] widths = new double[]{72,169,232,271,329,390};
        double[] heights = new double[]{1683,1590,1493,1383,1298,1200};
                
        getHorizontalDistance();
        getHorizontalPixelDistance();
        getTopDistance();
        
        System.out.println(horizontalDistance);
        System.out.println(horizontalPixelDistance);
        System.out.println();
        
        for(int i = 0; i < widths.length; i++)
        {
            widthPixelLength = widths[i];
            widthYPos = heights[i];
            getWidth();
            getHeight();
            
            System.out.println(height + " - " + width);
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
    
    public static void getWidth()
    {
        double missingVerticalPixelDistance;
        double angleToWidth;
        double distanceToWidth;
        double imageWidth;

        if(verticalTiltAngle > (VFOV / 2))
        {
            missingVerticalPixelDistance = horizontalPixelDistance * Math.tan(Math.toRadians(verticalTiltAngle - (VFOV / 2)));
            widthYPos = widthYPos + missingVerticalPixelDistance;
            
            angleToWidth = Math.toDegrees(Math.atan(widthYPos / horizontalPixelDistance));
            
            distanceToWidth = horizontalDistance / Math.cos(Math.toRadians(angleToWidth));
            
            imageWidth = (distanceToWidth * Math.tan(Math.toRadians(HFOV / 2))) * 2;
            
            width = (imageWidth / HRes) * widthPixelLength;
        }
        else
        {
            angleToWidth = Math.toDegrees(Math.atan(widthYPos / horizontalPixelDistance));
            
            distanceToWidth = horizontalDistance / Math.cos(Math.toRadians(angleToWidth));
            
            imageWidth = (distanceToWidth * Math.tan(Math.toRadians(HFOV / 2))) * 2;
            
            width = (imageWidth / HRes) * widthPixelLength;
        }
    } 
    
    public static void getHeight()
    {
        double missingVerticalPixelDistance;
        double angleToWidth;
        
        if(verticalTiltAngle > (VFOV / 2))
        {
            missingVerticalPixelDistance = horizontalPixelDistance * Math.tan(Math.toRadians(verticalTiltAngle - (VFOV / 2)));
            widthYPos = widthYPos + missingVerticalPixelDistance;
            
            angleToWidth = Math.toDegrees(Math.atan(widthYPos / horizontalPixelDistance));
            
            height = Math.tan(Math.toRadians(angleToWidth)) * horizontalDistance;
        }
        else
        {
            angleToWidth = Math.toDegrees(Math.atan(widthYPos / horizontalPixelDistance));
            
            height = Math.tan(Math.toRadians(angleToWidth)) * horizontalDistance;
        }
    }
}
