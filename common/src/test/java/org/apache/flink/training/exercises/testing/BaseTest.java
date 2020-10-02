package org.apache.flink.training.exercises.testing;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class BaseTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Tuple2> streamSource = env.fromElements(new Tuple2("a", 1),
                new Tuple2("a", 1)
                , new Tuple2("b", 1));

       streamSource.keyBy(0).sum(1).print();
       env.execute("test tuple");
    }


}
