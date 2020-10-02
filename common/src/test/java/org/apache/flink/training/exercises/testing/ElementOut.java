package org.apache.flink.training.exercises.testing;

public class ElementOut {

    private String key;

    private String result;

    private String windowStartTime;

    private String windowEndTime;


    @Override
    public String toString() {
        return "ElementOut{" +
                "key='" + key + '\'' +
                ", result='" + result + '\'' +
                ", windowStartTime='" + windowStartTime + '\'' +
                ", windowEndTime='" + windowEndTime + '\'' +
                '}';
    }

    public String getWindowStartTime() {
        return windowStartTime;
    }

    public void setWindowStartTime(String windowStartTime) {
        this.windowStartTime = windowStartTime;
    }

    public String getWindowEndTime() {
        return windowEndTime;
    }

    public void setWindowEndTime(String windowEndTime) {
        this.windowEndTime = windowEndTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
