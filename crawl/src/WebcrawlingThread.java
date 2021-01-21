
import java.text.DecimalFormat;
import java.util.*;


public class WebcrawlingThread extends Thread {

    private final Queue<String> tasks;
    private final Set<String> processedUrls;
    private volatile List<String> urls;
    private volatile List<String> bannedUrls = new ArrayList<>();
    private final List<String> urlsTitles;
    private final List<String> paragraphs;
    private final List<String> readScores;
    private final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private final int depthNumber;
    private static final DecimalFormat df2 = new DecimalFormat("#.##");

    public WebcrawlingThread(
            final List<String> urls, final List<String> urlsTitles,final List<String> paragraphs,final List<String> readScores,
            final Queue<String> tasks, final Set<String> processedUrls,
            final int depthNumber) {

        this.tasks = tasks;
        this.processedUrls = processedUrls;

        this.urls = urls;
        this.urlsTitles = urlsTitles;
        this.paragraphs = paragraphs;
        this.readScores = readScores;

        this.depthNumber = depthNumber;
    }

    @Override
    public void run() {
        int repeatTimes = depthNumber;
        do {
                final String currUrl = tasks.poll();

                    if ( (currUrl == null
                            || processedUrls.contains(currUrl))) {
                        return;
                    }

                    final String parsedHtmlCode = WebParsing.parseHtmlCode(currUrl);
                    final List<String> parsedLinks = WebParsing.parseLinksFromHtmlCode(currUrl, parsedHtmlCode);
                    processedUrls.add(currUrl);

                    for (String link : parsedLinks) {
                        if (Webcrawling.stoppingCondition)
                            break;
                        addUrlToUrlsIfIsNotAdded(link);
                        if (!processedUrls.contains(link)) {
                            tasks.offer(link);
                        }
                    }
                    repeatTimes--;

            } while (!tasks.isEmpty()
                && repeatTimes > 0 && ! Webcrawling.stoppingCondition);
        }

    private void addUrlToUrlsIfIsNotAdded(final String url) {

        String urlTitle;
        String paragraph;
        String readScore;

        threadLocal.set(url);
        if (urlWorthProcessing(url)) {
            final String htmlCode = WebParsing.parseHtmlCode(url);
            urlTitle = WebParsing.parseTitleFromHtmlCode(htmlCode);
            paragraph = WebParsing.parseParagraphFromTitle(urlTitle);
            int ageAverage = getAvgScore(paragraph);
            readScore = "This text should be understood on average by " + df2.format(ageAverage) + " year olds.";

            String threadName = Thread.currentThread().getName();
            System.out.println("tasks Size:" + tasks.size());
            System.out.println("thread::" + threadName);
            System.out.println(url);
            System.out.println(urlTitle);
            System.out.println(paragraph);
            System.out.println(readScore);
            System.out.println();

            if(!Arrays.asList(Webcrawling.bannedAges).contains(String.valueOf(ageAverage))) {
                addData(urlTitle, paragraph, readScore);
                System.out.println("founds++:" + Webcrawling.numberOfFoundLinks);
            }
            else
            {
                urls.remove(threadLocal.get());
                bannedUrls.add(threadLocal.get());
            }
        }

    }

    private synchronized boolean urlWorthProcessing(String url) {
        if(urls.contains(url) || bannedUrls.contains(url))
            return false;
        else
        {
            urls.add(url);
            return true;
        }
    }

    private void addData(String urlTitle, String paragraph, String readScore) {
        urlsTitles.add(urlTitle);
        paragraphs.add(paragraph);
        readScores.add(readScore);
        Webcrawling.numberOfFoundLinks++;

        if(Webcrawling.numberOfFoundLinks == Webcrawling.numberOfLinksToFind)
            Webcrawling.stoppingCondition = true;
    }

    private int getAvgScore(String paragraph) {
        TextAnalysis textAnalysis = new TextAnalysis(paragraph);

        double score = 4.71 * textAnalysis.getCharacters() / textAnalysis.getWords()
                + 0.5 * textAnalysis.getWords() / textAnalysis.getSentences() - 21.43;
            int[] arr = {6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 24, 25};
            int level = Math.max(1, Math.min(14, (int) Math.ceil(score))) - 1;
            return arr[level];
    }
}
