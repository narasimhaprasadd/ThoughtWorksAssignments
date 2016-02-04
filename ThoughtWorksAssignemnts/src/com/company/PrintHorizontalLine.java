package com.company;

import java.util.Scanner;

/**
 * Created by nprasad on 04/02/2016 AD.
 */
public class PrintHorizontalLine {
    void printAsterisk()
    {
        System.out.print("*");
    }
    public static void main(String args[])
    {
        int count;
        PrintHorizontalLine phl=new PrintHorizontalLine();
        Scanner s = new Scanner(System.in);
        count=s.nextInt();
        for (int i=0;i<count;i++)
        {
                phl.printAsterisk();
        }
    }
}
