package com.example.highandlow;


public class Card {
    //カードの属性(弱いほうから0)
    private int element = 0;
    //数値
    private int number = 0;
    //カードの画像が格納された二重配列
    private int[][]cardImg = {
            {
                    R.drawable.torannpu_illust14,R.drawable.torannpu_illust15,R.drawable.torannpu_illust16,
                    R.drawable.torannpu_illust17,R.drawable.torannpu_illust18,
                    R.drawable.torannpu_illust19, R.drawable.torannpu_illust20,
                    R.drawable.torannpu_illust21,R.drawable.torannpu_illust22,
                    R.drawable.torannpu_illust23,R.drawable.torannpu_illust24,
                    R.drawable.torannpu_illust25, R.drawable.torannpu_illust26
            },
            {
                R.drawable.torannpu_illust27,R.drawable.torannpu_illust28,R.drawable.torannpu_illust29,
                    R.drawable.torannpu_illust30,R.drawable.torannpu_illust31,
                    R.drawable.torannpu_illust32,R.drawable.torannpu_illust33,
                    R.drawable.torannpu_illust34,R.drawable.torannpu_illust35,
                    R.drawable.torannpu_illust36,R.drawable.torannpu_illust37,
                    R.drawable.torannpu_illust38,R.drawable.torannpu_illust39,
            },
            {
                R.drawable.torannpu_illust40,R.drawable.torannpu_illust41,R.drawable.torannpu_illust42,
                    R.drawable.torannpu_illust43,R.drawable.torannpu_illust44,
                    R.drawable.torannpu_illust45,R.drawable.torannpu_illust46,
                    R.drawable.torannpu_illust47,R.drawable.torannpu_illust48,
                    R.drawable.torannpu_illust49,R.drawable.torannpu_illust50,
                    R.drawable.torannpu_illust51,R.drawable.torannpu_illust52
            },
            {
                R.drawable.torannpu_illust1,R.drawable.torannpu_illust2,R.drawable.torannpu_illust3,
                    R.drawable.torannpu_illust4,R.drawable.torannpu_illust5,
                    R.drawable.torannpu_illust6,R.drawable.torannpu_illust7,
                    R.drawable.torannpu_illust8,R.drawable.torannpu_illust9,
                    R.drawable.torannpu_illust10,R.drawable.torannpu_illust11,
                    R.drawable.torannpu_illust12,R.drawable.torannpu_illust13
            }
    };
    //カードの裏の画像
    static int backCard = R.drawable.trump_back;

    //コンストラクタ
    public Card(int element,int number){
        this.element = element;
        this.number = number;
    }

    //属性を返すメソッド
    public int getElement() {
        return element;
    }
    //数字を返すメソッド
    public int getNumber() {
        return number;
    }
    //表画像を返すメソッド
    public int getImg(){
        return cardImg[this.element][this.number];
    }
}
