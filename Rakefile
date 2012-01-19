task :build => :clean do
  system 'unzip -q libs/pdfbox-app-1.6.0.jar -d target'
  system 'javac -d target -cp "libs/*" src/*.java'
  next if $?.exitstatus != 0
  system 'cp src/*.properties target/'
  system 'cd target && jar cvf ../applets/PrintPDFApplet.jar * > /dev/null'
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
