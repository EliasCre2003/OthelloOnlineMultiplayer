package eliascregard.client.main;

public class Test {
    public static void main(String[] args) {
        int x = 1, y = 2, z = 3;
        if ((x > y) && (x++ < y)) {
            System.out.println("x < y < z");
        }
        System.out.println(x);
    }
}
