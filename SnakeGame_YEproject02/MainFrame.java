package SnakeGame_YEproject02;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Graphics;
import java.awt.Color;

/**
 * ゲーム画面表示と全体処理の制御を担当するメインウィンドウクラス
 *
 * 【设计意图】
 * このクラスは Snake Game のメインウィンドウとして、画面表示、ユーザー入力、ゲーム全体の基本制御を担当する。
 * 蛇の移動、成長、衝突判定などの処理は Snake クラスに分離しており、クラスごとの責務を明確にしている。
 *
 * 【职责范围】
 *・ゲームウィンドウの初期化とパネル描画
 *・Timer でゲーム進行のタイミングを制御
 *・画面の再描画をトリガーして最新のゲーム状態を反映
 *・キーボード入力を受け取り、方向変更指示を Snake に伝える
 *
 * 【设计上的取舍】
 * Timer で定期的にゲーム状態を更新し、ループ処理による画面応答遅延やプログラムのフリーズを回避
 * 描画ロジックを JPanel 内に集中させ、repaint() で刷新タイミングを統一
 * MainFrame はゲームルールを直接実装せず、制御・調整層としてのみ存在
 *
 * 【协作类】
 *・Snake：蛇の移動、成長、存活状態判定を担当
 *・Node：蛇の体と食べ物の座標ノードを表現
 *・Direction：蛇の移動方向を限定する列挙型
 *
 * @author ye888000
 * @version 1.0
 * @since 2026-02-04
 */


public class MainFrame extends JFrame {
    //ゲームロジックを担う Snake インスタンス。表示制御層から操作指示のみを行う設計とする。
    //承担游戏核心逻辑的 Snake 实例。显示控制层仅向其发送操作指令。
    private Snake snake;

     //ゲーム進行を一定間隔で駆動するための Timer。UI スレッドをブロックしない更新方式を採用している。
     //用于以固定时间间隔驱动游戏流程的定时器。采用非阻塞方式以避免影响 UI 线程。
    private Timer timer;

    //ゲーム描画専用の JPanel。描画責務を明確に分離するため、JFrame 直下では描画処理を行わない。
    //专用于游戏绘制的 JPanel。为明确绘制职责，避免在 JFrame 中直接处理绘制逻辑。
    private JPanel jPanel;

    //フィールド上に表示される食物の座標情報。状態保持のみを行い、描画および判定処理は他コンポーネントに委譲する。
    //表示在游戏区域内的食物坐标信息。仅负责状态数据的保持，绘制与判定由其他组件处理。
    private Node food;


    //ゲーム起動時の初期状態を一括で構築するためのコンストラクタ。初期化順序を明示することで、状態不整合の発生を防ぐことを目的としている。
    //用于在游戏启动时统一构建初始状态的构造函数,通过明确初始化顺序，避免状态不一致等问题的发生。
    public MainFrame() {
        initFrame();
        initGamePanel();
        initSnake();
        initFood();
        initTimer();
        setVisible(true);
        setKeyListener();
    }


     //ウィンドウサイズおよび表示位置を固定することで、ユーザー操作や実行環境差異によるレイアウト崩れを防止する。
     //通过固定窗口尺寸与位置，避免因用户操作或运行环境差异导致界面布局异常。
    private void initFrame() {
        setTitle("Snake Game");
        setSize(610, 648);
        setLocation(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }


     //ゲーム画面の描画を一箇所に集約し、蛇、食物、グリッド、ゲーム終了表示などの描画タイミングを統一管理することを目的としている。
     //将游戏绘制集中管理，统一控制蛇、食物、网格线及游戏结束提示的绘制时机。
    private void initGamePanel() {
        jPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);

                // グリッド描画 / 绘制网格
                for (int i = 0; i <= 40; i++) {
                    g.drawLine(0, i * 15, 600, i * 15);
                }
                for (int i = 0; i <= 40; i++) {
                    g.drawLine(i * 15, 0, i * 15, 600);
                }

                // 蛇描画 / 绘制蛇
                if (snake != null) {
                    g.setColor(Color.BLACK);
                    for (Node node : snake.getBody()) {
                        g.fillRect(node.getX() * 15, node.getY() * 15, 15, 15);
                    }
                }
                // 食物描画 / 绘制食物
                if (food != null) {
                    g.setColor(Color.RED);
                    g.fillRect(food.getX() * 15, food.getY() * 15, 15, 15);
                }
                // ゲーム終了表示 / 游戏结束提示
                if (snake != null && !snake.isLiving()) {
                    g.setColor(Color.RED);
                    g.drawString("GAME OVER", 200, 300);
                }
            }
        };

        add(jPanel);
    }

    private void initSnake() {
        snake = new Snake();
    }

    //ゲーム開始時に食物を生成し、初期位置はランダム配置とする。
    //游戏开始时生成食物,初始位置采用随机配置。
    private void initFood() {
        food = new Node();
        food.random();
    }


    //ゲームを一定の速度で進めるため、Timer を使って定期的に処理を実行している。
    //为了让游戏以固定速度运行，使用 Timer 定期执行处理。
    private void initTimer() {
        timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (snake.isLiving()) {
                    snake.move();
                    Node head = snake.getBody().getFirst();
                    if (head.getX() == food.getX() && head.getY() == food.getY()) {
                        snake.eat(food);
                        food.random();
                    }
                }
                jPanel.repaint();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 100);
    }


    //キーボード入力を受け取り、蛇の移動方向を変更するための入力設定処理。
    //为接收键盘输入并控制蛇的移动方向而进行的输入设置处理。
    private void setKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {

                    case KeyEvent.VK_UP:
                        // 反対方向への即時切り替えを防止するための制御
                        // 为防止立即反向移动而进行的方向限制
                        if (snake.getDirection() != Direction.DOWN) {
                            snake.setDirection(Direction.UP);
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (snake.getDirection() != Direction.UP) {
                            snake.setDirection(Direction.DOWN);
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if (snake.getDirection() != Direction.RIGHT) {
                            snake.setDirection(Direction.LEFT);
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (snake.getDirection() != Direction.LEFT) {
                            snake.setDirection(Direction.RIGHT);
                        }
                        break;
                }
            }
        });
    }
    public static void main(String[] args) {
        new MainFrame();
    }
}
