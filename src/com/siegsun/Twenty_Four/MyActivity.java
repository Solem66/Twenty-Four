package com.siegsun.Twenty_Four;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity {
    private int numSlotFilled = 0;
    private int[] slotIDs = {R.id.slot1, R.id.slot2, R.id.slot3, R.id.slot4};
    private String result;

    private int poker2num(String text) {
        if (text.equals("A")) {
            return 1;
        } else if (text.equals("J")) {
            return 11;
        } else if (text.equals("Q")) {
            return 12;
        } else if (text.equals("K")) {
            return 13;
        } else {
            return Integer.parseInt(text);
        }
    }

    private boolean recursiveSolver(double[] numbers, double target) {
        if (numbers.length == 1) {
            // Only one number left, check against target
            if (numbers[0] == target) {
                // Success!
                result = Integer.toString((int)target);
                return true;
            } else {
                // Failed!
                return false;
            }
        } else {
            for (int i = 0; i < numbers.length; i++) {
                // Pick a number to start
                int picked = (int) numbers[i];
                // Get a new array of left numbers
                double[] left = new double[numbers.length - 1];
                for (int j = 0; j < numbers.length; j++) {
                    if (j < i) {
                        left[j] = numbers[j];
                    } else if (j > i) {
                        left[j-1] = numbers[j];
                    }
                }
                // Get 4 possible new targets
                // 1) newTarget + picked = target
                double newTarget = target - picked;
                if (recursiveSolver(left, newTarget)) {
                    result = "(" + result + "+" + picked + ")";
                    return true;
                }
                // 2) newTarget * picked = target
                newTarget = target / picked;
                if (recursiveSolver(left, newTarget)) {
                    result = "(" + result + "x" + picked + ")";
                    return true;
                }
                // 3) newTarget - picked = target
                newTarget = target + picked;
                if (recursiveSolver(left, newTarget)) {
                    result = "(" + result + "-" + picked + ")";
                    return true;
                }
                // 4) newTarget / picked = target
                newTarget = target * picked;
                if (recursiveSolver(left, newTarget)) {
                    result = "(" + result + "/" + picked + ")";
                    return true;
                }
            }
            return false;
        }
    }

    public void pickOneCard(View view) {
        final Button button = (Button) view;
        CharSequence cardID = button.getText();
        if (numSlotFilled < 4) {
            TextView slot = (TextView) findViewById(slotIDs[numSlotFilled]);
            slot.setText(cardID);

            numSlotFilled++;

            if (numSlotFilled == 4) {
                double[] numbers = new double[4];
                for (int i = 0; i < 4; i++) {
                    TextView aSlot = (TextView) findViewById((slotIDs[i]));
                    numbers[i] = poker2num(aSlot.getText().toString());
                }
                boolean success = recursiveSolver(numbers, 24);
                final TextView resultTextView = (TextView) findViewById(R.id.resultArea);
                if (success) {
                    resultTextView.setText(result);
                } else {
                    resultTextView.setText(R.string.no_sulution);
                }
            }
        }
    }

    public void resetContent(View view) {
        numSlotFilled = 0;
        result = "";
        final TextView result = (TextView) findViewById(R.id.resultArea);
        result.setText(R.string.default_result);
        TextView slot = (TextView) findViewById(R.id.slot1);
        slot.setText(R.string.unknown_card);
        slot = (TextView) findViewById(R.id.slot2);
        slot.setText(R.string.unknown_card);
        slot = (TextView) findViewById(R.id.slot3);
        slot.setText(R.string.unknown_card);
        slot = (TextView) findViewById(R.id.slot4);
        slot.setText(R.string.unknown_card);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}
