package SnakeGame_YEproject02;

import java.util.LinkedList;

/**
 *【Snake層】
 *蛇を表すクラス
 *
 *【設計意図】
 *蛇の体（Node のリスト）と方向を管理し、移動、成長、衝突判定の責務を担う。
 *Snakeクラスは、蛇のノードリストと移動方向を管理し、移動・成長・衝突判定のロジックを実装します。
 *
 *【責務範囲】
 *·体の初期化
 *·移動ロジック
 *·食物摂取による成長
 *·衝突判定
 *
 *@author ye888000
 *@version 1.0
 *@since 2026-02-05
 */

    public class Snake{
    private LinkedList<Node> body;
    private Direction direction = Direction.LEFT;
    private boolean isLiving=true;

    public Snake(){
        intSnake();
    }

    private void intSnake(){
        body = new LinkedList<>();
        body.add(new Node(16,20));
        body.add(new Node(17,20));
        body.add(new Node(18,20));
        body.add(new Node(19,20));
        body.add(new Node(20,20));
        body.add(new Node(21,20));
    }

    public void move(){
        if(!isLiving){
            return;
        }

        Node head=body.getFirst();
        int newX=head.getX();
        int newY=head.getY();

            if(direction==Direction.UP){
                newY=newY-1;
            }
            if(direction==Direction.DOWN){
                newY=newY+1;
            }
            if(direction==Direction.LEFT){
                newX=newX-1;
            }
            if(direction==Direction.RIGHT){
                newX=newX+1;
            }

        //フィールド外への移動を即ゲームオーバーとして扱うための境界判定。
        if(head.getX()<0||head.getY()<0||head.getX()>=40||head.getY()>=40){
            isLiving=false;
            return;
        }

        //自身との衝突判定：頭部が既存の体節と重なった場合はゲームオーバーとする。
        for (int i=1; i<body.size();i++) {
            Node node=body.get(i);
            if (head.getX()==node.getX()&&head.getY()==node.getY()){
                isLiving=false;
                return;
            }
        }
        //移動処理：新しい頭部を追加し、末尾を削除することで長さを維持する。
        body.addFirst(new Node(newX, newY));
        body.removeLast();
    }

    //蛇を食べたら、その位置に1マス分伸びるので、頭にノードを追加している。
    public void eat(Node food){
        body.addFirst(new Node(food.getX(),food.getY()));
    }

    public Direction getDirection(){
        return direction;
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public LinkedList<Node> getBody(){
        return body;
    }

    public boolean isLiving(){
        return isLiving;
    }
}



