import javax.swing.*;
import java.awt.*;


public class WebCrawler extends JFrame {

    private final Webcrawling model;

    private final JLabel urlLabel;
    private final JTextField urlTextField;
    private final JToggleButton runButton;

    private final JLabel threadsLabel;
    private final JTextField threadsTextField;

    private final JLabel depthLabel;
    private final JTextField depthTextField;
    private final JCheckBox depthCheckBox;

    private final JLabel bannedAgeLabel;
    private final JTextField bannedAgeTextField;

    private final JLabel numberOfLinksLabel;
    private final JTextField numberOfLinksTextField;

    private final JLabel elapsedSecondsNameLabel;
    private final JLabel elapsedSecondsLabel;

    private final JLabel foundLinksNumberNameLabel;
    private final JLabel foundLinksNumberLabel;

    private final JLabel urlTitleLabel;
    private final JTextField exportUrlTextField;
    private final JButton exportButton;

    public WebCrawler() {

        WebcrawlerAppSettings settings = new WebcrawlerAppSettings();
        setFrameLayout();

        this.model = new Webcrawling();

        urlLabel = new JLabel("URL:");
        urlTextField = new JTextField();
        runButton = new JToggleButton("Run");

        threadsLabel = new JLabel("Threads Number:");
        threadsTextField = new JTextField();

        depthLabel = new JLabel("Max Depth:");
        depthTextField = new JTextField();
        depthCheckBox = new JCheckBox("Enabled", true);

        bannedAgeLabel = new JLabel("Banned age:");
        bannedAgeTextField = new JTextField();

        numberOfLinksLabel = new JLabel("Min number of links to find:");
        numberOfLinksTextField = new JTextField();

        elapsedSecondsNameLabel = new JLabel("Elapsed seconds:");
        elapsedSecondsLabel = new JLabel("0");

        foundLinksNumberNameLabel = new JLabel("Found links:");
        foundLinksNumberLabel = new JLabel("0");
        foundLinksNumberLabel.setName("ParsedLabel");

        urlTitleLabel = new JLabel("Export path");
        exportUrlTextField = new JTextField();
        exportButton = new JButton("Export");

        setFrameElements(settings);
        addElementsToFrame();
        setFrame(settings.getFrameWidth(), settings.getFrameHeight());
    }


    private void setFrameLayout() {
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        setLayout(flowLayout);
    }

    private void setFrameElements(WebcrawlerAppSettings settings) {
        setUrlTextField(settings.getTextFieldWidth(), settings.getTextFieldHeight());
        setRunButton();

        setThreadsTextField(settings.getTextFieldWidth(), settings.getTextFieldHeight());

        setDepthTextField(settings.getTextFieldWidth(), settings.getTextFieldHeight());
        setDepthCheckBox();

        setBannedAgeField(settings.getTextFieldWidth(), settings.getTextFieldHeight());

        setNumberOfUniqueLinks(settings.getTextFieldWidth(), settings.getTextFieldHeight());
        setUrlTitleLabel(settings.getTextFieldWidth(), settings.getTextFieldHeight());
        setExportUrlTextField(settings.getTextFieldWidth(), settings.getTextFieldHeight());
        setExportButton();
    }

    private void addElementsToFrame() {
        add(urlLabel, BorderLayout.LINE_START);
        add(urlTextField, BorderLayout.CENTER);
        add(runButton, BorderLayout.LINE_END);

        add(threadsLabel, BorderLayout.LINE_START);
        add(threadsTextField, BorderLayout.CENTER);

        add(depthLabel, BorderLayout.LINE_START);
        add(depthTextField, BorderLayout.CENTER);
        add(depthCheckBox, BorderLayout.LINE_END);

        add(bannedAgeLabel, BorderLayout.LINE_START);
        add(bannedAgeTextField, BorderLayout.CENTER);

        add(numberOfLinksLabel, BorderLayout.LINE_START);
        add(numberOfLinksTextField, BorderLayout.CENTER);

        add(elapsedSecondsNameLabel, BorderLayout.LINE_START);
        add(elapsedSecondsLabel, BorderLayout.CENTER);

        add(foundLinksNumberNameLabel, BorderLayout.LINE_START);
        add(foundLinksNumberLabel, BorderLayout.CENTER);

        add(urlTitleLabel, BorderLayout.LINE_START);
        add(exportUrlTextField, BorderLayout.CENTER);
        add(exportButton, BorderLayout.LINE_END);
    }

