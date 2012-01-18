task :build => :clean do
  system 'unzip libs/pdfbox-app-1.6.0.jar -d target'
  system 'javac -d target -cp "libs/*" src/*.java'
  system 'cp src/*.properties target/'
  system 'cd target && jar cvf ../applets/PrintPDFApplet.jar *'
  #exec 'jarsigner -keystore ~/myKeyStore applets/PrintPDFApplet.jar me'
end

task :clean do
  system 'rm -rf target'
  system 'rm -rf applets'
  system 'mkdir target'
  system 'mkdir applets'
end

task :s do
  exec 'python -m SimpleHTTPServer'
end
