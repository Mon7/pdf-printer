task :build do
  system 'javac -cp "p/*" PrintPDFApplet.java'
  system 'javac FindPrintersService.java'
  system 'mv PrintPDFApplet.class pdfbox-app/'
  system 'mv FindPrintersService.class pdfbox-app/'
  system 'cd pdfbox-app && jar cvf ../applets/PrintPDFApplet.jar *'
  exec 'jarsigner -keystore ~/myKeyStore applets/PrintPDFApplet.jar me'
end