    private void setUrlTextField(final int width, final int height) {
        urlTextField.setName("UrlTextField");
        urlTextField.setPreferredSize(new Dimension(width, height));
        urlTextField.setVisible(true);
        urlTextField.setText("https://en.wikipedia.org/wiki/Football");
    }

    private void setRunButton() {
        runButton.setName("RunButton");
        runButton.setVisible(true);

        runButton.addActionListener(ev -> {
            try {
                clickRunButton();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void setThreadsTextField(final int width, final int height) {
        threadsTextField.setName("ThreadsTextField");
        threadsTextField.setPreferredSize(new Dimension(width, height));
        threadsTextField.setVisible(true);
        threadsTextField.setText("4");
    }

    private void setDepthTextField(final int width, final int height) {
        depthTextField.setName("DepthTextField");
        depthTextField.setPreferredSize(new Dimension(width, height));
        depthTextField.setVisible(true);
        depthTextField.setText("7");
    }

    private void setDepthCheckBox() {
        depthCheckBox.setName("DepthCheckBox");

        depthCheckBox.addChangeListener(ev -> {
            if (depthCheckBox.isSelected()) {
                depthTextField.setEnabled(true);
            } else {
                depthTextField.setEnabled(false);
                model.resetDepthNumber();
                depthTextField.setText(String.valueOf(model.getDepthNumber()));
            }
        });
    }

    private void setBannedAgeField(final int width, final int height) {
        bannedAgeTextField.setName("Banned ages");
        bannedAgeTextField.setPreferredSize(new Dimension(width, height));
        bannedAgeTextField.setVisible(true);
        bannedAgeTextField.setText("6");
    }


    private void setNumberOfUniqueLinks(final int width, final int height) {
        numberOfLinksTextField.setName("Number of links");
        numberOfLinksTextField.setPreferredSize(new Dimension(width, height));
        numberOfLinksTextField.setVisible(true);
        numberOfLinksTextField.setText(String.valueOf(model.getNumberOfLinksToFind()));
    }

    private void setUrlTitleLabel(final int width, final int height) {
        urlTitleLabel.setName("TitleLabel");
        urlTitleLabel.setPreferredSize(new Dimension(width, height));
        urlTitleLabel.setVisible(true);
    }

    private void setExportUrlTextField(final int width, final int height) {
        exportUrlTextField.setName("ExportUrlTextField");
        exportUrlTextField.setPreferredSize(new Dimension(width, height));
        exportUrlTextField.setVisible(true);
        exportUrlTextField.setText("D:\\crawl\\cs.txt");
    }

    private void setExportButton() {
        exportButton.setName("ExportButton");
        exportButton.setVisible(true);

        exportButton.addActionListener(ev -> clickExportButton());
    }

    private void setFrame(final int width, final int height) {
        setTitle("WebCrawler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setVisible(true);
    }

    private void clickRunButton() throws InterruptedException {
        runButton.setSelected(true);
        elapsedSecondsLabel.setText("0");
        urlTitleLabel.setText(
                WebParsing.parseTitleFromHtmlCode(
                        (urlTextField.getText()))
        );
        model.setCrawlingThreadsNumber(Integer.parseInt(threadsTextField.getText()));
        model.setDepthNumber(Integer.parseInt(depthTextField.getText()));
        model.setBannedAge(bannedAgeTextField.getText());
        model.setNumberOfLinksToFind(Integer.parseInt(numberOfLinksTextField.getText()));

        long startMillis = System.currentTimeMillis();
        model.run(urlTextField.getText());
        long endMillis = System.currentTimeMillis();
        int elapsedTime = (int) ((endMillis - startMillis) / 1000L);
        elapsedSecondsLabel.setText(String.valueOf(elapsedTime));
        foundLinksNumberLabel.setText(String.valueOf(model.getFoundLinksNumber()));
        runButton.setSelected(false);
    }

    private void clickExportButton() {
        model.export(exportUrlTextField.getText());
    }
}