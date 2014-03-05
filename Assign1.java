/**
 * Assignment 1, COMP7502 Computer Vision 
 * Due : 7. November 2013, 23:00pm 
 * Name: S. Matthew English 
 * University number: 2013950101
 * 
 */
public class Assign1 {

	private double maX = 0.0;
	private double miN = 0.0;

	/**
	 * This method is called when you click on "filter image". After this method
	 * has been called, the real part of f will be visualized.
	 * 
	 * @param f- 2D input and output image represented by a (row-major) array
	 *            of complex numbers
	 * @param width
	 * @param height
	 */
	public void filterImage(Complex[] f, int width, int height) {
		// No need to modify!
		Complex[] F = new Complex[width * height];
		for (int i = 0; i < F.length; i++)
			F[i] = new Complex();
		center(f, width, height); // step 1
		fft2D(f, F, width, height); // step 2
		blpf(F, width, height); // step 3
		ifft2D(F, f, width, height);// step 4 and 5
		center(f, width, height); // step 6
	}

	/**
	 * This method is called when you click on "show Fourier spectrum". After
	 * this method has been called, the real part of F will be visualized.
	 * 
	 * @param f- 2D input and output image represented by a (row-major) array
	 *            of complex numbers
	 * @param width
	 * @param height
	 */
	public void showFourierSpectrum(Complex[] f, int width, int height) {
		// No need to modify!
		Complex[] F = new Complex[width * height];
		for (int i = 0; i < F.length; i++)
			F[i] = new Complex();
		center(f, width, height);
		fft2D(f, F, width, height);
		for (int i = 0; i < F.length; i++)
			f[i].setReal(Math.sqrt(Math.pow(F[i].getReal(), 2)
					+ Math.pow(F[i].getImaginary(), 2)));
		log(f);
		scale(f);
	}

	/**
	 * This method centers (and 'un-centers') an image prior and post
	 * processing.
	 * 
	 * @param f- 2D input and output image represented by an array of complex
	 *            numbers
	 * @param width
	 * @param height
	 */
	private void center(Complex[] f, int width, int height) {

		for (int i = 0; i < f.length; i++) {
			int x = i % width;
			int y = i / width;
			f[i].setReal(f[i].getReal() * Math.pow(-1, (x + y)));
		}

	}

	/**
	 * This method performs the 1D fast Fourier transform from input data fx, it
	 * outputs the frequency coefficients in the array Fu.
	 * 
	 * @param fx- 1D input array of data
	 * @param Fu- 1D output array of data
	 * @param twoK- the total number of data to be considered in fx
	 * @param stride- the number of data to be skipped when reading the next data
	 *            in fx
	 * @param start- the location to start reading data in fx
	 */
	private void fft1D(Complex[] fx, Complex[] Fu, int twoK) {

		// base case
		if (twoK <= 1) {
			Fu[0] = fx[0];
			return;
		}

		Complex[] even = new Complex[twoK / 2];

		for (int k = 0; k < twoK / 2; k++) {
			even[k] = fx[2 * k];
		}

		Complex[] odd = new Complex[twoK / 2];
		for (int k = 0; k < twoK / 2; k++) {
			odd[k] = fx[2 * k + 1];
		}

		Complex[] q = new Complex[twoK / 2];
		fft1D(even, q, twoK / 2);

		Complex[] r = new Complex[twoK / 2];
		fft1D(odd, r, twoK / 2);

		for (int k = 0; k < twoK / 2; k++) {
			double kth = -2 * k * Math.PI / twoK;
			Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
			Fu[k] = q[k].plus(wk.mul(r[k]));
			Fu[k + twoK / 2] = q[k].minus(wk.mul(r[k]));
		}

	}

	/**
	 * This method performs the 2D Fast Fourier Transform.
	 * 
	 * @param f- 2D input image represented by an array of complex numbers
	 * @param F- 2D output image represented by an array of complex numbers
	 * @param width
	 * @param height
	 */
	private void fft2D(Complex[] f, Complex[] F, int width, int height) {

		Complex[] riz = new Complex[width];
		Complex[] container = new Complex[width];

		for (int i = 0; i < height; i++) {
			for (int ii = 0; ii < width; ii++) {
				riz[ii] = f[i * width + ii];
			}

			fft1D(riz, container, width);

			for (int r = 0; r < width; r++) {
				F[i * width + r] = container[r];
			}

		}

		Complex[] CLMriz = new Complex[height];
		Complex[] CLMcontainer = new Complex[height];

		for (int i = 0; i < width; i++) {
			for (int ii = 0; ii < height; ii++) {
				CLMriz[ii] = F[ii * width + i];
			}

			fft1D(CLMriz, CLMcontainer, height);

			for (int c = 0; c < height; c++) {
				F[c * width + i] = CLMcontainer[c];
			}
		}

	}

	/**
	 * This method performs the inverse 2D Fast Fourier Transform.
	 * 
	 * @param F
	 *            - 2D input image represented by an array of complex numbers
	 * @param f
	 *            - 2D output image represented by an array of complex numbers
	 * @param width
	 * @param height
	 */
	private void ifft2D(Complex[] F, Complex[] f, int width, int height) {
	
	
		Complex[] riz = new Complex[width*height];
		Complex[] container = new Complex[width*height];


			for (int ii = 0; ii < width*height; ii++)
				container[ii] = F[ii].getConjugate();
		
			fft2D(container, riz, width, height);

			for (int r = 0; r < width*height; r++) {
			f[r] = riz[r].getConjugate();
			f[r] = f[r].div((double)width*height); 
			 
			}
	}
	
	


	/**
	 * This method performs scaling so that all values lie within the range [0,
	 * 255]. The result is saved in the real component of F.
	 * 
	 * @param F
	 *            - the input and output image represented by an array of
	 *            complex numbers
	 */
	private void scale(Complex[] F) {
	
		for (int i = 0; i < F.length; i++) {
			F[i].setReal((double)255 * F[i].getReal() / (maX - miN));
			
			
		}

	}

	/**
	 * This method performs the log transformation. The result is saved in the
	 * real component of F.
	 * 
	 * @param F- the input and output images are represented by an array of
	 *            complex numbers
	 */
	private void log(Complex[] F) {

		F[0].setReal(Math.log10(1 + F[0].getReal()));
		maX = F[0].getReal();

		miN = F[0].getReal();

		for (int i = 1; i < F.length; i++) {
			F[i].setReal(Math.log10(1 + F[i].getReal()));
			if (F[i].getReal() > maX) {
				maX = F[i].getReal();
			}
			if (F[i].getReal() < miN) {
				miN = F[i].getReal();
			}

		}  

	}

	/**
	 * This method performs second order ButterWorth low pass filtering.
	 * 
	 * @param F- the input and output Fourier transformed image
	 * @param width
	 * @param height
	 */
	private void blpf(Complex[] F, int width, int height) {
		int centerY = width / 2;
		int centerX = height / 2;
		int D0 = 30;
		
						
		double value = 0;
        
		for (int y = 0; y < width; y++)
                {
                        for (int x = 0; x < height; x++)
                        {
                        int distance = (int)(Math.pow(x-centerX, 2)+Math.pow(y-centerY, 2));
                        value = distance/Math.pow(D0, 2);
                        value = value+1;
                        value = 1/value;

                        F[x*width+y] = F[x*width+y].mul(value); 
                                
		      			}
               }		
	}	
}














