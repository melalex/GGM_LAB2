import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;

@SuppressWarnings("serial")
public class Main extends JPanel implements ActionListener {
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 700;

    private static final int SCENE_WIDTH = 225;
    private static final int SCENE_HEIGHT = 175;
    private static final int MARGIN = 10;
    private static final int BOT_MARGIN = 50;
    private static final int FRAME_LINE_WIDTH = 3;
    private static final int FACE_LINE_WIDTH = 3;
    private static final int EYE_LINE_WIDTH = 3;
    private static final int NOSE_LINE_WIDTH = 1;
    private static final int BOT_WIDTH = SCENE_WIDTH - 2 * (MARGIN + BOT_MARGIN) + 2 * FACE_LINE_WIDTH;
    private static final int FRAME_SIDE = (int) round(sqrt(SCENE_WIDTH * SCENE_WIDTH + SCENE_HEIGHT * SCENE_HEIGHT));

    private static final int EYE_WIDTH = 80;
    private static final int EYE1_MARGIN = 30;
    private static final int EYE2_MARGIN = 40;

    private static final int NOSE_LAMBDA = 2;

    private static final double ROTATE_DELTA = 0.01;

    private double scaleDelta = -0.01;
    private double scaleX = 1;
    private double scaleY = 1;

    private double angle = 0;

    private static int maxWidth;
    private static int maxHeight;

    private Main() {
    }

    public static Main create() {
        Main instance = new Main();

        Timer timer = new Timer(10, instance);
        timer.start();

        return instance;
    }

    @Override
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

        final int sceneX = maxWidth / 2;
        final int sceneY = maxHeight / 2;

        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0, 0, maxWidth, maxHeight);

        BasicStroke frameStroke = new BasicStroke(FRAME_LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(frameStroke);
        g2d.setColor(Color.BLACK);

        g2d.drawRect(
                sceneX - FRAME_SIDE,
                sceneY - FRAME_SIDE,
                2 * FRAME_SIDE,
                2 * FRAME_SIDE
        );

        g2d.translate(sceneX, sceneY);
        g2d.scale(scaleX, scaleY);
        g2d.rotate(angle);

        g2d.setColor(Color.GREEN);
        g2d.fillRect(0, 0, SCENE_WIDTH, SCENE_HEIGHT);

        BasicStroke faceStroke = new BasicStroke(FACE_LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(faceStroke);
        g2d.setColor(Color.RED);

        g2d.drawPolyline(
                new int[]{
                        MARGIN,
                        BOT_MARGIN + MARGIN,
                        BOT_WIDTH + BOT_MARGIN + MARGIN,
                        BOT_WIDTH + 2 * BOT_MARGIN
                },
                new int[]{
                        MARGIN,
                        SCENE_HEIGHT - MARGIN,
                        SCENE_HEIGHT - MARGIN,
                        MARGIN
                },
                4
        );

        BasicStroke eyeStroke = new BasicStroke(EYE_LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(eyeStroke);
        g2d.setColor(Color.BLUE);

        final int endEye1X = MARGIN + EYE_WIDTH;
        g2d.drawLine(
                MARGIN + EYE1_MARGIN,
                MARGIN,
                endEye1X,
                MARGIN
        );

        final int startEye2X = SCENE_WIDTH - EYE2_MARGIN - EYE_WIDTH;
        g2d.drawLine(
                startEye2X,
                MARGIN,
                SCENE_WIDTH - EYE2_MARGIN - MARGIN,
                MARGIN
        );

        final int noseStartX = (endEye1X + startEye2X) / 2;
        final int endNose1X = (int) divideLine(noseStartX, BOT_MARGIN + MARGIN, NOSE_LAMBDA);
        final int endNose1Y = (int) divideLine(MARGIN, SCENE_HEIGHT - MARGIN, NOSE_LAMBDA);
        final int endNose2X = (int) divideLine(noseStartX, BOT_WIDTH + BOT_MARGIN + MARGIN, NOSE_LAMBDA);
        final int endNose2Y = (int) divideLine(MARGIN, SCENE_HEIGHT - MARGIN, NOSE_LAMBDA);

        BasicStroke noseStroke = new BasicStroke(NOSE_LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(noseStroke);
        GradientPaint gp = new GradientPaint(noseStartX,
                MARGIN,
                Color.YELLOW,
                endNose2X,
                endNose2Y,
                Color.BLUE,
                true
        );
        g2d.setPaint(gp);

        Polygon nose = new Polygon();
        nose.addPoint(noseStartX, MARGIN);
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

        Dimension size = frame.getSize();
        Insets insets = frame.getInsets();
        maxWidth = size.width - insets.left - insets.right - 1;
        maxHeight = size.height - insets.top - insets.bottom - 1;

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        angle += ROTATE_DELTA;
        scaleX += scaleDelta;
        scaleY += scaleDelta;

        if (scaleX <= 0 || scaleY <= 0 || scaleX >= 1 || scaleY >= 1) {
            scaleDelta = -scaleDelta;
        }

        repaint();
    }
}