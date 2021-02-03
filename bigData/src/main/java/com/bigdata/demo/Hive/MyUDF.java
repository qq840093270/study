package com.bigdata.demo.Hive;

import jodd.util.StringUtil;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

/**
 * @Classname MyUDF
 * @Description TODO
 * @Date 2021/1/30 17:52
 * @Created zzj
 */
public class MyUDF extends GenericUDF {

    //校验数据参数个数
    @Override
    public ObjectInspector initialize(ObjectInspector[] objectInspectors) throws UDFArgumentException {
        if (objectInspectors.length != 1){
            throw new UDFArgumentException("参数个数不为1");
        }
        return PrimitiveObjectInspectorFactory.javaIntObjectInspector;
    }

    @Override
    public Object evaluate(DeferredObject[] deferredObjects) throws HiveException {
        //取出输入数据
        String input = deferredObjects[0].get().toString();
        //判断输入数据是否为null
        if (StringUtil.isEmpty(input)){
            return 0;
        }
        return input.length();
    }

    @Override
    public String getDisplayString(String[] strings) {
        return null;
    }
}
