package com.company;

import java.util.Scanner;

/**
 * Created by nprasad on 04/02/2016 AD.
 */
public class PrintTriangle {
    void printAsterisk()
    {
        System.out.print("*");
    }

    void printLine(int count)
    {
        for(int i=0;i<count;i++)
        {
            printAsterisk();
        }
        System.out.println();
    }

    void printTriangle(int count)
    {
        for(int i=0;i<=count;i++)
        {
            printLine(i);
        }
    }
    public static void main(String args[])
    {
        Scanner s=new Scanner(System.in);
        int count=s.nextInt();
        new PrintTriangle().printTriangle(count);
    }
}
