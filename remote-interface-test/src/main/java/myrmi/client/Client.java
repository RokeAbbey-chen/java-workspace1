package myrmi.client;

import myrmi.server.IService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws NamingException, RemoteException {
        Context namingContext = new InitialContext();
        String url = "rmi://localhost/service1";
        IService iService = (IService) namingContext.lookup(url);
        iService.saySomething("client");

        System.out.println("---------------client main ---------");
        System.out.println("我实现了的接口:");
        for(Class c : iService.getClass().getInterfaces())
            System.out.println("\t" + c.getName());

        System.out.println("我继承了的类:");
        System.out.println("\t" + iService.getClass().getSuperclass().getName());

    }
}
