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

//import com.sun.pdfview.*;
import org.apache.pdfbox.pdmodel.*;


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

 // private com.sun.pdfview.PDFFile pdf;
  private PDDocument pdf;

  /**
   * Load a PDF
   * @param filename the URL of the filename (relative to the current document)
   */
  public void load(String printerName) {
    try {
      URL url = new URL(getDocumentBase(), getParameter("pdf"));
      error("start load pdf");
      pdf = PDDocument.load(url.openStream());
      error("loaded pdf");
      /*InputStream in = url.openStream();
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
      ByteBuffer bb = ByteBuffer.allocate(buffer.length);
      bb.put(buffer, 0, buffer.length);
      pdf = new PDFFile(bb);
      java.util.Iterator iterator = pdf.getMetadataKeys();*/
      print(printerName);
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
      netscape.javascript.JSObject.getWindow(this).call(null, new Object[0] );
    } catch (Throwable e) {
    }
  }

  public String[] get_printers() {
    PrintRequestAttributeSet attr_set = new HashPrintRequestAttributeSet();
    DocFlavor doc_flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;

    PrintService[] services = PrintServiceLookup.lookupPrintServices(doc_flavor, attr_set);
    String[] names = new String[services.length];
    for(int i = 0; i < services.length; i++) {
      names[i] = services[i].getName();
    }
    return names;
  }

  private PrintService getPrintService(String printerName) {
    PrintRequestAttributeSet attr_set = new HashPrintRequestAttributeSet();
    DocFlavor doc_flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;

    PrintService[] services = PrintServiceLookup.lookupPrintServices(doc_flavor, attr_set);
    for(int i = 0; i < services.length; i++) {
      if(printerName.equals(services[i].getName())) {
        return services[i];
      }
    }
    return null;
  }
  public void print(String printerName) {
    if (pdf==null) {
      error("PDF not loaded");
    } else {
      try {
        PrintService printService = getPrintService(printerName);
        if (printService == null) {
          error("Fanns ingen matchande skrivare");
          return;
        }
        //initializePrintAttributes(atts, pdf);

        // Create Print Job.
        // We set the margins to 0, on the default 8.5 x 11 paper

        PrinterJob pjob = PrinterJob.getPrinterJob();
        pjob.setPrintService(printService);
        pjob.setJobName(pdf.getDocumentInformation().getTitle());

        /*PDFPrintPage pages = new PDFPrintPage(pdf);
        PageFormat pfDefault = pjob.defaultPage();
        Paper paper = new Paper();
        PDFPage page = pdf.getPage(0);
        paper.setSize(page.getWidth(), page.getHeight());
        error(Float.toString(pdf.getPageBox().getHeight()));
        error(Float.toString(page.getHeight()));
        error(Float.toString(page.getWidth()));
        paper.setImageableArea(0, 0, page.getHeight(), page.getWidth());
        pfDefault.setPaper(paper);
        pjob.setJobName(pdf.getMetadataKeys("Title"));
        // validate the page against the chosen printer to correct
        // paper settings and margins
        // Book book = new Book();
        // book.append(pages, pf, pdfFile.getNumPages());
        // pjob.setPageable(book);
        pjob.setPrintable(pages, pfDefault);*/
        error(pdf.getDocumentInformation().getTitle());

        pdf.print();
        pdf.close();
      } catch (Exception e) {
        error(e.toString());
      }
    }
  }

  private void error(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
}
