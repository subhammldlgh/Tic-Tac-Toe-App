package androidsamples.java.tictactoe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class GameFragment extends Fragment {
  private static final String TAG = "GameFragment";
  private static final int GRID_SIZE = 9;

  private final Button[] mButtons = new Button[GRID_SIZE];
  private NavController mNavController;
  private boolean gameFinished;
  private String gameState;
  private TextView res;
  private FirebaseAuth mAuth;
  private DatabaseReference rootRef,games;
  private String player1,player2;
  // 0 depicts null state
  // 1 depicts move from player one : "O"
  // 2 depicts move from player two (or computer for 1 player game)  : "X"

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true); // Needed to display the action menu for this fragment
    // Extract the argument passed with the action in a type-safe way
    GameFragmentArgs args = GameFragmentArgs.fromBundle(getArguments());
    Log.d(TAG, "New game type = " + args.getGameType());
    gameFinished=false;
    gameState ="000000000";
    mAuth = FirebaseAuth.getInstance();

    // Handle the back press by adding a confirmation dialog
    OnBackPressedCallback callback = new OnBackPressedCallback(true) {
      @Override
      public void handleOnBackPressed() {
        Log.d(TAG, "Back pressed");

        if (!gameFinished) {
          AlertDialog dialog = new AlertDialog.Builder(requireActivity())
                  .setTitle(R.string.confirm)
                  .setMessage(R.string.forfeit_game_dialog_message)
                  .setPositiveButton(R.string.yes, (d, which) -> {
                    // TODO update loss count
                    mNavController.popBackStack();
                    rootRef.child("users").child(mAuth.getCurrentUser().getUid()).child("losses").setValue(ServerValue.increment(1));
                  })
                  .setNegativeButton(R.string.cancel, (d, which) -> d.dismiss())
                  .create();
          dialog.show();
        }
        else
          mNavController.popBackStack();
      }
    };
    requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_game, container, false);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    rootRef = FirebaseDatabase.getInstance().getReference();
    games = FirebaseDatabase.getInstance().getReference("games");
    mNavController = Navigation.findNavController(view);
    res=view.findViewById(R.id.txt_result);

    mButtons[0] = view.findViewById(R.id.button0);
    mButtons[1] = view.findViewById(R.id.button1);
    mButtons[2] = view.findViewById(R.id.button2);
    mButtons[3] = view.findViewById(R.id.button3);
    mButtons[4] = view.findViewById(R.id.button4);
    mButtons[5] = view.findViewById(R.id.button5);
    mButtons[6] = view.findViewById(R.id.button6);
    mButtons[7] = view.findViewById(R.id.button7);
    mButtons[8] = view.findViewById(R.id.button8);

    GameFragmentArgs args = GameFragmentArgs.fromBundle(getArguments());
    if(args.getGameType().equals("One-Player")) {
      for (int i = 0; i < mButtons.length; i++) {
        int finalI = i;
        mButtons[i].setOnClickListener(v -> {
          if (gameState.charAt(finalI) != '0') {
            Toast.makeText(getContext(), "Cell already filled", Toast.LENGTH_SHORT).show();
          } else if (!gameFinished) {

            mButtons[finalI].setText("X");
            gameState = gameState.substring(0, finalI) + '1' + gameState.substring(finalI + 1);
            if (checkResult(gameState) == 0) {
              Random rand = new Random();
              int r = rand.nextInt(8);
              while (gameState.charAt(r)!='0') {
                r++;
                if (r == 8) {
                  r = 0;
                }
              }
              gameState = gameState.substring(0, r) + '2' + gameState.substring(r + 1);
              mButtons[r].setText("O");
            }
            if (checkResult(gameState) == 1) {
              res.setText("You Win. Press back to go back to main menu");
              rootRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wins").setValue(ServerValue.increment(1));
              gameFinished = true;
            } else if (checkResult(gameState) == 2) {
              res.setText("You Lose. Press back to go back to main menu");
              rootRef.child("users").child(mAuth.getCurrentUser().getUid()).child("losses").setValue(ServerValue.increment(1));
              gameFinished = true;
            } else if (checkResult(gameState) == 3) {
              res.setText("Game is Draw. Press back to go back to main menu");
              gameFinished = true;
            }
          }
        });
      }
    }
    else if(args.getGameType().equals("Two-Player")) {
      player1=mAuth.getCurrentUser().getUid();
      MultiGame multiGame = new MultiGame(player1);
      games.child(player1).setValue(multiGame);
      games.child(player1).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          gameState = snapshot.child("gameState").getValue().toString();
          updateUI();
          if (!snapshot.child("player2").getValue().equals("") && snapshot.child("gameState").getValue().equals("000000000")) {
            //Toast.makeText(getActivity(), "Second player joined, you can make the first move", Toast.LENGTH_SHORT).show();
          }
          if (snapshot.child("gameRes").getValue().equals("2") && !gameFinished) {
            res.setText("You Lose. Press back to go back to main menu");
            rootRef.child("users").child(mAuth.getCurrentUser().getUid()).child("losses").setValue(ServerValue.increment(1));
            gameFinished=true;
          }

          for (int i = 0; i < mButtons.length; i++) {
            int finalI = i;
            mButtons[i].setOnClickListener(v -> {
              if (gameState.charAt(finalI) != '0') {
                Toast.makeText(getContext(), "Cell already filled", Toast.LENGTH_SHORT).show();
              }
              else if(!gameFinished && Integer.parseInt(snapshot.child("playerChance").getValue().toString())==1 && !snapshot.child("player2").getValue().equals("")){
                mButtons[finalI].setText("X");
                gameState = gameState.substring(0, finalI) + '1' + gameState.substring(finalI + 1);
                games.child(player1).child("gameState").setValue(gameState);
                if (checkResult(gameState) == 1) {
                  res.setText("You win. Press back to go back to main menu");
                  rootRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wins").setValue(ServerValue.increment(1));
                  gameFinished = true;
                  games.child(player1).child("gameRes").setValue("1");
                }
                else if (checkResult(gameState) == 3) {
                  res.setText("Game is Draw. Press back to go back to main menu");
                  gameFinished = true;
                  games.child(player1).child("gameRes").setValue("3");
                }
                games.child(player1).child("playerChance").setValue(2);
              }
            });
          }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      });
    }
    else{
      player1=args.getUid();
      player2=mAuth.getCurrentUser().getUid();
      games.child(player1).child("player2").setValue(player2);
      games.child(player1).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          gameState = snapshot.child("gameState").getValue().toString();
          updateUI();
          if (snapshot.child("gameRes").getValue().equals("1") && !gameFinished) {
            res.setText("You Lose. Press back to go back to main menu");
            rootRef.child("users").child(mAuth.getCurrentUser().getUid()).child("losses").setValue(ServerValue.increment(1));
            gameFinished = true;
          }
          if (snapshot.child("gameRes").getValue().equals("3") && !gameFinished) {
            res.setText("Game is Draw. Press back to go back to main menu");
            gameFinished = true;
          }

          for (int i = 0; i < mButtons.length; i++) {
            int finalI = i;
            mButtons[i].setOnClickListener(v -> {
              if (gameState.charAt(finalI) != '0') {
                Toast.makeText(getContext(), "Cell already filled", Toast.LENGTH_SHORT).show();
              }
              else if(!gameFinished && Integer.parseInt(snapshot.child("playerChance").getValue().toString())==2){
                mButtons[finalI].setText("O");
                gameState = gameState.substring(0, finalI) + '2' + gameState.substring(finalI + 1);
                games.child(player1).child("gameState").setValue(gameState);
                if (checkResult(gameState) == 2) {
                  res.setText("You win. Press back to go back to main menu");
                  rootRef.child("users").child(mAuth.getCurrentUser().getUid()).child("wins").setValue(ServerValue.increment(1));
                  gameFinished = true;
                  games.child(player1).child("gameRes").setValue("2");
                }
                games.child(player1).child("playerChance").setValue(1);
              }
            });
          }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
        }
      });
    }
  }

  private void updateUI() {
    for (int i = 0; i < mButtons.length; i++) {
      if (gameState.charAt(i) == '1') {
        mButtons[i].setText("X");
      } else if (gameState.charAt(i) == '2') {
        mButtons[i].setText("O");
      }
    }
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.menu_logout, menu);
    // this action menu is handled in MainActivity
  }
  int checkResult(String a)
  {
    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    for (int[] winPosition : winPositions) {
      if (a.charAt(winPosition[0]) == '1' && a.charAt(winPosition[1]) == '1' && a.charAt(winPosition[2]) == '1')
        return 1;
      else if (a.charAt(winPosition[0]) == '2' && a.charAt(winPosition[1]) == '2' && a.charAt(winPosition[2]) == '2')
        return 2;
    }
    for (int j = 0; j < a.length(); j++) {
      if (a.charAt(j) == '0')
        return 0;
    }
    return 3;
  }
}