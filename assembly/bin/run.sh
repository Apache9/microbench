# add libs to CLASSPATH
for f in lib/*.jar; do
    CLASSPATH=${CLASSPATH}:$f;
done
export CLASSPATH
java -Xms1g -Xmx1g org.openjdk.jmh.Main $@
