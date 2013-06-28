package com.silviaterra;

/*
 * 
 * @author Vincent Szolnoky (ippytraxx@unixhub.net)
 */
    
public class SilviaTerra
{

    public static void main(String[] args)
    {
        /*
         * Input variables
         */
        
        final double HFOV = 47.1;
        final double VFOV = 60;
        double DBHLength = 83;
        double DBHPixels = 1626;
        double imageWidthPixels = 2448;
        double imageHeightPixels = 3264;
        double DTHY = 1543;
        double DBHY = 1605;
        double verticalTiltAngle = 52;
        double widthYPos = 1543;
        double widthPixels = 202;
        final double crosshairModifier = 1;
        
        double distanceToTree;
        double lowerFOVAngle;
        
        //distanceToTree = getDistanceToTree(imageWidthPixels, DBHPixels, DBHLength, HFOV);
        
        lowerFOVAngle = getLowerFOVAngle(crosshairModifier, imageHeightPixels, VFOV);
        
        distanceToTree = getHorizontalDistance(crosshairModifier, imageHeightPixels, imageWidthPixels, DBHPixels, DBHLength, VFOV, HFOV);
        
        System.out.println(distanceToTree);
                   
//        System.out.println(getScaledWidth(DTHY, widthYPos, widthPixels, VFOV, HFOV, verticalTiltAngle, distanceToTree, imageHeightPixels));
        
        System.out.println(getScaledWidth(distanceToTree, verticalTiltAngle, HFOV, imageWidthPixels, widthPixels, lowerFOVAngle, imageHeightPixels, DBHY, widthYPos, DTHY));
        System.out.println(getScaledHeight(DTHY, verticalTiltAngle, VFOV,imageHeightPixels, widthYPos, distanceToTree, DBHY, lowerFOVAngle));
        
        //System.out.println(getHorizontalDistance(crosshairModifier, imageHeightPixels, imageWidthPixels, DBHPixels, DBHLength, VFOV, HFOV));
    }
    
    /**
     * Gets distance between the tree and camera lens
     * 
     * @param imageWidthPixels  Width of entire image in pixels
     * @param DBHPixels          Width of tree at breast height in pixels
     * @param DBHLength          Width of tree at breast height in centimeters
     * @param HFOV                 Field of view in degrees
     * @return                     Distance to tree in centimeters
     */
    
    public static double getDistanceToTree(double imageWidthPixels, double DBHPixels, double DBHLength, double HFOV)
    {
        double imageWidthLength = ((imageWidthPixels / DBHPixels) * DBHLength);
        double distanceToTree = (imageWidthLength / 2) / (Math.tan(Math.toRadians(HFOV/2)));
        
        return distanceToTree;
    }
    
    public static double getScaledWidth(double horizontalDistance, double verticalTiltAngle, double HFOV, double imageWidthPixels, double widthPixels, double lowerFOVAngle, double imageHeightPixels, double DBHY, double widthYPos, double DTHY)
    {
        double distanceToWidth;
        double imageWidthLength;
        double scaledWidth;
        
        distanceToWidth = getDistanceToWidth(horizontalDistance, verticalTiltAngle, lowerFOVAngle, imageHeightPixels, DBHY, widthYPos, DTHY);
        imageWidthLength = (Math.tan(Math.toRadians(HFOV / 2)) * distanceToWidth) * 2;
        
        scaledWidth = (imageWidthLength / imageWidthPixels) * widthPixels;
        
        return scaledWidth;
    }
    
//    public static double getScaledWidtha(double DTHY, double widthYPos, double widthPixels, double lowerFOVAngle, double HFOV, double verticalTiltAngle, double distanceToTree, double imageHeightPixels)
//    {
//        double visiblePixels = imageHeightPixels - DTHY;
//        double hiddenAngle = verticalTiltAngle - lowerFOVAngle;
//        
//        if(hiddenAngle > 0)
//        {
//            visiblePixels = imageHeightPixels - DTHY;
//            angleToWidth = hiddenAngle + (((VFOV / 2) / visiblePixels) * (imageHeightPixels - widthYPos));
//        }
//        else
//        {
//            visiblePixels = DBHY - DTHY;
//            angleToWidth = (verticalTiltAngle / visiblePixels) * (DBHY - widthYPos);
//        }
//        
//        double angleToWidth = hiddenAngle + (((VFOV / 2) / visiblePixels) * (imageHeightPixels - widthYPos));
//        double distanceToWidth = distanceToTree / Math.cos(Math.toRadians(angleToWidth));
//        double lengthOfImageAtWidth = (Math.tan(Math.toRadians(HFOV / 2)) * distanceToWidth) * 2;
//        
//        double widthLength = (lengthOfImageAtWidth / 3264) * widthPixels;
//        
//        return widthLength;
//    }
    
