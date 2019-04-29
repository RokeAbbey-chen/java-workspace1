package myrmi.server;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class IServiceImpl extends UnicastRemoteObject implements IService, Serializable{
    String name = null;
    public IServiceImpl(String name) throws RemoteException {
        super();
        this.name = name;
    }
    @Override
    public void saySomething(String name) {
        /*
        请注意 这些system.out.print出去的东西最后都只会在服务端显示，不会在client端出现， 注意这里的client端记得不能调用同一个项目里的client，因为这样
        看不出来效果， 可以调用rmi-test-2里的client就可以清晰的看到了，
        rmiregistry 是运行在 这个项目的最外层包的外层目录
         */
        System.out.println("-------------- saySomething ---------------");
        System.out.println("hi " + name + ", my name is " + this.name);
        System.out.println("this.class =  " + this.getClass().getName());

        System.out.println("我实现了的接口:");
        for(Class c : this.getClass().getInterfaces())
            System.out.println("\t" + c.getName());
        System.out.println("我继承了的类：");
        System.out.println("\t" + getClass().getSuperclass().getName());
    }
}
