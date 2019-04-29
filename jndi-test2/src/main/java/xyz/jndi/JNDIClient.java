package xyz.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;

import xyz.service.DBService;
import xyz.service.LogService;

public class JNDIClient {

    public static void main(String[] args){
        try{
            JNDIContainer container=new JNDIContainer();
            container.init();

            //JNDI客户端使用标准JNDI接口访问命名服务。
//            InitialContext;
            Context ctx=container.getContext();
            DBService db=(DBService)ctx.lookup("DBService");
            System.out.println("db location is:"+db.getLocation()+",state is:"+db.getState());
            db.accessDB();

            LogService ls=(LogService)ctx.lookup("LogService");
            ls.log("this is a log message.");

            container.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}