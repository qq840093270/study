package com.bigdata.demo.Hive;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname MyUDTF
 * @Description  输入数据：hello,hive,spark
 *               输出数据：hello
 *                        hive
 *                        spark
 * @Date 2021/2/1 17:15
 * @Created zzj
 */
public class MyUDTF extends GenericUDTF {

    private List<String> outPut = new ArrayList<>();
    @Override
    public StructObjectInspector initialize(StructObjectInspector argOIs) throws UDFArgumentException {
        //输出数据的默认列名，可以别名覆盖
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("word");
        //输出数据的类型
        List<ObjectInspector> fieldOIs = new ArrayList<>();
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        //最终返回值
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    //处理输入数据
    @Override
    public void process(Object[] args) throws HiveException {

        //1.取出输入数据
        String input = args[0].toString();
        String split = args[1].toString();
        //2.按“，”分割
        String[] words = input.split(split);


        for (String word:words){

            outPut.clear();
            //*个元素*列
            outPut.add(word);
            //输出一行数据
            forward(word);
        }

    }

    //收尾方法
    @Override
    public void close() throws HiveException {

    }
}
