This file provides a high-level explanation of SeamCarving implementation.It has also the additional information that helps the user to run the program.



//************************************/Implementation of Seam Carving/*****************************************//

Seam carving is used to change the size of an image by removing the least visible pixels in the image.  The visibility of a pixel can be defined using an energy function. Seam carving can be done by finding one pixel wide paths of lowest energy pixels crossing the image from top to bottom or from left to right and removing the path (seam).
The implementation contains of two files:  1.ImageProcessorDriver.java and 2.ImageProcessor.java
The program also uses the following class files:
ImageProcessorGUI.class,ImageProcessorGUI$1.class,ImageProcessorGUI$ControlActionListener, ImageProcessorDriver.class and PgmImageViewer.class

1.ImageProcessorDriver.java
This is the driver program for the image processing project
2.ImageProcessor.java
ImageProcessor reads the file using readImage() function, process the image and displays the output on the image viewer.Processing includes stretching, fixedRatio, cropping  and seamCarving.
SeamCarving process takes verticalSeamsToBeRemoved and horizontalSeamsToBeRemoved as the input and based on the no of seams to be removed,for each iteration :
calculatePixelEnergy() function calculates Pixel energy for each pixel.
calculateCumulativeMinEnergy() function calculates minimum cumulative energy by using the pixel energy for each pixel.
seamCarving() function removes each vertical/horizontal seam from the image.
updateImg() function is used to update the pixels to current image.
saveImage() function saves the processed image in a pgm file named : original_image_file_name_processed.pgm

//************************************/Execution of Seam Carving/*****************************************//

The project is developed using Eclipse in Java. The program also uses the following class files:
ImageProcessorGUI.class,ImageProcessorGUI$1.class,ImageProcessorGUI$ControlActionListener, ImageProcessorDriver.class and PgmImageViewer.class

Compilation:
javac ImageProcessorDriver.java

Execution:

java ImageProcessorDriver filename.pgm verticalSeamsToBeRemoved horizontalSeamsToBeRemoved

//Input filename and also vertical and horizantal seams to be removed by command line.
//an applet window opens,load image and input the seams and click on process button. 
//output will be a processed image with filename as filename_processed.pgm 

Sample Execution:

DEEKSHITHs-MacBook-Air:~ DEEKSHITH$ javac ImageProcessorDriver.java
DEEKSHITHs-MacBook-Air:~ DEEKSHITH$ java ImageProcessorDriver test.pgm 1 0
Processing Original Image..
processing method = seamCarving_(1,0)
  verticalSeamsToBeRemoved= 1 horizontalSeamsToBeRemoved = 0
Output Image path is: /Users/DEEKSHITH/./test_processed.pgm$ 
