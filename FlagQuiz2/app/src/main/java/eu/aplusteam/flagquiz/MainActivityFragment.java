package eu.aplusteam.flagquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements QuizInterface {

    private static final String TAG = "FlagQuiz Activity";
    private static final int FLAGS_IN_QUIZ = 10;

    private List<String>    fileNameList;
    private List<String>    quizCountriesList;
    private Set<String>     regionSet;
    private String          correctAnswer;
    private int             totalGuesses;
    private int             correctAnswers;
    private int             guessRows;
    private SecureRandom    random;
    private Handler         handler;
    private Animation       shakeAnimation;

    private LinearLayout    quizLinearLayout;
    private TextView        questionNumberTextView;
    private ImageView       flagImageView;
    private LinearLayout[]  guessLinearLayouts;
    private TextView        answerTextView;

    public MainActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view =
                inflater.inflate(R.layout.fragment_main, container, false);

        fileNameList = new ArrayList<>();
        quizCountriesList = new ArrayList<>();
        random = new SecureRandom();
        handler = new Handler();


        shakeAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3);

        quizLinearLayout =
                (LinearLayout) view.findViewById(R.id.quizLinearLayout);
        questionNumberTextView =
                (TextView) view.findViewById(R.id.questionNumberTextView);
        flagImageView = (ImageView) view.findViewById(R.id.flagImageView);
        guessLinearLayouts = new LinearLayout[4];
        guessLinearLayouts[0] =
                (LinearLayout) view.findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] =
                (LinearLayout) view.findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[2] =
                (LinearLayout) view.findViewById(R.id.row3LinearLayout);
        guessLinearLayouts[3] =
                (LinearLayout) view.findViewById(R.id.row4LinearLayout);
        answerTextView = (TextView) view.findViewById(R.id.answerTextView);

        for (LinearLayout row : guessLinearLayouts) {
            for (int column = 0; column < row.getChildCount(); column++) {
                Button button = (Button) row.getChildAt(column);
                button.setOnClickListener(guessButtonListener);
            }
        }

        questionNumberTextView.setText(
                getString(R.string.question, 1, FLAGS_IN_QUIZ) );

        return view;
    }

    @Override
    public void updateGuessRows(SharedPreferences sharedPreferences) {

        String choices = sharedPreferences.getString(MainActivity.CHOICES, null);
        guessRows = Integer.parseInt(choices) / 2;

        for (LinearLayout layout : guessLinearLayouts) {
            layout.setVisibility(View.GONE);
        }

        for (int row = 0; row < guessRows; row++ ) {
            guessLinearLayouts[row].setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateRegions(SharedPreferences sharedPreferences) {
        regionSet =
                sharedPreferences.getStringSet(MainActivity.REGIONS, null);
    }

    @Override
    public void resetQuiz() {
        AssetManager assets = getActivity().getAssets();
        fileNameList.clear();

        try {
            for (String region : regionSet) {
                String[] paths = assets.list(region);

                for (String path : paths) {
                    fileNameList.add(path.replace(".png", ""));
                }

            }
        } catch (IOException exception) {
            Log.e(TAG, "Error loading image file names", exception);
        }

        correctAnswers = 0;
        totalGuesses = 0;
        quizCountriesList.clear();

        int flagCounter = 1;
        int numberOfFlags = fileNameList.size();

        while (flagCounter <= FLAGS_IN_QUIZ) {
            int randomIndex = random.nextInt(numberOfFlags);

            String filename = fileNameList.get(randomIndex);

            if (!quizCountriesList.contains(filename)) {
                quizCountriesList.add(filename);
                ++flagCounter;
            }
        }

        loadNextFlag();
    }

    private void loadNextFlag() {
        String nextImage = quizCountriesList.remove(0);
        correctAnswer = nextImage;
        answerTextView.setText("");

        questionNumberTextView.setText(getString(
                R.string.question, (correctAnswers + 1), FLAGS_IN_QUIZ
        ));

        String region = nextImage.substring(0, nextImage.indexOf('-'));

        AssetManager assets = getActivity().getAssets();

        try (InputStream stream = assets.open(region + "/" + nextImage + ".png")) {
            Drawable flag = Drawable.createFromStream(stream, nextImage);
            flagImageView.setImageDrawable(flag);

            animate(false);
        } catch (IOException exception) {
            Log.e(TAG, "Error loading " + nextImage, exception);
        }

        Collections.shuffle(fileNameList);

        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));

        for (int row = 0; row < guessRows; row++ ) {
            for (int column = 0;
                    column < guessLinearLayouts[row].getChildCount();
                        column++ ) {
                Button newGuessButton =
                        (Button) guessLinearLayouts[row].getChildAt(column);
                newGuessButton.setEnabled(true);

                String filename = fileNameList.get((row * 2) + column);
                newGuessButton.setText(getCountryName(filename));
            }
        }

        int row = random.nextInt(guessRows); // pick random row
        int column = random.nextInt(2); // pick random column
        LinearLayout randomRow = guessLinearLayouts[row]; // get the row
        String countryName = getCountryName(correctAnswer);
        ((Button) randomRow.getChildAt(column)).setText(countryName);
    }

    // parses the country flag file name and returns the country name
    @NonNull
    private String getCountryName(String name) {
        return name.substring(name.indexOf('-') + 1).replace('_', ' ');
    }

    private void animate(boolean animateOut) {

        if (correctAnswers == 0)
            return;

        int centerX = (quizLinearLayout.getLeft() +
                        quizLinearLayout.getRight() ) / 2;
        int centerY = (quizLinearLayout.getTop() +
                        quizLinearLayout.getBottom() ) / 2;

        int radius = Math.max(quizLinearLayout.getWidth(),
                quizLinearLayout.getHeight());

        Animator animator;

        if (animateOut) {
            animator = ViewAnimationUtils.createCircularReveal(
                    quizLinearLayout, centerX, centerY, radius, 0 );
            animator.addListener(
                    new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loadNextFlag();
                        }
                    }
            );


        } else {
            animator = ViewAnimationUtils.createCircularReveal(
                    quizLinearLayout, centerX, centerY, 0, radius);
        }

        animator.setDuration(500); // set animation duration to 500 ms
        animator.start(); // start the animation

    }



    public static class QuizFragment extends DialogFragment {

        private QuizInterface quizInterface;
        private int totalGuesses;

        static QuizFragment newInstance(int totalGuesses) {
            QuizFragment f = new QuizFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("totalGuesses", totalGuesses);

            f.setArguments(args);

            return f;
        }


        // create an AlertDialog and return it
        @Override
        public Dialog onCreateDialog(Bundle bundle) {

            totalGuesses = getArguments().getInt("totalGuesses");

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());
            builder.setMessage(
                    getString(R.string.results,
                            totalGuesses,
                            (1000 / (double) totalGuesses)));

            // "Reset Quiz" Button
            builder.setPositiveButton(R.string.reset_quiz,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            QuizInterface quizInterface = (QuizInterface)
                                    getFragmentManager().findFragmentById(R.id.quizFragment);
                            quizInterface.resetQuiz();
                        }
                    }
            );

            return builder.create(); // return the AlertDialog
        }
    }

    private OnClickListener guessButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Button guessButton = ((Button) v);
            String guess = guessButton.getText().toString();
            String answer = getCountryName(correctAnswer);
            ++totalGuesses; // increment number of guesses the user has made

            if (guess.equals(answer)) { // if the guess is correct
                ++correctAnswers; // increment the number of correct answers

                // display correct answer in green text
                answerTextView.setText(answer + "!");
                answerTextView.setTextColor(
                        getResources().getColor(R.color.correct_answer,
                                getContext().getTheme()));

                disableButtons(); // disable all guess Buttons

                // if the user has correctly identified FLAGS_IN_QUIZ flags
                if (correctAnswers == FLAGS_IN_QUIZ) {
                    // DialogFragment to display quiz stats and start new quiz
                    QuizFragment quizResults = QuizFragment.newInstance(totalGuesses);

                    // use FragmentManager to display the DialogFragment
                    quizResults.setCancelable(false);
                    quizResults.show(getFragmentManager(), "quiz results");
                }
                else { // answer is correct but quiz is not over
                    // load the next flag after a 2-second delay
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    animate(true); // animate the flag off the screen
                                }
                            }, 2000); // 2000 milliseconds for 2-second delay
                }
            }
            else { // answer was incorrect
                flagImageView.startAnimation(shakeAnimation); // play shake

                // display "Incorrect!" in red
                answerTextView.setText(R.string.incorrect_answer);
                answerTextView.setTextColor(getResources().getColor(
                        R.color.incorrect_answer, getContext().getTheme()));
                guessButton.setEnabled(false); // disable incorrect answer
            }
        }
    };

    private void disableButtons() {
        for (int row = 0; row < guessRows; row++) {
            LinearLayout guessRow = guessLinearLayouts[row];
            for (int i = 0; i < guessRow.getChildCount(); i++)
                guessRow.getChildAt(i).setEnabled(false);
        }
    }

}
