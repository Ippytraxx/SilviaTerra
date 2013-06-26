package com.silviaterra;

/*
 * 
 * @author Vincent Szolnoky (ippytraxx@unixhub.net)
*/
    
public class SilviaTerra
{

    public static void main(String[] args)
    {
        final int FOV = 59;
        double DBHLength = 10;
        double DBHPixels = 15;
        double imageWidthPixels = 3264;
        double distanceToTree;
        double verticalTiltAngle = 22.9;
        double treeHeight;
        double distanceToTop;
        double DTHPixels = 14;
        double topDiameter;
        
        distanceToTree = getDistanceToTree(imageWidthPixels, DBHPixels, DBHLength, FOV);
        treeHeight = getTreeHeight(distanceToTree, verticalTiltAngle);
        distanceToTop = getDistanceToTop(distanceToTree, treeHeight);
        topDiameter = getTopDiameter(DTHPixels, distanceToTop, FOV, imageWidthPixels);
        
        System.out.println(distanceToTree + " : " + distanceToTop + " : " + treeHeight);
        System.out.println(topDiameter);
    }
    
    public static double getDistanceToTree(double imageWidthPixels, double DBHPixels, double DBHLength, int FOV)
    {
        double imageWidthLength = ((imageWidthPixels / DBHPixels) * DBHLength);
        System.out.println(imageWidthLength);
        System.out.println(Math.tan(Math.toRadians(30)));
        double distanceToTree = (imageWidthLength / 2) / (Math.tan(Math.toRadians(FOV/2)));
        
        return distanceToTree;
    }
    
    public static double getTreeHeight(double distanceToTree, double verticalTiltAngle)
    {
        double treeHeight = Math.tan(Math.toRadians(verticalTiltAngle)) * distanceToTree;
        
        return treeHeight;
    }
    
    public static double getDistanceToTop(double distanceToTree, double treeHeight)
    {
        double distanceToTop = Math.sqrt(Math.pow(distanceToTree, 2) + Math.pow(treeHeight, 2));
        
        return distanceToTop;
    }
    
    public static double getTopDiameter(double DTHPixels, double distanceToTop, int FOV, double imageWidthPixels)
    {
        double topWidthLength = (Math.tan(Math.toRadians(FOV / 2)) * distanceToTop) * 2;
        double topDiameter = (topWidthLength / imageWidthPixels) * DTHPixels;
        
        return topDiameter;
    }
}
