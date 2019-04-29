package test;

import com.aliyun.odps.data.Record;
import com.aliyun.odps.mapred.MapperBase;

import java.io.IOException;

public class WCMapper extends MapperBase {
    private Record word;
    private Record one;

    @Override
    public void setup(TaskContext context) throws IOException {
        word = context.createMapOutputKeyRecord();
        one = context.createMapOutputValueRecord();
        one.set(new Object[] { 1L });
    }

    @Override
    public void map(long recordNum, Record record, TaskContext context)
            throws IOException {

        System.out.println("recordNum:"+recordNum);
        for (int i = 0; i < record.getColumnCount(); i++) {
            String[] words = record.get(i).toString().split("\\s+");
            for (String w : words) {
                word.set(new Object[] { w });
                context.write(word, one);
            }
        }
    }

    @Override
    public void cleanup(TaskContext context) throws IOException {
    }

}
