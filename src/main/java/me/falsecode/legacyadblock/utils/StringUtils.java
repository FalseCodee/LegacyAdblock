package me.falsecode.legacyadblock.utils;

import net.minecraft.text.Text;

import java.util.List;

public class StringUtils {
    public static String concatArray(List<Text> array, String concat) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < array.size(); i++) {
            sb.append(array.get(i).getString());
            if(i < array.size() - 1) {
                sb.append(concat);
            }
        }

        return sb.toString();
    }
}
