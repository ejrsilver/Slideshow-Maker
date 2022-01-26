package slideshowmaker;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.imageio.ImageIO;

/**
 *
 * @author ethansilver
 */
public class SlideshowMaker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        // Setting the options.
        String targetSchool = "ESS";
        String doc = "temp.csv";

        // If there's a consent column.
        boolean consent = false;

        // If awards are listed.
        boolean options = false;

        DecimalFormat df = new DecimalFormat("000");

        File[] fla = new File("/Users/ethansilver/Downloads/ESS/Slide7-86").listFiles((File dir, String name) -> !name.equals(".DS_Store"));

        ArrayList<String> arPhotos = new ArrayList<>();
        ArrayList<String> arFiles = new ArrayList<>();
        ArrayList<String> arGrads = new ArrayList<>();

        String sL;

        for (File f : fla) {
            arPhotos.add(f.getName().substring(0, f.getName().length() - 4));
        }

        BufferedReader br = new BufferedReader(new FileReader("/Users/ethansilver/Desktop/" + doc));

        while ((sL = br.readLine()) != null) {
            arGrads.add(sL);
        }

        br.close();

        Color c = new Color(133, 205, 242);

        Color green = new Color(50, 112, 67);

        // Open a JPEG file, load into a BufferedImage.
        BufferedImage img = ImageIO.read(new File("background.png"));

        BufferedImage img2 = ImageIO.read(new File("background.png"));

        // Obtain the Graphics2D context associated with the BufferedImage.
        Graphics2D g = img.createGraphics();
        Graphics2D g2 = img2.createGraphics();

        // The logo/placeholder object for student picture.
        BufferedImage bi = ImageIO.read(new File("Loreal.png"));

        for(String s : arGrads) {
            System.out.println(s.split(",").length);
        }





        // For each grad.
        for (int x = 0; x < arGrads.size(); x++) {

            // Create a new directory for the curent grad's frames.
            String[] cmd = {"mkdir", "OUTPUT/OUT" + df.format(x)};
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.start();

            // Get the grad's name.
            String[] sName = {arGrads.get(x).split(",")[1], arGrads.get(x).split(",")[0]};

            ArrayList<String> sAwards = new ArrayList<>();

            if(options) {
                // Array of student awards
                String[] sA;

                if(consent) {
                    sA = Arrays.copyOfRange(arGrads.get(x).split(","), 3, arGrads.get(x).split(",").length);
                }
                else {
                    sA = Arrays.copyOfRange(arGrads.get(x).split(","), 2, arGrads.get(x).split(",").length);
                }

                for (String s : sA) {
                    if (!s.equals("")) {

                        if(s.length() > 44) {

                            String q = "";

                            q = s.split(" ")[0];

                            for(int w = 1; w < s.split(" ").length/3; w++) {
                                q+= " " + s.split(" ")[w];
                            }
                            sAwards.add(q);

                            q = s.split(" ")[s.split(" ").length/3];

                            for(int w = s.split(" ").length/3 + 1; w < 2*s.split(" ").length/3; w++) {
                                q+= " " + s.split(" ")[w];
                            }
                            sAwards.add(q);

                            q = s.split(" ")[2*s.split(" ").length/3];

                            for(int w = 2*s.split(" ").length/3 + 1; w < s.split(" ").length; w++) {
                                q+= " " + s.split(" ")[w];
                            }
                            sAwards.add(q);

                        }
                        else if(s.length() > 23) {

                            String q = "";

                            q = s.split(" ")[0];

                            for(int w = 1; w < s.split(" ").length/2; w++) {
                                q+= " " + s.split(" ")[w];
                            }
                            sAwards.add(q);

                            q = s.split(" ")[s.split(" ").length/2];

                            for(int w = s.split(" ").length/2 + 1; w < s.split(" ").length; w++) {
                                q+= " " + s.split(" ")[w];
                            }
                            sAwards.add(q);
                        }
                        else {
                            sAwards.add(s);
                        }
                    }
                }
            }

            // Enable text anti-aliasing.
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Whether the student's photo goes in or not.
            bi = ImageIO.read(new File("Loreal.png"));
            Collections.sort(arPhotos);

            for (int q = 0; q < fla.length; q++) {


                // If the photo is a match.
                if (fla[q].getName().substring(0, fla[q].getName().length() - 4).equals(arGrads.get(x).split(",")[0])) {

                    // If there is a consent column and the student gave consent or there isn't a consent column.
                    if(!consent || (consent && !arGrads.get(x).split(",")[2].equals("X"))) {
                        bi = ImageIO.read(fla[q]);
                    }
                }
            }

            if (bi.getWidth() > bi.getHeight()) {

                AffineTransform tx = new AffineTransform();

                System.out.println(bi.getWidth() + "," + bi.getHeight());

                int h = bi.getHeight();
                int w = bi.getWidth();

                tx.rotate(Math.PI / 2, w, w);

                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

                bi = op.filter(bi, null);

                bi = bi.getSubimage(2 * w - h, 0, h, w);

            }






            // For the left-side slides.

            // When re-doing missing photos (Integer.parseInt(arGrads.get(x).split(",")[arGrads.get(x).split(",").length-1])%2==0)
            //if (x%2==0) {

                // For each frame.
                for (int y = 0; y < 150; y++) {

                    // Add the right-side background.
                    g.drawImage(ImageIO.read(new File("background.png")), 0, 0, null);

                    // Add the student picture/KCVI logo.
                    g.drawImage(bi, 118, 90, 690, 900, null);

                    // Add the outline.
                    g.drawImage(ImageIO.read(new File("ESS-Portrait.png")), 118, 90, 690, 900, null);

                    // After the first 15 frames until the 30th frame.
                    if (y > 15 && y < 30) {

                        // Set transparency.
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .066f * (y - 15)));

                        // Set font to Serif.
                        g.setFont(new Font("Serif", Font.PLAIN, 90));

                        g.setColor(Color.black);

                        // Draw in the grad's name.
                        if((sName[0] + " " + sName[1]).length() > 23) {
                            g.drawString(sName[0], 1440 - 16 * y, 340);
                            g.drawString(sName[1], 1440 - 16 * y, 440);
                        }
                        else{
                            g.drawString((sName[0] + " " + sName[1]), 1440 - 16 * y, 340);
                        }

                        // If the school has Awards.
                        if (options) {

                            // Set font to Sans-Serif.
                            g.setFont(new Font("Sans-Serif", Font.PLAIN, 55));

                            // Set the colour to light blue.
                            g.setColor(c);

                            // Draw in each award.
                            for (int s = 0; s < sAwards.size(); s++) {
                                g.drawString(sAwards.get(s), 1540 - 16 * y, 700 + 60 * s);
                            }

                            // Reset transparency and color to 100% and white.
                            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                            g.setColor(Color.black);
                        }
                    }
                    // Including and after the 30th frame. (After animation completes)
                    else if (y >= 30) {

                        g.setFont(new Font("Serif", Font.PLAIN, 90));

                        g.setColor(Color.black);
                        // Draw in the grad's name.
                        if((sName[0] + " " + sName[1]).length() > 23) {
                            g.drawString(sName[0], 960, 340);
                            g.drawString(sName[1], 960, 440);
                        }
                        else {
                            g.drawString((sName[0] + " " + sName[1]), 960, 340);
                        }

                        if (options) {
                            g.setColor(c);
                            g.setFont(new Font("Sans-Serif", Font.PLAIN, 55));

                            // Draw in each award, but now the x coordinate is final.
                            for (int s = 0; s < sAwards.size(); s++) {
                                g.drawString(sAwards.get(s), 1060, 700 + 60 * s);
                            }

                            // Reset colour to black.
                            g.setColor(Color.black);
                        }
                    }

                    // For the first 30 frames.
                    if (y < 30) {

                        // Animate drawing the green line.
                        g.setColor(green);
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                        g.fillRect(1920 - y * 33, 520, 1920 - y * 33, 12);
                    }
                    // After the first 30 frames.
                    else {

                        // Draw in the white line.
                        g.setColor(green);
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                        g.fillRect(960, 520, 1000, 12);
                        g.fillRect(960, 520, 12, 50);
                    }

                    //if (options) {
                        if (y < 25) {
                            // Didn't feel like rearranging my code
                        }
                        // Animate the Loreal fading into view.
                        else if (y >= 25 && y <= 35) {
                            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f * (y - 25)));
                            g.drawImage(ImageIO.read(new File("Loreal.png")), null, 1000, 560);
                            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                        }
                        // Draw the Loreal at 100% opacity.
                        else {
                            g.drawImage(ImageIO.read(new File("Loreal.png")), null, 1000, 560);
                        }
                    //}

                    // Output the current frame.
                    ImageIO.write(img, "PNG", new File("OUTPUT/OUT" + df.format(x) + "/" + df.format(y) + ".PNG"));
                }
            //}
            // For the right-side slides.
            
            else {
                for (int y = 0; y < 150; y++) {
                    // Add the left-side background.
                    g.drawImage(ImageIO.read(new File("LSB-Grad-Background-2.png")), 0, 0, null);

                    // Add the student picture/KCVI logo.
                    g.drawImage(bi, 1112, 90, 690, 900, null);

                    // Pretty much the same as before, but I'll comment the major differences.
                    if (y > 15 && y < 30) {
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .066f * (y - 15)));
                        g.setFont(new Font("Serif", Font.PLAIN, 90));

                        // Since this is the right side, the width of the name must be accounted for when drawing it on.
                        if((sName[0] + " " + sName[1]).length() > 23) {
                            g.drawString(sName[0], (720 - g.getFontMetrics().stringWidth(sName[0])) + 16 * (y - 15), 340);
                            g.drawString(sName[1], (720 - g.getFontMetrics().stringWidth(sName[1])) + 16 * (y - 15), 440);
                        }
                        else{
                            g.drawString((sName[0] + " " + sName[1]), (720 - g.getFontMetrics().stringWidth((sName[0] + " " + sName[1]))) + 16 * (y - 15), 340);
                        }

                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

                        if (options) {
                            g.setFont(new Font("Sans-Serif", Font.PLAIN, 55));
                            g.setColor(c);
                            for (int s = 0; s < sAwards.size(); s++) {
                                // The same applies for drawing each award on the right side.
                                g.drawString(sAwards.get(s), 580 - g.getFontMetrics().stringWidth(sAwards.get(s)) + 16 * (y - 15), 700 + 60 * s);
                            }
                            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                            g.setColor(new Color(255, 255, 255));
                        }

                    } else if (y >= 30) {
                        g.setFont(new Font("Serif", Font.PLAIN, 90));

                        // Because name widths vary, even the final values are affected by width.
                        if((sName[0] + " " + sName[1]).length() > 23) {
                            g.drawString(sName[0], 960 - g.getFontMetrics().stringWidth(sName[0]), 340);
                            g.drawString(sName[1], 960 - g.getFontMetrics().stringWidth(sName[1]), 440);
                        }
                        else{
                            g.drawString((sName[0] + " " + sName[1]), 960 - g.getFontMetrics().stringWidth((sName[0] + " " + sName[1])), 340);
                        }

                        if (options) {
                            g.setColor(c);
                            g.setFont(new Font("Sans-Serif", Font.PLAIN, 55));
                            for (int s = 0; s < sAwards.size(); s++) {
                                // Because name widths vary, even the final values are affected by width.
                                g.drawString(sAwards.get(s), 860 - g.getFontMetrics().stringWidth(sAwards.get(s)), 700 + 60 * s);
                            }
                            g.setColor(new Color(255, 255, 255));
                        }
                    }

                    // The rest is exactly the same just with different coordinates and the white line is animated in the opposite direction.
                    if (y < 30) {
                        g.setColor(Color.white);
                        g.fillRect(0, 520, y * 33, 3);
                    } else {
                        g.fillRect(0, 520, 960, 3);
                        g.fillRect(960, 520, 3, 50);
                    }

                    if (options) {
                        if (y < 25) {

                        } else if (y >= 25 && y <= 35) {
                            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .1f * (y - 25)));
                            g.drawImage(ImageIO.read(new File("Loreal.png")), null, 600, 560);
                            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                        } else {
                            g.drawImage(ImageIO.read(new File("Loreal.png")), null, 600, 560);
                        }
                    }
                    ImageIO.write(img, "PNG", new File("OUTPUT/OUT" + df.format(x) + "/" + df.format(y) + ".PNG"));
                }
            }

        }
    }
}
