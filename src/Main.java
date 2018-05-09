/*
 *
 *  FileCrypt made by alexzava
 *
 *  Github: https://github.com/alexzava/filecrypt
 *
 *  License: Apache License 2.0
 *
 *  Copyright 2018 alexzava
 *
 */

import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        System.out.println("\nFile Crypt (1.0) - Alexzava\n");

        String op, filename;
        char[] password;
        Console console = System.console();

        if(!args[0].equals("encrypt") && !args[0].equals("decrypt"))
        {
            System.out.println("Invalid input.");
            return ;
        }

        op = args[0];
        filename = args[1];

        File file = new File(filename);
        if(!file.exists())
		{
			System.out.println("Invalid file.");
			return ;
		}

        System.out.println("Password: ");
        password = console.readPassword();

        if(op.equals("encrypt"))
        {
            System.out.println("Retype password: ");
            char[] rePassword = console.readPassword();

            if(!Arrays.equals(password, rePassword))
            {
                System.out.println("The passwords are different.");
                return ;
            }

            if(password.length < 10)
            {
                System.out.println("WARNING! The password is short. A password of at least 10 characters is recommended.\nAre you sure you want to continue? (Y/N)");
                String resp = console.readLine();
                if(!resp.contains("y") && !resp.contains("Y"))
                    return ;
            }

            CryptoAES.Encrypt(filename, new String(password));
        }
        else if(op.equals("decrypt"))
        {
            CryptoAES.Decrypt(filename, new String(password));
        }
    }
}