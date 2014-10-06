
cp pom.xml pom.xml.bak
#1. find lineno of maven-jetty-plugin dependency
line_no_jetty_plugin=`grep -n maven-jetty-plugin pom.xml | awk -F":" '{print $1}' | head -1`

line_no_start=0
line_no_end=0
#2. find maven-jetty-plugin dependency start line no
for i in {5..1};
do
  line_no=$((line_no_jetty_plugin-i))
  #echo $line_no
  output=`echo "sed -n -e '$line_no#p'  pom.xml" | sed "s/#//g" | bash | grep "<dependency>"`
  if [ "x$output" != "x" ];
  then
      line_no_start=$line_no
  fi
done
#echo line_no_start=$line_no_start

#3. find maven-jetty-plugin dependency end line no
for i in {1..5};
do
  line_no=$((line_no_jetty_plugin+i))
  #echo $line_no
  output=`echo "sed -n -e '$line_no#p' pom.xml" | sed "s/#//g" | bash | grep "<\/dependency>"`
  #echo $output  
  if [ "x$output" != "x" ];
  then
      line_no_end=$line_no
  fi
done
#echo line_no_end=$line_no_end

#4. delete scope line betwee #2 no and #3 line no
scope_line_no=-1
line_no=$line_no_start
while [ $line_no -lt $line_no_end ] ; do
   output=`echo "sed -n -e '$line_no#p' pom.xml" | sed "s/#//g" | bash` 
   grepScopeResult=`echo $output | grep scope`
   #echo $grepScopeResult
   if [ "x$grepScopeResult" != "x" ];
   then
      #delete scope line
      scope_line_no=$line_no 
   fi   
   line_no=$((line_no+1))
done
#echo scope_line_no=$scope_line_no

output=`echo "sed -i '$scope_line_no#d' pom.xml" | sed "s/#//g" | bash`
failed=$?
if [ "$failed" == "1" ];
then
    #echo failed, try mac cmd
    output=`echo "sed -i \"\" '$scope_line_no#d' pom.xml" | sed "s/#//g" | bash`
    #echo $?
fi

mvn clean compile package -Dmaven.test.skip=true

cp pom.xml.bak pom.xml
rm -f pom.xml.bak




