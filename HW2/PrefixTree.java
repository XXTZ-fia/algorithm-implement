import java.util.Scanner;

public class PrefixTree {
    private char character;
    private PrefixTree left;
    private PrefixTree right;

    // 构造函数
    public PrefixTree(char character) {
        this.character = character;
    }

    // 静态方法构建树
    public static PrefixTree buildTree(char num[]) {
        int cal = 0;
        while (cal < num.length && num[cal] == ' ') {
            cal++;
        }
        if (cal >= num.length) {
            return null;
        }
        PrefixTree node = new PrefixTree(num[cal]);
        num[cal] = ' ';
        if (node.character == '*') {
            node.left = buildTree(num);
            node.right = buildTree(num);
        }
        return node;
    }

    // 前序遍历方法
    public String preorder(String st) {
        if (character != '*') {
            System.out.println(character + " " + st.length() + " " + st);
            return st;
        }
        if (character == '*') {
            if (left != null) {
                left.preorder(st + '0');
            }
            if (right != null) {
                right.preorder(st + '1');
            }
        }
        return st;
    }

    public char search(int num[], int cal){
        if(character != '*'){
            return character;
        }
        if(num[cal] == -1){
            return '\0';
        }
        if(num[cal] == 0){
            num[cal] = -1;
            return left.search(num, cal + 1);
        } else if(num[cal] == 1){
            num[cal] = -1;
            return right.search(num, cal + 1);
        }
        return '\0';
    }


    // 主方法
    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String result = scanner.nextLine();
        char[] num = input.toCharArray();
        char[] res = result.toCharArray();
        int[] byd = new int[result.length()];
        for(int i = 0; i < result.length(); i++) {
            byd[i] = res[i] - '0';
        }
        PrefixTree tree = buildTree(num);
        scanner.close();
        System.out.println("character bits encoding");
        System.out.println("-------------------------");
        tree.preorder("");

        int NumOfBit, NumOfChar = 0;
        NumOfBit = result.length();
        double CompRat = 0.0;
        for(int i = 0; i < byd.length; i++){
            char temp = tree.search(byd, i);
            System.out.print(temp);
            if(temp != '\0'){
                NumOfChar++;
            }
        }
        System.out.println();
        NumOfChar *= 8;
        CompRat = (double)NumOfBit / (double)NumOfChar * 100;
        System.out.println("Number of bits: " + NumOfBit);
        System.out.println("Number of characters: " + NumOfChar);
        System.out.println("Compression ratio: " + CompRat + "%");
    }
}