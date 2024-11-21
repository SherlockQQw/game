import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

class Ball {
    int x, y, diameter;
    int ySpeed;

    Ball(int x, int diameter) {
        this.x = x;
        this.y = 0; // Начальная высота
        this.diameter = diameter;
        this.ySpeed = 5; // Начальная скорость падения
    }

    void move() {
        y += ySpeed; // Перемещение шарика вниз
    }

    void reset(int newX) {
        y = 0; // Сброс позиции
        x = newX; // Новая позиция
        ySpeed = 5; // Сброс скорости
    }
}

public class BallBounceGame extends JPanel implements ActionListener {
    private static final int RECTANGLE_WIDTH = 80;
    private static final int RECTANGLE_HEIGHT = 10;
    private int rectangleX = 200;
    private int score = 0;
    private int difficulty = 1; // Уровень сложности
    private Timer timer;
    private ArrayList<Ball> balls; // Список шариков
    private Random random = new Random();

    public BallBounceGame(int difficulty) {
        this.difficulty = difficulty;
        this.balls = new ArrayList<>();

        // Создаем шарики в зависимости от уровня сложности
        for (int i = 0; i < difficulty; i++) {
            balls.add(new Ball(random.nextInt(400), 30 + difficulty * 10));
        }

        timer = new Timer(20, this);
        timer.start();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT && rectangleX > 0) {
                    rectangleX -= 20;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT && rectangleX < getWidth() - RECTANGLE_WIDTH) {
                    rectangleX += 20;
                }
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE); // Белый фон

        // Рисуем шарики
        g.setColor(Color.RED);
        for (Ball ball : balls) {
            g.fillOval(ball.x, ball.y, ball.diameter, ball.diameter);
        }

        // Рисуем прямоугольник
        g.setColor(Color.BLUE);
        g.fillRect(rectangleX, getHeight() - RECTANGLE_HEIGHT, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);

        // Рисуем счет
        g.setColor(Color.BLACK);
        g.drawString("Счет: " + score, 10, 20);
    }

    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < balls.size(); i++) {
            Ball ball = balls.get(i);
            ball.move();

            // Проверяем, отбит ли шарик
            if (ball.y >= getHeight() - RECTANGLE_HEIGHT && ball.x + ball.diameter > rectangleX && ball.x < rectangleX + RECTANGLE_WIDTH) {
                ball.reset(random.nextInt(getWidth() - ball.diameter)); // Сброс позиции шарика
                score++; // Увеличиваем счет
            }

            // Если шарик вышел за пределы экрана
            if (ball.y > getHeight()) {
                JOptionPane.showMessageDialog(this, "Игра окончена! Ваш счет: " + score);
                System.exit(0);
            }
        }
        repaint();
    }

    public static void main(String[] args) {
        String[] options = {"Легкий - 1", "Средний - 2", "Сложный - 3"};
        int difficulty = JOptionPane.showOptionDialog(null,
                "Выберите уровень сложности",
                "Уровень сложности",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]) + 1; // +1 для преобразования индекса в уровень

        JFrame frame = new JFrame("Падение Шариков");
        BallBounceGame game = new BallBounceGame(difficulty);
        frame.add(game);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.requestFocus();
    }
}
