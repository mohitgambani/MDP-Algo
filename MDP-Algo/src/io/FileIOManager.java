package io;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class FileIOManager {
	private static String readFilePath = "";
	private static String writeFilePath = "";
	
	public static void writeFile(String content, String fileName) throws IOException{
		FileWriter outputStream = null;
		Date date = new Date();
		try {
            outputStream = new FileWriter(writeFilePath + fileName + "_" + date.toString() + ".txt");

            int index;
            for(index = 0; index < content.length();++index){
            	outputStream.write(content.charAt(index));
            }
        }finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
	}
	
	public static String readFile() throws IOException{
		FileReader inputStream = null;
		String content = "";
        try {
            inputStream = new FileReader(readFilePath);
            int c;
            while ((c = inputStream.read()) != -1) {
                content += (char) c;
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return content;
	}
	
	public static void setReadFilePath(String path) {
		readFilePath = path;
	}
}
