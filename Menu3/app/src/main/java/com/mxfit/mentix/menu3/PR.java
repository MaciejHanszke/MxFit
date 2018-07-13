package com.mxfit.mentix.menu3;

import java.util.ArrayList;



class PR {
    ArrayList<Integer> redo(String Sets){
        ArrayList<Integer> pushups = new ArrayList<>();
        int n = 0;
        String push = Sets.replace('-',' ');
        while (n != -1) {
            String sub = "";
            n = push.indexOf(" ");
            if (n != -1) {
                sub = push.substring(0, n);
                push = push.substring((n + 1));
            } else {
                sub = push;
            }
            pushups.add(Integer.parseInt(sub));
        }
        return pushups;
    }
}
