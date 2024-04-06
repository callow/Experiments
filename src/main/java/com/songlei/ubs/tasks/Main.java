package com.songlei.ubs.tasks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// UBS Data Classification: Public - Suitable for sharing with persons outside UBS

// TODO take this code and transform it into a professional project with the following expectations:
/*
- All initial TODOs are resolved
- Project is configured with gradle or maven
- Code is clean and maintainable
- TODOs must be added where something is not clear and must be discussed
- Please feel free to fix the code or refactor anything which doesn't look right
- Ensure testing has been properly considered
*/


class Main {
    public static void main(String[] args) {
        //number of iterations
        int is = 10;
        //TODO adapt the code to receive the input list either from stdin or as a static list
        //fill the list
        ArrayList x = new ArrayList();
        x.add("Mary");
        x.add("Alina");
        x.add("John");
        x.add("Nicole");x.add("Mike");

        //input file
        File file = new File("/tmp/a.txt"); //TODO all files must be read from a configurable folder
        try {
            FileWriter b = new FileWriter(file);
            for (int i1 = 1; i1 < x.size(); i1++) {
                //writes a name per line
                b.write ((String) x.get(i1));
            }
        } catch (IOException e) {
            //nothing to do here
        }

        //TODO - Log the count of names with length 4
        //TODO - Update the above code to receive the name and age for each person and save them as CSV in b.txt file
        //TODO - Implement a way to write into the b.txt file only persons over a specified age
    }
}
