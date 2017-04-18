import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

@SuppressWarnings("serial")
public class Main extends JPanel implements ActionListener {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;
    private static final int SCENE_X = 100;
    private static final int SCENE_Y = 125;

    private static final int SCENE_WIDTH = 300;
    private static final int SCENE_HEIGHT = 250;
    private static final int MARGIN = 10;
    private static final int BOT_MARGIN = 50;
    private static final int FACE_LINE_WIDTH = 3;
    private static final int EYE_LINE_WIDTH = 3;
    private static final int NOSE_LINE_WIDTH = 1;
    private static final int BOT_WIDTH = SCENE_WIDTH - 2 * (MARGIN + BOT_MARGIN) + 2 * FACE_LINE_WIDTH;

    private static final int EYE_WIDTH = 80;
    private static final int EYE1_MARGIN = 30;
    private static final int EYE2_MARGIN = 40;

    private static final int NOSE_LAMBDA = 2;

    private Main() {
    }

    public static Main create() {
        Main instance = new Main();

        Timer timer = new Timer(10, instance);
        timer.start();

        return instance;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );
        rh.put(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );
        g2d.setRenderingHints(rh);

        g2d.setBackground(Color.GREEN);
        g2d.clearRect(SCENE_X, SCENE_Y, SCENE_WIDTH, SCENE_HEIGHT);

        BasicStroke faceStroke = new BasicStroke(FACE_LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(faceStroke);
        g2d.setColor(Color.RED);

        g2d.drawPolyline(
                new int[]{
                        SCENE_X + MARGIN,
                        SCENE_X + BOT_MARGIN + MARGIN,
                        SCENE_X + BOT_WIDTH + BOT_MARGIN + MARGIN,
                        SCENE_X + BOT_WIDTH + 2 * BOT_MARGIN
                },
                new int[] {
                        SCENE_Y + MARGIN,
                        SCENE_Y + SCENE_HEIGHT - MARGIN,
                        SCENE_Y + SCENE_HEIGHT - MARGIN,
                        SCENE_Y + MARGIN
                },
                4
        );

        BasicStroke eyeStroke = new BasicStroke(EYE_LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(eyeStroke);
        g2d.setColor(Color.BLUE);

        final int endEye1X = SCENE_X + MARGIN + EYE_WIDTH;
        g2d.drawLine(
                SCENE_X + MARGIN + EYE1_MARGIN,
                SCENE_Y + MARGIN,
                endEye1X,
                SCENE_Y + MARGIN
        );

        final int startEye2X = SCENE_X + SCENE_WIDTH - EYE2_MARGIN - EYE_WIDTH;
        g2d.drawLine(
                startEye2X,
                SCENE_Y + MARGIN,
                SCENE_X + SCENE_WIDTH - EYE2_MARGIN - MARGIN,
                SCENE_Y + MARGIN
        );

        final int noseStartX = (endEye1X + startEye2X) / 2;
        final int endNose1X = (int) divideLine(noseStartX, SCENE_Y + BOT_MARGIN + MARGIN, NOSE_LAMBDA);
        final int endNose1Y = (int) divideLine(SCENE_Y + MARGIN, SCENE_Y + SCENE_HEIGHT - MARGIN, NOSE_LAMBDA);
        final int endNose2X = (int) divideLine(noseStartX, SCENE_Y + BOT_WIDTH + BOT_MARGIN + MARGIN, NOSE_LAMBDA);
        final int endNose2Y = (int) divideLine(SCENE_Y + MARGIN, SCENE_Y + SCENE_HEIGHT - MARGIN, NOSE_LAMBDA);

        BasicStroke noseStroke = new BasicStroke(NOSE_LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(noseStroke);
        GradientPaint gp = new GradientPaint(noseStartX,
                SCENE_Y + MARGIN,
                Color.YELLOW,
                endNose2X,
                endNose2Y,
                Color.BLUE,
                true
        );
        g2d.setPaint(gp);

        Polygon nose = new Polygon();
        nose.addPoint(noseStartX, SCENE_Y + MARGIN);
        nose.addPoint(endNose1X, endNose1Y);
        nose.addPoint(endNose2X, endNose2Y);

        g2d.fill(nose);
    }

    private double divideLine(double c1, double c2, double lambda) {
        return (c1 + c2 * lambda) / (1 + lambda);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Lab2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(create());

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}