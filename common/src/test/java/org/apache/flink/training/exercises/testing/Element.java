package org.apache.flink.training.exercises.testing;

import java.io.Serializable;

public class Element  implements Serializable  {
    private long timestamp;

    private String name;

    private long id;

    @Override
    public String toString() {
        return "Element{" +
                "timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
