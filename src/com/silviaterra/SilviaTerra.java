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
    
    /**
     * Gets distance between the tree and camera lens
     * 
     * @param imageWidthPixels  Width of entire image in pixels
     * @param DBHPixels          Width of tree at breast height in pixels
     * @param DBHLength          Width of tree at breast height in centimeters
     * @param FOV                 Field of view in degrees
     * @return                     Distance to tree in centimeters
     */
    
    public static double getDistanceToTree(double imageWidthPixels, double DBHPixels, double DBHLength, int FOV)
    {
        double imageWidthLength = ((imageWidthPixels / DBHPixels) * DBHLength);
        System.out.println(imageWidthLength);
        System.out.println(Math.tan(Math.toRadians(30)));
        double distanceToTree = (imageWidthLength / 2) / (Math.tan(Math.toRadians(FOV/2)));
        
        return distanceToTree;
    }
    
    /**
     * Gets the height of the tree, from the horizontal of the first picture and the diagonal from the second picture
     * 
     * @param distanceToTree        Distance to tree in centimeters
     * @param verticalTiltAngle     Angle to top of tree in degrees
     * @return                        Height between the horizontal from the camera lens and the diagonal from the camera lens to the top of the tree
     * @see                           getDistanceToTree()
     */
    
    public static double getTreeHeight(double distanceToTree, double verticalTiltAngle)
    {
        double treeHeight = Math.tan(Math.toRadians(verticalTiltAngle)) * distanceToTree;
        
        return treeHeight;
    }
    
    /**
     * Gets the distance between the camera lens, when it is angle in the second picture, and the top of the tree
     * 
     * @param distanceToTree        Distance to tree in centimeters
     * @param treeHeight             Height of tree in centimeters
     * @return                        The diagonal distance from the camera lens to the top of the tree
     * @see                            getTreeHeight(), getDistanceToTree()
     */
    
    public static double getDistanceToTop(double distanceToTree, double treeHeight)
    {
        double distanceToTop = Math.sqrt(Math.pow(distanceToTree, 2) + Math.pow(treeHeight, 2));
        
        return distanceToTop;
    }
    
    /**
     * Gets diameter at the top tree, with foreshortening taken into account
     * 
     * @param DTHPixels            Width of top of tree in pixels  
     * @param distanceToTop       Distance to top of tree from camera lens when angled at top
     * @param FOV                  Field of view of camera in degrees
     * @param imageWidthPixels   Width of entire image in pixels
     * @return                      Scaled diameter of top of tree, foreshortening is taken into account
     * @see                         getDistanceToTop()
     */
    
    public static double getTopDiameter(double DTHPixels, double distanceToTop, int FOV, double imageWidthPixels)
    {
        double topWidthLength = (Math.tan(Math.toRadians(FOV / 2)) * distanceToTop) * 2;
        double topDiameter = (topWidthLength / imageWidthPixels) * DTHPixels;
        
        return topDiameter;
    }
}
