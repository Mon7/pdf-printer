task :build => :clean do
  system 'javac -d target -cp "libs/*" src/*.java'
  next if $?.exitstatus != 0
  #system 'cp src/*.properties target/'
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
