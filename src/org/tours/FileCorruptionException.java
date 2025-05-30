package org.tours;

public class FileCorruptionException extends Exception{
    public FileCorruptionException(String exp, Integer line) {
        super("Expected: " + exp + " at line #" + line);

    }
}
