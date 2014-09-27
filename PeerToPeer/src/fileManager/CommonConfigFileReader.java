package fileManager;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class CommonConfigFileReader {

	private int numberOfPreferredNeighbors;
    private int unchokingInterval;
    private int optimisticUnchokingInterval;
    private String fileName;
    private long fileSize;
    private long pieceSize;
    
    public void readFile() {
		
		String common = "Common.cfg";
		
		Properties file = new Properties();
		
		try {
			
			file.load(new BufferedInputStream(new FileInputStream(common)));
			numberOfPreferredNeighbors = Integer.parseInt(file.getProperty("NumberOfPreferredNeighbors"));
			unchokingInterval = Integer.parseInt(file.getProperty("UnchokingInterval"));
			optimisticUnchokingInterval = Integer.parseInt(file.getProperty("OptimisticUnchokingInterval"));
			fileName = file.getProperty("FileName");
			fileSize = Long.parseLong(file.getProperty("FileSize"));
			pieceSize = Long.parseLong(file.getProperty("PieceSize"));
			
		} catch (FileNotFoundException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public int getNumberOfPreferredNeighbors() {
		return numberOfPreferredNeighbors;
	}

	public int getUnchokingInterval() {
		return unchokingInterval;
	}

	public int getOptimisticUnchokingInterval() {
		return optimisticUnchokingInterval;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public long getPieceSize() {
		return pieceSize;
	}

}
