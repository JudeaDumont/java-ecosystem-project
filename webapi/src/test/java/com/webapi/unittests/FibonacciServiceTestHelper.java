package com.webapi.unittests;

import java.util.List;

public class FibonacciServiceTestHelper {
    public static boolean checkFibonacci(List<Long> fibonacci){
        if(fibonacci.size()<3){
            return false;
        }
        for (int i = 2; i < fibonacci.size(); i++) {
            if(fibonacci.get(i) != fibonacci.get(i - 1) + fibonacci.get(i - 2)){
                return false;
            }
        }
        return true;
    }
}
