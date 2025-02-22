// This class uses the Color class, which is part of a package called awt,
// which is part of Java's standard class library.
import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] imageOut;

		// Tests the horizontal flipping of an image:
		imageOut = flippedHorizontally(tinypic);
		System.out.println();
		print(imageOut);

		imageOut = flippedVertically(tinypic);
		System.out.println();
		//print(imageOut);
		Color c1 = new Color (100,40,100);
		Color c2 = new Color (200,20,40);
		double a = 0.25;
		print(blend(c1, c2, a));
		//print(luminance(c));
		System.out.println();
		//imageOut = grayScaled(tinypic);
		imageOut = scaled(tinypic, 3,5);
		print(imageOut);

		
		//// Write here whatever code you need in order to test your work.
		//// You can reuse / overide the contents of the imageOut array.
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		while(!in.isEmpty())
		{
			for (int i = 0; i<image.length; i++)
			{
				for (int j = 0; j<image[i].length; j++)
				{
					int pixel1 = in.readInt();
					int pixel2 = in.readInt();
					int pixel3 = in.readInt();
					Color new_color = new Color (pixel1, pixel2, pixel3);
					image[i][j] = new_color;
				}
			}	
		}
		// Reads the RGB values from the file, into the image array. 
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		//// Replace the following statement with your code.
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) 
	{
		for (int i = 0; i<image.length; i++)
			{
				for (int j = 0; j<image[i].length; j++)
				{
					print(image[i][j]);
				}
				System.out.println();
			}		
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		Color[][] flipped_image = new Color[image.length][image[0].length]; //creates a new 2D array to store the new image
		for (int i = 0; i<image.length; i++)
		{	
			int index = 0;
			for (int j = image[i].length-1; j>=0; j--) //starts running from the last column of every row
			{
				flipped_image[i][index] = image[i][j];
				index++;
			}
		}
		return flipped_image;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		Color[][] flipped_image = new Color[image.length][image[0].length]; //creates a new 2D array to store the new image
		int row = 0;
		for (int i = image.length-1; i>=0; i--)
		{	
			for (int j = 0; j<image[i].length; j++)
			{
				flipped_image[row][j] = image[i][j];

			}
			row++;
		}
		return flipped_image;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	public static Color luminance(Color pixel) {
		double r = pixel.getRed() *0.299;
		double g = pixel.getGreen() * 0.587;
		double b = pixel.getBlue() * 0.114;
		int rgb = (int)(r+g+b);
		Color new_pixel = new Color (rgb, rgb, rgb);
		return new_pixel;
	}
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		Color[][] grey_image = new Color[image.length][image[0].length];
		for (int i = 0; i<image.length; i++)
		{
			for (int j=0; j<image[i].length; j++)
			{
				grey_image[i][j] = luminance(image[i][j]);
			}
		}
		return grey_image;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int width, int height) 
	{
		int w0 = image[0].length; //gets the width of the source array
		int h0 = image.length; //gets the hight of the source array
		Color[][] scaled_image = new Color[height][width];
		
		for (int i = 0; i <scaled_image.length; i++)
		{
			for (int j = 0; j<scaled_image[i].length; j++)
			{
				int row = (int) ((i * (double)(h0) / (double) (height)));
				int col = (int) (j * ((double)(w0) / (double) (width)));
				scaled_image[i][j] = image[row][col];
			}
		}
		return scaled_image;
	}
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int red = (int)(alpha * c1.getRed() + (1 - alpha) * c2.getRed());
		int green = (int)(alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
		int blue = (int)(alpha * c1.getBlue() + (1 - alpha) * c2.getBlue());
		Color blended = new Color(red, green, blue);
		return blended;
	}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color[][] blended = new Color[image1.length][image1[0].length];

		for (int i = 0; i < blended.length; i++)
		{
			for (int j = 0; j < blended[i].length; j++)
			{
				Color c1 = image1[i][j];
				Color c2 = image2[i][j];
				blended[i][j] = blend(c1, c2, alpha);
			}
		}
		return blended;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
	
		if (source.length != target.length || source[0].length != target[0].length)
		{
            target = scaled(target, source[0].length, source.length);
		}
		for (int i = 0; i < n; i++)
		{
			double alpha = (double)((n - i + 0.0) / n); 
			display(blend(source, target, alpha));
		    StdDraw.pause(500); 
		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

