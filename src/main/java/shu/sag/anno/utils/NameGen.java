package shu.sag.anno.utils;
import java.util.Random;

public class NameGen {
    private static final String[] prefix={"一只","一碗","一头","著名","卧龙岗"};
    private static final String[] medium={"睡着的","慵懒的","高大的"};
    private static final String[] postfix={"宽面","雄狮","绵羊"};

    public static String randomName(){
        Random r_prefix = new Random();
        Random r_medium = new Random();
        Random r_postfix =  new Random();

        int num_prefix = r_prefix.nextInt(50)%prefix.length;
        int num_medium = r_medium.nextInt(50)%medium.length;
        int num_postfix = r_postfix.nextInt(50)%postfix.length;

        return prefix[num_prefix]+medium[num_medium]+postfix[num_postfix];
    }
}
