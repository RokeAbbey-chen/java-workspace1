package test;

public class DuckServiceImpl implements IService{

    @Override
    public String sayHello() {
        return "ga ga ga!!!";
    }

    @Override
    public String getSchema() {
        return "duck";
    }
}
