import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        double[][] x1 = {{5,0,-2,-8},{-6,8,8,8},{8,-4,11,19}, {-1,3,3,-1}};
        Matrix a = new Matrix(x1);

        System.out.println(a);
        System.out.println(a.transpose().multiply(a));
        System.out.println(a);
      }
}