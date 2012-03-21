import netscape.javascript.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.applet.*;
import java.net.URL;
import javax.swing.JOptionPane;

public class PrintPDFApplet extends Applet {
  public void init() {
    try {
      String printerName = getParameter("printer");
      DocFlavor flavor = DocFlavor.URL.PDF;
      PrintRequestAttributeSet attribs = new HashPrintRequestAttributeSet();
      attribs.add(MediaSizeName.ISO_A4);
      PrintService printService = getPrintService(printerName, flavor, attribs);
      if (printService == null) {
        error("Fanns ingen matchande skrivare");
        return;
      }
      URL url = new URL(getDocumentBase(), getParameter("pdf"));
      Doc pdfDoc = new SimpleDoc(url, flavor, null);
      DocPrintJob printJob = printService.createPrintJob();

      printJob.print(pdfDoc, attribs);
      reportProgress();
    } catch (Exception e){
      error(e.toString());
    }
  }

  private PrintService getPrintService(String printerName, DocFlavor flavor, PrintRequestAttributeSet attribs) {
    PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, attribs);
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

  private void error(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
  }
}
