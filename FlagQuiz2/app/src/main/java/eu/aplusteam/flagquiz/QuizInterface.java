package eu.aplusteam.flagquiz;

import android.content.SharedPreferences;

/**
 * Created by anton on 20.12.16.
 */
public interface QuizInterface {
    void updateGuessRows(SharedPreferences sharedPreferences);

    void updateRegions(SharedPreferences sharedPreferences);

    void resetQuiz();
}
