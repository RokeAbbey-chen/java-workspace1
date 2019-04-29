package two;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class Test {
    public static void main(String[] args) {
        try {
            ObjectName name = new ObjectName("rokeabbey:name=hello");
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.registerMBean(new Hello2(), name);
            Thread.sleep(13 * 60 * 60 * 1000);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
