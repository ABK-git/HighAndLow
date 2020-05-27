package com.example.highandlow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighLowActivity extends AppCompatActivity {

    //データベース
    private CustomOpenHelper customOpenHelper = null;

    //持っているチップデータを取り出すための名前
    private String yourName = "myName";

    //山札
    List<Card> list = new ArrayList<Card>();
    //ImageViewを取得
    ImageView question = null;
    ImageView answer = null;

    //ImageViewに入れるカード
    Card questionCard = null;
    Card answerCard = null;

    //ベット用のNumberPicker
     LinearLayout layout = null;
     NumberPicker betChip = null;

    //Upボタン
    ImageView upImg = null;
    //Downボタン
    ImageView downImg = null;

    //持っているチップ
    int chip = 0;
    //持っているチップ数を表示するTextView
    TextView haveChips = null;

    //かけたチップ
    int bet = 0;
    //かけているチップ数を表示するTextView
    TextView textBet = null;
    //掛金を決定するボタン
    Button decideChip = null;

    //選択肢がUpかDownか
    boolean updown = false;
    //正解の回答
    boolean trueUpDown = false;

    //正解時の質問レイアウト
    LinearLayout layoutTrue = null;
    //不正解時の質問レイアウト
    LinearLayout layoutFalse = null;

    //効果音再生
    MySound mySound;
    //効果音配列
    int[]mp3 = {R.raw.correct1,R.raw.incorrect1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_low);

        //効果音を取得する
        mySound = new MySound();
        mySound.onCreate(this,mp3);

        //データベースを取得
        customOpenHelper = new CustomOpenHelper(this);

        //チップ数を取得する
        chip = customOpenHelper.getChip(yourName);
        //チップを持っていなかった場合
        if(chip == 0){
            chip = 50;
        }

        //Viewをセット
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);

        //NumberPickerを保有するレイアウト
        layout = findViewById(R.id.linear);
        //NumberPickerの数字を表示する
        betChip = findViewById(R.id.betchip);
        //掛金表示Textの取得
        textBet = findViewById(R.id.chips);

        //保有チップ表示Textを取得
        haveChips = findViewById(R.id.haveChipsSheets);
        //保有チップ数を表示する
        haveChips.setText(String.valueOf(chip));

        //掛金決定ボタン
        decideChip = findViewById(R.id.bet);

        //Upボタン取得
        upImg = findViewById(R.id.up);
        //Downボタン取得
        downImg = findViewById(R.id.down);

        //正解時のレイアウト取得
        layoutTrue = findViewById(R.id.tryWup);
        //不正解時のレイアウト取得
        layoutFalse = findViewById(R.id.tryContinue);

        //初期設定
        init();
    }


    //チップのかけ数を設定
    private void setBetChip(){
        betChip.setMaxValue(chip);
        betChip.setMinValue(1);
        //初期値は1にする
        betChip.setValue(1);
        //Textの色を変える
        betChip.setTextColor(Color.RED);
        //可視化
        layout.setVisibility(View.VISIBLE);
    }
    //掛金を決定して、Gameを始める
    public void gameStart(View v){
        //掛金が確定
        bet = betChip.getValue();
        //かけた分保有チップから減らす
        chip -= bet;
        //かけたチップを表示
        textBet.setText(String.valueOf(bet));
        //保有チップを表示
        haveChips.setText(String.valueOf(chip));

        //NumberPickerを不可視化
        layout.setVisibility(View.INVISIBLE);

        //UpとDownを可視化する
        visibleUpDown();
    }
    //選択肢UP
    public void onUp(View v){
        updown = true;
        getAnswer();
    }
    //選択肢Down
    public void onDown(View v){
        updown = false;
        getAnswer();
    }
    //答え合わせ
    private void getAnswer(){
        //UPとDOWNを不可視にする
        goneVisibleUpDown();
        //答えをめくる
        answer.setImageResource(answerCard.getImg());
        //文字も答えのほうが大きかった場合
        if(questionCard.getNumber() < answerCard.getNumber()){
            trueUpDown = true;
        }else if(questionCard.getNumber() > answerCard.getNumber()){
            trueUpDown = false;
        }else{//引き分けだった場合
            if(questionCard.getElement() < answerCard.getElement()){
                trueUpDown = true;
            }else{
                trueUpDown = false;
            }
        }

        //予想と答えが一致していた場合
        if(updown == trueUpDown){
            gameWin();
            mySound.onPlay(0);
        }else{//外れの場合
            gameLose();
            mySound.onPlay(1);
        }
    }
    //正解時のToastの生成
    public void makeTrueToast(){
        //インフレータ取得
        LayoutInflater inflater = getLayoutInflater();
        //ViewGroupの取得
        ViewGroup viewGroup = findViewById(R.id.winToast);
        //Inflateする
        View view = inflater.inflate(R.layout.toast_win,viewGroup);

        //Toastの生成
        Toast toast = new Toast(this);
        toast.setView(view);
        //中央表示
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
    //不正解時のToastの生成
    public void makeFalseToast(){
        //インフレータ取得
        LayoutInflater inflater = getLayoutInflater();
        //ViewGroupの取得
        ViewGroup viewGroup = findViewById(R.id.loseToast);
        //Inflateする
        View view = inflater.inflate(R.layout.toast_lose,viewGroup);

        //Toastの生成
        Toast toast = new Toast(this);
        toast.setView(view);
        //中央表示
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
    //退去時のToastの生成
    public void makeLostToast(){
        //インフレータ取得
        LayoutInflater inflater = getLayoutInflater();
        //ViewGroupの取得
        ViewGroup viewGroup = findViewById(R.id.lostToast);
        //Inflateする
        View view = inflater.inflate(R.layout.toast_bye,viewGroup);

        //Toastの生成
        Toast toast = new Toast(this);
        toast.setView(view);
        //中央表示
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
    //UPとDOWNの画像を不可視化する
    public void goneVisibleUpDown(){
        upImg.setVisibility(View.GONE);
        downImg.setVisibility(View.GONE);
    }
    //UPとDOWNの画像を可視化する
    public void visibleUpDown(){
        upImg.setVisibility(View.VISIBLE);
        downImg.setVisibility(View.VISIBLE);
    }

    //正解したときの処理
    private void gameWin(){
        //かけたチップが2倍になる
        bet *= 2;
        //Textの更新
        textBet.setText(String.valueOf(bet));
        makeTrueToast();
        //Toastが消えたら質問レイアウトを表示する
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //リストが0になった場合初期化する
                if(list.size() == 0){
                    init();
                }else{
                    //questionLayoutの表示
                    layoutTrue.setVisibility(View.VISIBLE);
                }
            }
        },2100);
    }
    //Wupに挑戦する
    public void onTry(View v){
        //questionLayoutを非表示する
        layoutTrue.setVisibility(View.GONE);
        //カードを進める
        tryWup();
    }
    //Wupを選択した場合の処理
    private void tryWup(){
        //Answerカードをquestionカードにする
        questionCard = answerCard;
        //questionカードを新しく配る
        answerCard = list.get(0);
        //デッキから配ったカードを削除する
        list.remove(0);
        //questionカードをめくる
        question.setImageResource(questionCard.getImg());
        //answerカードを裏にする
        answer.setImageResource(Card.backCard);
        //UpかDownかを表示する
        visibleUpDown();
    }
    //Retireする
    public void onRetire(View v){
        //かけたチップを保有チップに統合する
        chip += bet;
        //保有チップのTextを更新する
        haveChips.setText(String.valueOf(chip));
        //questionLayoutを非表示にする
        layoutTrue.setVisibility(View.GONE);
        //初期化する
        init();
    }
    //不正解の時の処理
    private void gameLose(){
        //かけたチップが没収される
        bet = 0;
        //textの更新
        textBet.setText(String.valueOf(bet));
        //チップが0ではない場合
        if(chip != 0){
            makeFalseToast();
            //Toastが消えたら質問レイアウトを表示する
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    //questionLayoutの表示
                    layoutFalse.setVisibility(View.VISIBLE);
                }
            },2100);
        }else{
            makeLostToast();
            //強制退去(1文無し)
            customOpenHelper.deleteChip(yourName);
            finish();
        }
    }
    //Topに戻る
    public void onTop(View v){
        if(layoutFalse.getVisibility() == View.VISIBLE){
            //questionLayoutを非表示にする
            layoutFalse.setVisibility(View.GONE);
        }
        if(bet != 0){
            chip += bet;
        }
        //持っているチップ数を登録する
        customOpenHelper.updateChip(yourName,chip);
        //メニュー画面にもどる
        finish();
    }
    //再挑戦する
    public void onRetry(View v){
        //questionLayoutを非表示にする
        layoutFalse.setVisibility(View.GONE);
        //ゲームを初期化
        init();
    }

    //53枚のカードを生成する
    private void makeDekki(){
        for(int i = 0; i < 4; i++){
            for(int j = 0;j < 13; j++){
                list.add(new Card(i,j));
            }
        }
        Collections.shuffle(list);
    }

    //初期設定
    private void init(){
        //山札を生成してシャッフルする
        makeDekki();
        //ゲームの最初はかけているチップはない
        bet = 0;
        textBet.setText(String.valueOf(bet));
        //カードを配る
        questionCard = list.get(0);
        list.remove(0); //山札から1枚削除

        answerCard = list.get(0);
        list.remove(0); //山札から1枚削除

        //questionCardをめくる
        question.setImageResource(questionCard.getImg());
        //Answerカードを裏にする
        answer.setImageResource(Card.backCard);

        //掛金設定
        setBetChip();
    }
}
