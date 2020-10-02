package org.apache.flink.training.exercises.testing;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class WatermarkTest {

    private static final Logger logger = LoggerFactory.getLogger(WatermarkTest.class);

   private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    private static List<Element> buildElements() {

        List<Element> elements = new ArrayList<>();
        Date date = new Date();
        System.out.println("当前时间 : " + sdf.format(date));
        Element element = new Element();
        element.setName("lyj");
        element.setTimestamp(DateUtils.addSeconds(date, -10).getTime());
        elements.add(element);
        element.setId(1);

//        element = new Element();
//        element.setName("wyy");
//        element.setTimestamp(DateUtils.addSeconds(date, 10).getTime());
//        elements.add(element);
//        element.setId(2);


//        element = new Element();
//        element.setName("wyy");
//        element.setTimestamp(DateUtils.addSeconds(date, -8).getTime());
//        element.setId(3);
//        elements.add(element);
//
//        element = new Element();
//        element.setName("wyy");
//        element.setTimestamp(DateUtils.addSeconds(date, -20).getTime());
//        element.setId(4);
//        elements.add(element);
//
//        element = new Element();
//        element.setName("wyy");
//        element.setTimestamp(DateUtils.addSeconds(date, -20).getTime());
//        element.setId(5);
//        elements.add(element);

        return elements;
    }

    /**
     * 测试数据
     * 000001,1461756862000
     * 000001,1461756866000
     * 000001,1461756872000
     * 000001,1461756873000
     * 000001,1461756874000
     * 000001,1461756876000
     * 000001,1461756877000
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        environment.setParallelism(1);
        DataStreamSource<String> streamSource = environment.socketTextStream("192.168.31.200", 9999);
        org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks<Tuple2<String, Long>> assignerWithPeriodicWatermarks =
                new org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks<Tuple2<String, Long>>() {
                    long maxOutOfOrderness = 10000L;
                    long currentMaxTimestamp = 0L;

                    @Override
                    public long extractTimestamp(Tuple2<String, Long> element, long recordTimestamp) {
                        System.out.println("element : " + element);
                        Long timestamp = element.f1;
                        currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp);
                        System.out.println("timestamp:" + element.f0 +","+ element.f1 + "|" +sdf.format(element.f1) +","+  currentMaxTimestamp + "|"+ sdf.format(currentMaxTimestamp) + ","+ sdf.format(getCurrentWatermark().getTimestamp()));
                        return timestamp;
                    }

                    @Nullable
                    @Override
                    public Watermark getCurrentWatermark() {
                        return new Watermark(this.currentMaxTimestamp - maxOutOfOrderness);
                    }
                };

        SingleOutputStreamOperator<ElementOut> apply = streamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {

            @Override
            public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                System.out.println(value);

                String[] split = value.split("\\W+");
                if (split.length == 2) {
                    out.collect(new Tuple2<String, Long>(split[0], Long.valueOf(split[1])));
                }
            }
        }).assignTimestampsAndWatermarks(assignerWithPeriodicWatermarks)
                .keyBy(each -> {
                    return each.f0;
                })
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply(new WindowFunction<Tuple2<String, Long>, ElementOut, String, TimeWindow>() {
                           @Override
                           public void apply(String s, TimeWindow window, Iterable<Tuple2<String, Long>> input,
                                             Collector<ElementOut> out) throws Exception {

                               ElementOut elementOut = new ElementOut();
                               elementOut.setKey(s);
                               int count = 0;
                               for (Tuple2<String, Long> each : input) {
                                   count++;
                               }
                               elementOut.setResult(String.valueOf(count));
                               out.collect(elementOut);
                           }
                       }
                )
                ;
        apply.print("计算结果： ");
        environment.execute("window word count");
    }
}
