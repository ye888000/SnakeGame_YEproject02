package SnakeGame_YEproject02;

import java.util.Random;

/**
 *【Node層】
 * 蛇の体および食物の位置を表す座標ノードクラス。
 *
 *【設計意図】
 * Snake の体および食物を同一のNodeとして扱い、座標管理を共通化することで処理の単純化を図っている。
 *
 *【責務範囲】
 *·x,y座標の保持。
 *·食物生成時のランダム座標設定。
 *
 *@author ye888000
 *@version 1.0
 *@since 2026-02-06
 */

//指定した座標でノードを生成するコンストラクタ。
public class Node{

        private int x;
        private int y;

    //初期化のみを行うデフォルトコンストラクタ。
         public Node(){
        }

         public Node(int x,int y){
            this.x=x;
            this.y=y;
        }

        public int getX(){
            return x;
        }

        public void setX(int x){
            this.x=x;
        }

        public int getY(){
            return y;
        }

        public void setY(int y){
            this.y=y;
        }

    //食物生成時に使用するランダム座標設定処理。フィールドサイズ40×40を前提とした範囲で座標を生成する。
        public void random(){
            Random r=new Random();
            this.x=r.nextInt(40);
            this.y=r.nextInt(40);
        }
    }


