package xyz.serviceprovider;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;

public class SimpleLogServiceFactory implements ObjectFactory {

    @Override
    public Object getObjectInstance(Object obj, Name name, Context ctx,
                                    Hashtable<?, ?> env) throws Exception {
        if(obj instanceof Reference){
            return new SimpleLogService();
        }
        return null;
    }
}