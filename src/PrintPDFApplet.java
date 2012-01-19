import netscape.javascript.*;
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

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;

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
  public void init() {
    String printer = getParameter("printer");
    loadPdf(printer);
  }
  /**
   * Load a PDF
   * @param filename the URL of the filename (relative to the current document)
   */
  public void loadPdf(String printerName) {
    try {
      URL url = new URL(getDocumentBase(), getParameter("pdf"));
      PDDocument pdf = PDDocument.load(url, true);
      print(pdf, printerName);
    } catch (Exception e){
      error(e.toString());
    }
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
  private void reportProgress() {
    String callback = getParameter("callback");
    if (callback != null)
      netscape.javascript.JSObject.getWindow(this).call(callback, null);
  }
  public void print(PDDocument pdf, String printerName) {
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
      pjob.setJobName("AD");

      pdf.silentPrint(pjob);
      pdf.close();
      reportProgress();
    } catch (Exception e){
      error(e.toString());
    }
  }

  private void error(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
}
