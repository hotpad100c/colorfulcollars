package mypals.ml;

import com.google.common.primitives.Ints;

import java.util.Arrays;

public class ColorHelper {
    public static int blendColors(int colorOrg, int dyeColor, int blendFactor) {
        int newRed = (dyeColor >> 16) & 0xFF;
        int newGreen = (dyeColor >> 8) & 0xFF;
        int newBlue = dyeColor & 0xFF;

        int oldRed = (colorOrg >> 16) & 0xFF;
        int oldGreen = (colorOrg >> 8) & 0xFF;
        int oldBlue = colorOrg & 0xFF;

        var newColor = new int[3];
        newColor[0] = newRed;
        newColor[1] = newGreen;
        newColor[2] = newBlue;

        var oldColor = new int[3];
        oldColor[0] = oldRed;
        oldColor[1] = oldGreen;
        oldColor[2] = oldBlue;
        if (!Arrays.equals(oldColor, new int[]{-1, -1, -1})) {
            var avgColor = new int[3];
            avgColor[0] = (oldColor[0] + newColor[0]) / blendFactor;
            avgColor[1] = (oldColor[1] + newColor[1]) / blendFactor;
            avgColor[2] = (oldColor[2] + newColor[2]) / blendFactor;

            var avgMax = (Ints.max(oldColor) + Ints.max(newColor)) / 2.0f;

            var maxOfAvg = (float) Ints.max(avgColor);
            var gainFactor = (avgMax / maxOfAvg);

            oldColor[0] = (int) (avgColor[0] * gainFactor);
            oldColor[1] = (int) (avgColor[1] * gainFactor);
            oldColor[2] = (int) (avgColor[2] * gainFactor);
        } else {
            oldColor = newColor;
        }

        return (oldColor[0] << 16) | (oldColor[1] << 8) | oldColor[2];
    }
}
