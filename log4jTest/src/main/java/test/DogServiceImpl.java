package test;

public class DogServiceImpl implements IService{
    @Override
    public String sayHello() {
        return "wang wang wang !!!";
    }

    @Override
    public String getSchema() {
        return "dog";
    }
}
