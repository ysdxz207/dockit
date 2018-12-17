public class TestClass {
    public static void main(String[] args) throws ClassNotFoundException {

        Class clazz = TestClass.class.getClassLoader().loadClass("java.util.List");

        System.out.println(clazz);
    }
}
