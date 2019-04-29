package two;

public class Hello2 implements Hello2MBean{
    private String name = "roke";
    @Override
    public void greet() {
        System.out.println(name + " hello ");
    }

    @Override
    public void bye() {
        System.out.println(name + " bye bye");
    }
}
