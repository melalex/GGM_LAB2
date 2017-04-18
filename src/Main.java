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

    private double angle = 0;

    private double scale = 1;
    private double delta = 0.01;

    private double dx = 1;
    private double tx = 0;
    private double dy = 1;
    private double ty = 6;

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

        g2d.rotate(angle, sceneX, sceneY);
        //g2d.scale(scale, 0.99);

        g2d.setColor(Color.GREEN);
        g2d.fillRect(sceneX, sceneY, SCENE_WIDTH, SCENE_HEIGHT);

        BasicStroke faceStroke = new BasicStroke(FACE_LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(faceStroke);
        g2d.setColor(Color.RED);

        g2d.drawPolyline(
                new int[]{
                        sceneX + MARGIN,
                        sceneX + BOT_MARGIN + MARGIN,
                        sceneX + BOT_WIDTH + BOT_MARGIN + MARGIN,
                        sceneX + BOT_WIDTH + 2 * BOT_MARGIN
                },
                new int[]{
                        sceneY + MARGIN,
                        sceneY + SCENE_HEIGHT - MARGIN,
                        sceneY + SCENE_HEIGHT - MARGIN,
                        sceneY + MARGIN
                },
                4
        );

        BasicStroke eyeStroke = new BasicStroke(EYE_LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(eyeStroke);
        g2d.setColor(Color.BLUE);

        final int endEye1X = sceneX + MARGIN + EYE_WIDTH;
        g2d.drawLine(
                sceneX + MARGIN + EYE1_MARGIN,
                sceneY + MARGIN,
                endEye1X,
                sceneY + MARGIN
        );

        final int startEye2X = sceneX + SCENE_WIDTH - EYE2_MARGIN - EYE_WIDTH;
        g2d.drawLine(
                startEye2X,
                sceneY + MARGIN,
                sceneX + SCENE_WIDTH - EYE2_MARGIN - MARGIN,
                sceneY + MARGIN
        );

        final int noseStartX = (endEye1X + startEye2X) / 2;
        final int endNose1X = (int) divideLine(noseStartX, sceneY + BOT_MARGIN + MARGIN, NOSE_LAMBDA);
        final int endNose1Y = (int) divideLine(sceneY + MARGIN, sceneY + SCENE_HEIGHT - MARGIN, NOSE_LAMBDA);
        final int endNose2X = (int) divideLine(noseStartX, sceneY + BOT_WIDTH + BOT_MARGIN + MARGIN, NOSE_LAMBDA);
        final int endNose2Y = (int) divideLine(sceneY + MARGIN, sceneY + SCENE_HEIGHT - MARGIN, NOSE_LAMBDA);

        BasicStroke noseStroke = new BasicStroke(NOSE_LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
        g2d.setStroke(noseStroke);
        GradientPaint gp = new GradientPaint(noseStartX,
                sceneY + MARGIN,
                Color.YELLOW,
                endNose2X,
                endNose2Y,
                Color.BLUE,
                true
        );
        g2d.setPaint(gp);

        Polygon nose = new Polygon();
        nose.addPoint(noseStartX, sceneY + MARGIN);
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
        scale += delta;
        angle += 0.01;

        repaint();
    }
}