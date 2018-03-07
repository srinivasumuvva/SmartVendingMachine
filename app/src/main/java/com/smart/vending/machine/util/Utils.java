package com.smart.vending.machine.util;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by MUVVASR on 3/7/2018.
 */

public class Utils {


    public  static HashMap<String,Float> validCoins=new HashMap<String,Float>();


    public static boolean isValidCoin(String pInsertedCoin){
      return validCoins.containsKey(pInsertedCoin);
    }

}
