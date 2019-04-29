package myrmi.server;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IService extends Remote, Serializable{
    void saySomething(String name) throws RemoteException;
}
