import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.print.*;
import javax.swing.*;
import java.nio.channels.FileChannel;
import java.io.IOException;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.io.File;

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.util.StringTokenizer;
import java.applet.*;
import java.net.URL;
import javax.swing.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;

import com.sun.pdfview.*;

public class PDFPrintable implements Printable {
  private com.sun.pdfview.PDFPage page;

  public PDFPrintable(PDFPage pdf) {
    page = pdf;
  }
  public int print(Graphics g, PageFormat format, int index) {
    int pagenum = index + 1;

    // fit the PDFPage into the printing area
    Graphics2D g2 = (Graphics2D) g;
    double pwidth = format.getImageableWidth();
    double pheight = format.getImageableHeight();

    double aspect = page.getAspectRatio();

    // handle page orientation matching
    double paperaspect = pwidth / pheight;
    if (paperaspect < 1.0) {
      switch (format.getOrientation()) {
        case PageFormat.REVERSE_LANDSCAPE:
        case PageFormat.LANDSCAPE:
          format.setOrientation(PageFormat.PORTRAIT);
          break;
        case PageFormat.PORTRAIT:
          format.setOrientation(PageFormat.LANDSCAPE);
          break;
      }
      pwidth = format.getImageableWidth();
      pheight = format.getImageableHeight();
      paperaspect = pwidth / pheight;
    }

    Rectangle imgbounds;
    int width;
    int height;
    if (aspect > paperaspect) {
      // paper is too tall / pdfpage is too wide
      height = (int) (pwidth / aspect);
      width = (int) pwidth;
    } else {
      // paper is too wide / pdfpage is too tall
      width = (int) (pheight * aspect);
      height = (int) pheight;
    }
    imgbounds = new Rectangle((int) format.getImageableX(),
        (int) format.getImageableY(),
        width, height);

    // render the page
    PDFRenderer pgs =
      new PDFRenderer(page, g2, imgbounds, null, null);
    try {
      page.waitForFinish();
      pgs.run();
    } catch (InterruptedException ie) {
    }
    return PAGE_EXISTS;
  }
}
