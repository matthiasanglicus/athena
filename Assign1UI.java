import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class Assign1UI extends JFrame {
    
	private JPopupMenu viewportPopup;
    
	public Assign1UI() {
		super("COMP 7502 - Assignment 1");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane scroller = new JScrollPane(new ImagePanel());
		this.add(scroller);
		this.setSize(500, 500);
		this.setVisible(true);
	}
    
	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Assign1UI();
			}
		});
	}
    
	private class ImagePanel extends JPanel implements MouseListener, ActionListener {
		private BufferedImage img;
		private Assign1 imgProcessor;
        
		public ImagePanel() {
			imgProcessor = new Assign1();
			this.addMouseListener(this);
		}
        
		public Dimension getPreferredSize() {
			if (img != null) {
				return (new Dimension(img.getWidth(), img.getHeight()));
			} else {
				return (new Dimension(0, 0));
			}
		}
        
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (img != null)
				g.drawImage(img, 0, 0, this);
		}
        
		private void showPopup(MouseEvent e) {
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			viewportPopup = new JPopupMenu();
            
			JMenuItem openImageMenuItem = new JMenuItem("open image ...");
			openImageMenuItem.addActionListener(this);
			openImageMenuItem.setActionCommand("open image");
			viewportPopup.add(openImageMenuItem);
            
			JMenuItem loadDefaultImageMenuItem = new JMenuItem("load default image");
			loadDefaultImageMenuItem.addActionListener(this);
			loadDefaultImageMenuItem.setActionCommand("load default image");
			viewportPopup.add(loadDefaultImageMenuItem);
			
			JMenuItem loadLennaImageMenuItem = new JMenuItem("load lenna");
			loadLennaImageMenuItem.addActionListener(this);
			loadLennaImageMenuItem.setActionCommand("load lenna");
			viewportPopup.add(loadLennaImageMenuItem);
			
			viewportPopup.addSeparator();
			
			JMenuItem fftMenuItem = new JMenuItem("show Fourier spectrum");
			fftMenuItem.addActionListener(this);
			fftMenuItem.setActionCommand("show Fourier spectrum");
			viewportPopup.add(fftMenuItem);
			
			viewportPopup.addSeparator();
			
			JMenuItem filterMenuItem = new JMenuItem("filter image");
			filterMenuItem.addActionListener(this);
			filterMenuItem.setActionCommand("filter image");
			viewportPopup.add(filterMenuItem);
			
			viewportPopup.addSeparator();
            
			JMenuItem exitMenuItem = new JMenuItem("exit");
			exitMenuItem.addActionListener(this);
			exitMenuItem.setActionCommand("exit");
			viewportPopup.add(exitMenuItem);
            
			viewportPopup.show(e.getComponent(), e.getX(), e.getY());
		}
        
		@Override
		public void mouseClicked(MouseEvent e) {
		}
        
		@Override
		public void mouseEntered(MouseEvent e) {
		}
        
		@Override
		public void mouseExited(MouseEvent e) {
		}
        
		@Override
		public void mousePressed(MouseEvent e) {
			if (viewportPopup != null) {
				viewportPopup.setVisible(false);
				viewportPopup = null;
			} else {
				showPopup(e);
			}
		}
        
		@Override
		public void mouseReleased(MouseEvent e) {
		}
        
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("open image")) {
				final JFileChooser fc = new JFileChooser();
				FileFilter imageFilter = new FileNameExtensionFilter("Image files", "bmp", "gif", "jpg");
				fc.addChoosableFileFilter(imageFilter);
				fc.setDragEnabled(true);
				fc.setMultiSelectionEnabled(false);
				fc.showOpenDialog(this);
				File file = fc.getSelectedFile();
				try {
					img = ImageIO.read(file);
					img = colorToGray(img);
				} catch (Exception ee) {
					//ee.printStackTrace();
				}
			} else if (e.getActionCommand().equals("load default image")) {
				try {
					img = ImageIO.read(new URL("http://www.cs.hku.hk/~sdirk/georgesteinmetz.jpg"));
					img = colorToGray(img);
					
				} catch (Exception ee) {
					JOptionPane.showMessageDialog(this, "Unable to fetch image from URL", "Error",
                                                  JOptionPane.ERROR_MESSAGE);
					ee.printStackTrace();
				}
			} else if (e.getActionCommand().equals("load lenna")) {
				try {
					img = ImageIO.read(new URL("http://www.cs.hku.hk/~sdirk/lenna.png"));
					img = colorToGray(img);
				} catch (Exception ee) {
					JOptionPane.showMessageDialog(this, "Unable to fetch image from URL", "Error",
                                                  JOptionPane.ERROR_MESSAGE);
					ee.printStackTrace();
				}
			} else if (e.getActionCommand().equals("show Fourier spectrum")) {
				if (img!=null) {
					byte[] original = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
					Complex[] copy = getComplexCopy(original);
					imgProcessor.showFourierSpectrum(copy, img.getWidth(), img.getHeight());
					assignComplexToByte(original, copy);
				}
			} else if (e.getActionCommand().equals("filter image")) {
				if (img != null) {
					byte[] original = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
					Complex[] copy = getComplexCopy(original);
					imgProcessor.filterImage(copy, img.getWidth(), img.getHeight());
					assignComplexToByte(original, copy);
				}
			}
			else if (e.getActionCommand().equals("exit")) {
				System.exit(0);
			}
			viewportPopup = null;
			this.updateUI();
            
		}
		
		public BufferedImage colorToGray(BufferedImage source) {
			//padding
			int newWidth = (int)Math.pow(2, Math.ceil((Math.log(img.getWidth())/Math.log(2))));
			int newHeight = (int)Math.pow(2, Math.ceil((Math.log(img.getHeight())/Math.log(2))));
	        BufferedImage returnValue = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
	        Graphics g = returnValue.getGraphics();
	        g.drawImage(source, 0, 0, null);
	        g.dispose();
	        return returnValue;
	    }
        
		private Complex[] getComplexCopy(byte[] byteArrayIn) {
			Complex[] returnValue = new Complex[img.getWidth()*img.getHeight()];
			for (int x=0;x<img.getHeight();x++) {
				for (int y=0;y<img.getWidth();y++) {
					returnValue[x*img.getWidth()+y] = new Complex();
					returnValue[x*img.getWidth()+y].setReal((byteArrayIn[x*img.getWidth()+y] & 0xFF));
				}
			}
			return returnValue;
		}
        
		private void assignComplexToByte(byte[] img2, Complex[] img1) {
			for (int x = 0; x < img.getHeight(); x++) {
				for (int y = 0; y < img.getWidth(); y++) {
                    double p = img1[x * img.getWidth() + y].getReal();
                    p = Math.max(0.0, Math.min(255.0, p)); // enforce bounds
                    img2[x * img.getWidth() + y] = (byte) p;
				}
			}
		}
		
	}
}