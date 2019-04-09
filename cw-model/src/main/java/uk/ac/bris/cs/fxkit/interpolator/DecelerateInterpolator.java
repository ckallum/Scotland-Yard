package uk.ac.bris.cs.fxkit.interpolator;

import javafx.animation.Interpolator;

public class DecelerateInterpolator extends Interpolator {

    public static final DecelerateInterpolator DEFAULT = new DecelerateInterpolator(2.5f);

    private float factor = 1;

    public DecelerateInterpolator() {
    }

    public DecelerateInterpolator(float factor) {
        this.factor = factor;
    }

    @Override
    protected double curve(double input) {
        if (factor == 1.0f)
            return (float) (1.0f - (1.0f - input) * (1.0f - input));
        else
            return (float) (1.0f - Math.pow((1.0f - input), 2 * factor));
    }

}
