import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Exercise22_05 extends JApplet {
  public Exercise22_05() {
    add(new BallControl());
  }

  class BallPanel extends JPanel {
    private int delay = 10;
    private Timer timer = new Timer(delay, new TimerListener());
    ArrayList<Ball> ballList = new ArrayList<Ball>();

    public BallPanel() {
      timer.start();
      
      this.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          for (int i = 0; i < ballList.size(); i++) {
            if (ballList.get(i).isInside(e.getX(), e.getY())) {
              ballList.remove(i);
              repaint();
            }
          }          
        }
      });
    }

    private class TimerListener implements ActionListener {
      @Override 
      public void actionPerformed(ActionEvent e) {
        repaint();
      }
    }

    @Override
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      for (int i = 0; i < ballList.size(); i++) {
        for (int j = i + 1; j < ballList.size(); j++) {
          if (i != j) {
            ballList.get(i).checkCollide(ballList.get(j).getX(),
                ballList.get(j).getY(), ballList.get(j).getRad());
            if (ballList.get(i).checkCollideBool(ballList.get(j).getX(),
                ballList.get(j).getY(), ballList.get(j).getRad()) == 1) {
              ballList.remove(ballList.get(j));
            }
          }
        }
      }

      for (int i = 0; i < ballList.size(); i++) {
        g.setColor(ballList.get(i).getColor());
        g.fillOval(ballList.get(i).getX() - ballList.get(i).getRad(), ballList
            .get(i).getY() - ballList.get(i).getRad(), 2 * ballList.get(i)
            .getRad(), 2 * ballList.get(i).getRad());
        ballList.get(i).setWidth(getWidth());
        ballList.get(i).setHeight(getHeight());
        ballList.get(i).moveX();
        ballList.get(i).moveY();
      }
    }

    public void move() {
      timer.start();
    }

    public void suspend() {
      timer.stop();
    }

    public void resume() {
      timer.start();

    }

    public void addOne() {
      ballList.add(new Ball());
    }

    public void minusOne() {
      ballList.remove(ballList.size() - 1);
    }

    public void setDelay(int delay) {
      this.delay = delay;
      timer.setDelay(delay);
    }
  }

  class Ball {
    public int x = 5;
    public int y = 5;
    public int radius = 10;
    public int dx = 2;
    public int dy = 2;
    public int width;
    public int height;

    Color color = new Color((int) (Math.random() * 128),
        (int) (Math.random() * 128), (int) (Math.random() * 128));

    public void setWidth(int w) {
      this.width = w;
    }

    public void setHeight(int w) {
      this.height = w;
    }

    public boolean isInside(int x1, int y1) {
      return Math.sqrt((x1 - x) * (x1- x) + (y1 - y) * (y1 - y)) <= radius;
    }

    public void moveX() {
      x += dx;
      if (x - radius < 0)
        dx = Math.abs(dx);
      if (x > width - radius)
        dx = -Math.abs(dx);
    }

    public void moveY() {
      y += dy;
      if (y - radius < 0)
        dy = Math.abs(dy);
      if (y > height - radius)
        dy = -Math.abs(dy);
    }

    public void checkCollide(int x2, int y2, int rad) {
      int x3 = (x2 + rad) - (x + rad);
      int y3 = (y2 + rad) - (y + rad);
      int hyp = (int) (Math.sqrt(Math.pow(x3, 2) + Math.pow(y3, 2)));
      if (hyp <= rad + radius) {
        radius = rad + radius;
      }
    }

    public int checkCollideBool(int x2, int y2, int rad) {
      int x3 = (x2 + rad) - (x + rad);
      int y3 = (y2 + rad) - (y + rad);
      int hyp = (int) (Math.sqrt(Math.pow(x3, 2) + Math.pow(y3, 2)));
      if (hyp <= rad + radius) {
        return 1;
      } else
        return 0;
    }

    public int getX() {
      return this.x;
    }

    public int getRad() {
      return this.radius;
    }

    public int getY() {
      return this.y;
    }

    public Color getColor() {
      return this.color;
    }

  }

  class BallControl extends JPanel {
    private BallPanel ballPanel = new BallPanel();
    private JButton jbtSuspend = new JButton("Suspend");
    private JButton jbtResume = new JButton("Resume");
    private JButton jbtAddOne = new JButton("+1");
    private JButton jbtMinusOne = new JButton("-1");
    private JScrollBar jsbDelay = new JScrollBar();

    public BallControl() {
      JPanel panel = new JPanel();
      panel.add(jbtSuspend);
      panel.add(jbtResume);
      panel.add(jbtAddOne);
      panel.add(jbtMinusOne);

      ballPanel.setBorder(new javax.swing.border.LineBorder(Color.red));
      jsbDelay.setOrientation(JScrollBar.HORIZONTAL);
      ballPanel.setDelay(jsbDelay.getMaximum());
      setLayout(new BorderLayout());
      add(jsbDelay, BorderLayout.NORTH);
      add(ballPanel, BorderLayout.CENTER);
      add(panel, BorderLayout.SOUTH);

      jbtSuspend.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          ballPanel.suspend();
        }
      });

      jbtResume.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          ballPanel.resume();
        }
      });

      jbtAddOne.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          ballPanel.addOne();
        }
      });

      jbtMinusOne.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          ballPanel.minusOne();
        }
      });

      jsbDelay.addAdjustmentListener(new AdjustmentListener() {
        @Override
        public void adjustmentValueChanged(AdjustmentEvent e) {
          ballPanel.setDelay(jsbDelay.getMaximum() - e.getValue());
        }
      });
    }
  }

  public static void main(String[] args) {
    Exercise22_05 applet = new Exercise22_05();
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Combining Colliding Balls");
    frame.add(applet, BorderLayout.CENTER);
    frame.setSize(400, 320);
    frame.setVisible(true);
  }
}