package com.mark.games.fallingblocks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.util.Log;

import com.mark.games.fallingblocks.framework.FileIO;

public class Settings {

	public static int[] scoresArray = new int[5];
	public static boolean showFps;
	public static boolean playMusic;
	public static ArrayList<String> logList;
	
	public static void load(FileIO files, String file) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					files.readFile(file)));
			SettingsScreen.showFps = Boolean.parseBoolean(in.readLine());
			showFps = SettingsScreen.showFps;
			
			SettingsScreen.playMusic = Boolean.parseBoolean(in.readLine());
			playMusic = SettingsScreen.playMusic;
			
			for(int i = 0; i < 5; i++) 
				scoresArray[i] = Integer.parseInt(in.readLine());
			
			in.close();
		} catch (IOException e) {
		} catch (NumberFormatException e) {
		} finally {
			try {
				if (in != null){
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}
	public static void loadLog(FileIO files, String file) {
		BufferedReader in = null;
		logList = new ArrayList<String>();
		try {
			in = new BufferedReader(new InputStreamReader(
					files.readFile(file)));
			String a = "";
			while ((a = in.readLine()) != null){
				logList.add(a);
			}
		} catch (IOException e) {
		} catch (NumberFormatException e) {
		} finally {
			try {
				if (in != null){
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}

	public static void save(FileIO files) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					files.writeFile(".falling")));
			out.write(Boolean.toString(SettingsScreen.showFps));
			out.newLine();
			out.write(Boolean.toString(SettingsScreen.playMusic));
			out.newLine();
			
			for (int i = 0; i < 5; i++) {
                out.write(Integer.toString(scoresArray[i]));
                out.newLine();
			}
			out.close();
		} catch (IOException e) {

		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {

			}
		}
	}
	public static void addScore(int score) {
        for (int i = 0; i < 5; i++) {
        	Log.d("scores: ", Integer.toString(scoresArray[i]));
            if (scoresArray[i] <= score) {
                for (int j = 4; j > i; j--)
                	scoresArray[j] = scoresArray[j - 1];
                scoresArray[i] = score;
                break;
            }
        }
    }
	public static void writeLog2(FileIO files, String tag, String message){
		BufferedWriter out= null;

		try {
			//create the stream using appendToFile() if you don't want to erase the contents of the file when writing
			out = new BufferedWriter(new OutputStreamWriter(files.appendToFile(".log")));
			out.append(tag + ": " + message);
			out.newLine();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {

			}
		}
	}
	public static void deleteLog(FileIO files){
		BufferedWriter out= null;
		try {
			//delete the log file easily
			out = new BufferedWriter(new OutputStreamWriter(files.writeFile(".log")));
			out.write("");
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {

			}
		}
	}
}