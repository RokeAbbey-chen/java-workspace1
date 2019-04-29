package myrmi.server;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class Server {
    //要在myrmi(最外层包)的外面运行rmiregistry 程序 否则找不到IServiceImpl
    public static void main(String[] args) throws NamingException, RemoteException {
//        System.setSecurityManager(new RMISecurityManager());
        System.out.println("line 13");
        IService service = new IServiceImpl("service1");
        System.out.println("line 15");
        Context namingContext = new InitialContext();
        System.out.println("line 17");
        try {
            namingContext.bind("rmi://localhost/service1", service);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("line 19");
    }
}
