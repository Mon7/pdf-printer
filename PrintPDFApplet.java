import java.awt.print.*;
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

/**
 * This applet can be used to load and print a PDF from the browser with
 * minimal user interaction. The PDF is not displayed.
 *
 * The PDF can be specified as the "pdf" parameter to the applet (a URL
 * relative to the page containing the applet), or it can be set by
 * calling the "load" method.
 *
 * Once loaded, if the "callback" parameter was set the applet will call
 * the method specified by that parameter. At any point after that, the
 * "print" method can be called to open a print dialog. You can alter the
 * code to print immediately if you want to.
 *
 * This code is in the public domain
 */
public class PrintPDFApplet extends Applet {

  private com.sun.pdfview.PDFFile pdf;
  private String callback;

  public void init() {
    System.out.println("Start print job");

    callback = getParameter("callback");
    if (getParameter("pdf")!=null) {
      load(getParameter("pdf"));
    }
  }

  /**
   *      * Initialize the PrintRequestAttributeSet based on any default print options
   *           * set in the PDF, such as duplex, number of copies etc.
   *                */

  /**
   * Load a PDF
   * @param filename the URL of the filename (relative to the current document)
   */
  public void load(String filename) {
    System.out.println("Start load");
    try {
      URL url = new URL(getDocumentBase(), getParameter("pdf"));
      InputStream in = url.openStream();
      int size = 4096;
      int offset = 0;
      byte[] buffer = new byte[size];
      int read;
      do {
        read = in.read(buffer, size * offset, size);
        ++offset;
        byte[] new_buff = new byte[size * (1 + offset)];
        System.arraycopy(buffer, 0, new_buff, 0, buffer.length);
        buffer = new_buff;
      } while (read == size);
      System.out.println(buffer.length);
      ByteBuffer bb = ByteBuffer.allocate(buffer.length);
      bb.put(buffer, 0, buffer.length);
      System.out.println("new buffer");
      pdf = new PDFFile(bb);
      System.out.println("new pdf");
      if (callback!=null) {
        reportLoaded();
      }
      print();
    } catch (Exception e) {
      error(e.toString());
    }
  }

  /**
   * Return true if the PDF is loaded
   */
  public boolean isLoaded() {
    return pdf!=null;
  }

  /**
   * Called when the PDF is loaded to "call back" to the JavaScript to notify
   * it of the load. This requires the "mayscript" attribute on the applet and
   * the "plugin.jar" file supplied with the JRE to be in your classpath - if
   * you don't need this functionality you can remove the contents of this method.
   */
  private void reportLoaded() {
    try {
      // This method requires the "plugin.jar" supplied with the JRE
      // to be in your ClassPath.
      netscape.javascript.JSObject.getWindow(this).call(callback, new Object[0] );
    } catch (Throwable e) {
    }
  }

  public void print() {
    if (pdf==null) {
      error("PDF not loaded");
    } else {
      try {
        PrintRequestAttributeSet atts = new HashPrintRequestAttributeSet();
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;

        //initializePrintAttributes(atts, pdf);

        // Create Print Job.
        // We set the margins to 0, on the default 8.5 x 11 paper

        PDFPrintPage pages = new PDFPrintPage(pdf);

        PrinterJob pjob = PrinterJob.getPrinterJob();
        PageFormat pfDefault = PrinterJob.getPrinterJob().defaultPage();
        Paper defaultPaper = new Paper();
        defaultPaper.setImageableArea(0, 0, defaultPaper.getWidth(), defaultPaper.getHeight());
        pfDefault.setPaper(defaultPaper);
        pjob.setJobName("file");
        // validate the page against the chosen printer to correct
        // paper settings and margins
        pfDefault = pjob.validatePage(pfDefault);
        Book book = new Book();

        book.append(pages, pfDefault, pdf.getNumPages());
        pjob.setPageable(book);

        try {
          pjob.print();
        } catch (PrinterException exc) {
          System.out.println(exc);
        }
      } catch (Exception e) {
        error(e.toString());
      }
    }
  }

  private void error(String message) {
    System.out.println("ERROR: "+message);
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
}
