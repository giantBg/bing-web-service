package com.bingcore.web.util.kafka.case1;

/**
 * Created by xubing on 16/10/16.
 */
import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;


public class MyPartitioner implements Partitioner {
    public MyPartitioner(VerifiableProperties props) {


    }


    /*
     * @see kafka.producer.Partitioner#partition(java.lang.Object, int)
     */
    @Override
    public int partition(Object key, int partitionCount) {
        return Integer.valueOf((String) key) % partitionCount;
    }
}