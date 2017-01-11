package eu.aplusteam.canongame;

/**
 * Created by anton on 09.01.17.
 */

public class Blocker extends GameElement {

    private int missPenalty;


    public Blocker(CannonView view, int color, int missPenalty,
                   int x, int y, int width, int length, float velocityY) {
        super(view, color, CannonView.BLOCKER_SOUND_ID,
                x, y, width, length, velocityY);

        this.missPenalty = missPenalty;
    }

    public int getMissPenalty() {
        return missPenalty;
    }
}