    public static double getScaledHeight(double DTHY, double verticalTiltAngle, double VFOV, double imageHeightPixels, double widthYPos, double distanceToTree, double DBHY, double lowerFOVAngle)
    {
        double visiblePixels;
        double hiddenAngle = verticalTiltAngle - lowerFOVAngle;
        double angleToWidth;
        
        if(hiddenAngle > 0)
        {
            visiblePixels = imageHeightPixels - DTHY;
            angleToWidth = hiddenAngle + ((lowerFOVAngle / visiblePixels) * (imageHeightPixels - widthYPos));
        }
        else
        {
            visiblePixels = DBHY - DTHY;
            angleToWidth = (verticalTiltAngle / visiblePixels) * (DBHY - widthYPos);
        }
        
        double heightToWidth = Math.tan(Math.toRadians(angleToWidth)) * distanceToTree;
        
        return heightToWidth;
    }
    
    public static double getHorizontalDistance(double crosshairModifier, double imageHeightPixels, double imageWidthPixels, double DBHPixels, double DBHLength, double VFOV, double HFOV)
    {
        double distanceFromBottom;
        double angleToDBH;
        double imageWidthLength;
        double distanceToDBH;
        double horizontalDistance;
        
        distanceFromBottom = imageHeightPixels - (imageHeightPixels / crosshairModifier);
        
        if(distanceFromBottom != 0)
        {
            angleToDBH = (VFOV / 2) - ((VFOV / imageHeightPixels) * distanceFromBottom);
            imageWidthLength = (imageWidthPixels / DBHPixels) * DBHLength;
            distanceToDBH = (imageWidthLength / 2) / Math.tan(Math.toRadians(HFOV / 2));
            horizontalDistance = Math.cos(Math.toRadians(angleToDBH)) * distanceToDBH;
        }
        else
        {
            imageWidthLength = ((imageWidthPixels / DBHPixels) * DBHLength);
            horizontalDistance = (imageWidthLength / 2) / (Math.tan(Math.toRadians(HFOV/2)));
        }
        
        System.out.println(horizontalDistance);
        
        return horizontalDistance;
    }
    
    public static double getLowerFOVAngle(double crosshairModifier, double imageHeightPixels, double VFOV)
    {
        double distanceFromBottom;
        double lowerFOVAngle;
        
        distanceFromBottom = imageHeightPixels - (imageHeightPixels / crosshairModifier);
        
        if(distanceFromBottom == 0)
        {
            lowerFOVAngle = VFOV / 2;
        }
        else
        {
            lowerFOVAngle = (VFOV / imageHeightPixels) * distanceFromBottom;
        }
        
        return lowerFOVAngle;
    }
    
    public static double getDistanceToWidth(double horizontalDistance, double verticalTiltAngle, double lowerFOVAngle, double imageHeightPixels, double DBHY, double widthYPos, double DTHY)
    {
        double distanceToWidth;
        double visiblePixels;
        double hiddenAngle = verticalTiltAngle - lowerFOVAngle;
        double angleToWidth;
        
        if(hiddenAngle > 0)
        {
            visiblePixels = imageHeightPixels - DTHY;
            angleToWidth = hiddenAngle + ((lowerFOVAngle / visiblePixels) * (imageHeightPixels - widthYPos));
        }
        else
        {
            visiblePixels = DBHY - DTHY;
            angleToWidth = (verticalTiltAngle / visiblePixels) * (DBHY - widthYPos);
        }
        
        distanceToWidth = horizontalDistance / Math.cos(Math.toRadians(verticalTiltAngle));
        
        return distanceToWidth;
    }
}
