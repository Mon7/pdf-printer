import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.util.StringTokenizer;
import java.applet.*;

public class FindPrintersService extends Applet {
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
}
