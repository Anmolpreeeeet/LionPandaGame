package com.calculator.lionpanda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    enum Player {
        ONE , TWO , NO
    }
    private Button btnReset;
    private TextView playerTurn;
    private GridLayout gridLayout;
    Player currentPlayer = Player.ONE;
    Player[] playerChoices = new Player[]{Player.NO, Player.NO, Player.NO, Player.NO, Player.NO, Player.NO, Player.NO, Player.NO, Player.NO};
    int[][] winnerRowCol = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    private boolean gameOver = false;
    private boolean noOneWon = true;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnReset = findViewById(R.id.btnReset);
        playerTurn = findViewById(R.id.playerTurn);
        gridLayout = findViewById(R.id.gridLayout);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTheGame();
            }
        });
    }
    public void imgViewIsTapped(View imageView) {
        imageView.clearAnimation();
        ImageView tappedImageView = (ImageView) imageView;

        // to get the position of image tapped
        int tiTag = Integer.parseInt(tappedImageView.getTag().toString());

        if(playerChoices[tiTag] == Player.NO && !gameOver) {
            tappedImageView.setTranslationX(-800);
            playerChoices[tiTag] = currentPlayer;

            if (currentPlayer == Player.ONE) {
                tappedImageView.setImageResource(R.drawable.loin);
                playerTurn.setText("Player : Panda");
                currentPlayer = Player.TWO;
            } else if (currentPlayer == Player.TWO) {
                tappedImageView.setImageResource(R.drawable.panda);
                playerTurn.setText("Player : Lion");
                currentPlayer = Player.ONE;
            }
            // to bring back images
            tappedImageView.animate().translationXBy(800).alpha(1).rotation(1440).setDuration(400);

            // to check who won
            for (int[] winnerColumns : winnerRowCol) {
                if (playerChoices[winnerColumns[0]] == playerChoices[winnerColumns[1]]
                        && playerChoices[winnerColumns[1]] == playerChoices[winnerColumns[2]]
                        && playerChoices[winnerColumns[0]] != Player.NO) {

                    btnReset.setVisibility(View.VISIBLE);
                    String winnerOfGame = "";
                    gameOver = true;
                    noOneWon = false;
                    rotation();
                    if (currentPlayer == Player.ONE) {
                        winnerOfGame = "Player Panda ";
                    } else {
                        winnerOfGame = "Player Lion ";
                    }
                    playerTurn.setText(winnerOfGame + "WON!!!");
                    Toast.makeText(this, winnerOfGame + "is the Winner", Toast.LENGTH_SHORT).show();
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.rickroll);
                    mediaPlayer.start();
                    break;
                }
            }
            noPlayerWon();
        }


    }
    //rotation on winning
    private void rotation() {

        for(int i = 0 ; i < gridLayout.getChildCount() ; i++){
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.clearAnimation();
            imageView.animate().rotationY(imageView.getRotationY() + 1080).setDuration(2500);
        }
    }
    //nobody won
    private void noPlayerWon() {
        boolean flag = false;
        for(int i = 0 ; i < gridLayout.getChildCount() ; i++) {
            if (playerChoices[i] == Player.NO){
                flag = true;
                break;
            }
        }
        if(flag == false) {
            if(gameOver)
                noOneWon = false;
            else {
                noOneWon = true;
                playerTurn.setText("Try Again!!!");
            }
            btnReset.setVisibility(View.VISIBLE);
        }
    }
    //reset
    private void resetTheGame() {
        for(int i = 0 ; i < gridLayout.getChildCount() ; i++){
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.animate().rotationX(imageView.getRotationX() + 720).setDuration(1000);
            imageView.setImageDrawable(null);
            imageView.setAlpha(0.5f);
        }
        if(noOneWon == false) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        playerTurn.setText("Player : Lion");
        currentPlayer = Player.ONE;
        gameOver = false;
        btnReset.setVisibility(View.INVISIBLE);
        playerChoices = new Player[]{Player.NO, Player.NO, Player.NO, Player.NO, Player.NO, Player.NO, Player.NO, Player.NO, Player.NO};
        noOneWon = true;
    }
}