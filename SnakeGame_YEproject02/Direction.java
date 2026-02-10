package SnakeGame_YEproject02;

/**
 * 移動方向を表す列挙型（Direction）
 *
 *【設計意図 / Design Intent】
 * 蛇の移動方向を「定数の集合」として明確に定義することで、数値や文字列による判定を避け、ロジックの可読性と安全性を高めている。
 *
 *【採用理由 / Why Enum】
 *・方向は有限かつ固定の値であるため、enum が最も適した表現
 *・無効な値が入り込むリスクをコンパイル時に排除できる
 *
 *【利用箇所 / Usage】
 * Snake クラスの移動ロジックおよび、MainFrame におけるキーボード入力制御で使用される。
 *
 * @author ye888000
 * @version 1.0
 * @since 2026-02-04
 */

public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;
}
