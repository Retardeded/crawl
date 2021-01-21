import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class Webcrawling {

    public static volatile boolean stoppingCondition = false;
    public static volatile int numberOfFoundLinks = 0;
    public static int numberOfLinksToFind;
    public static String[] bannedAges;

    public static final int MIN_DEPTH_NUMBER = 1;
    public static final int MAX_DEPTH_NUMBER = 100000;
    public static final int DEFAULT_DEPTH_NUMBER = 8;
    public static final int DEFAULT_LINKS = 5;

    private volatile List<String> urls;
    private final List<String> urlsTitles;
    private final List<String> paragraphs;
    private final List<String> readScores;

    private int crawlingThreadsNumber;
    private int depthNumber;

    public Webcrawling() {
        super();
        urls = new ArrayList<>();
        urlsTitles = new ArrayList<>();
        paragraphs = new ArrayList<>();
        readScores = new ArrayList<>();
        crawlingThreadsNumber = 1;
        depthNumber = DEFAULT_DEPTH_NUMBER;
        numberOfLinksToFind = DEFAULT_LINKS;
    }

    public int getFoundLinksNumber() {
        return urls.size();
    }

    public void setCrawlingThreadsNumber(int value) {
        if (value <= 0 || value > 8) {
            throw new IndexOutOfBoundsException();
        }
        crawlingThreadsNumber = value;
    }

    public int getDepthNumber() {
        return depthNumber;
    }

    public void setDepthNumber(int value) {
        if (value < MIN_DEPTH_NUMBER || value > MAX_DEPTH_NUMBER) {
            throw new IndexOutOfBoundsException();
        }
        depthNumber = value;
    }

    public int getNumberOfLinksToFind() {
        return numberOfLinksToFind;
    }

    public void setNumberOfLinksToFind(int value) {

        if (value < 1 ) {
            throw new IndexOutOfBoundsException();
        }
        numberOfLinksToFind = value;
    }

    public void setBannedAge(String value) {
        bannedAges = value.split(" |,");
    }

    public void resetDepthNumber() {
        depthNumber = DEFAULT_DEPTH_NUMBER;
    }

    public void run(final String startUrl) throws InterruptedException {
        clear();
        Queue<String> processingQueue = new ConcurrentLinkedQueue<>();
        for(int i = 0; i < crawlingThreadsNumber; i++) {
            processingQueue.offer(startUrl);
        }
        Set<String> processedUrls = new CopyOnWriteArraySet<>();
        createAndStartCrawlingThreads(processingQueue, processedUrls);
    }

    private void clear() {
        urls.clear();
        urlsTitles.clear();
        paragraphs.clear();
        readScores.clear();
        stoppingCondition = false;
        numberOfFoundLinks = 0;
    }

    private void createAndStartCrawlingThreads(final Queue<String> processingUrls, final Set<String> processedUrls) throws InterruptedException {

        Thread[] threads = new Thread[crawlingThreadsNumber];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new WebcrawlingThread(
                    urls, urlsTitles,paragraphs,readScores,
                    processingUrls, processedUrls,
                    depthNumber
            );
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }
        }
    }

    public void export(final String exportFileName) {
        try (PrintWriter fileWriter = new PrintWriter(exportFileName, StandardCharsets.UTF_8)) {
            final int lastIndex = urls.size() - 1;
            for (int i = 0; i <= lastIndex; i++) {
                fileWriter.println(urls.get(i));
                fileWriter.println(urlsTitles.get(i));
                fileWriter.println(paragraphs.get(i));
                fileWriter.println(readScores.get(i));
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }

    }
}
