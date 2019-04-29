package test;

import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.RunningJob;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;

public class WCDriver {
    public static void main(String[] args) throws OdpsException {
        args = new String[]{"roke", "abbey"};
        if(args.length !=2){
            System.out.println("参数错误");
            System.exit(2);
        }

        JobConf job = new JobConf();

        job.setMapOutputKeySchema(SchemaUtils.fromString("word:string"));
        job.setMapOutputValueSchema(SchemaUtils.fromString("count:bigint"));

        InputUtils.addTable(TableInfo.builder().tableName(args[0]).build(),
                job);
        OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(),
                job);

        job.setMapperClass(WCMapper.class);
        job.setReducerClass(WCReducer.class);

        RunningJob rj = JobClient.runJob(job);
        rj.waitForCompletion();
    }
}
