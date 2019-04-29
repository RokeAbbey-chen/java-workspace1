package test;

public class CatServiceImpl implements IService{
    @Override
    public String sayHello() {
        return "miao miao miao !!!";
    }

    @Override
    public String getSchema() {
        return "cat";
    }
}
