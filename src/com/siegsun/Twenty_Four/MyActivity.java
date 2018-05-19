package com.siegsun.Twenty_Four;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyActivity extends Activity {
    private int numSlotFilled = 0;
    private int[] slotIDs = {R.id.slot1, R.id.slot2, R.id.slot3, R.id.slot4};
    private String result;

    private int poker2num(String text) {
        switch (text) {
            case "A":
                return 1;
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
            default:
                return Integer.parseInt(text);
        }
    }

    /**
     * Remove an element from a double array, also cut element before starting point
     * @param numbers original array
     * @param index element to be removed
     * @param start starting point
     * @return a new array
     */
    private double[] removeElement(double[] numbers, int index, int start) {
        double[] left = new double[numbers.length - 1 - start];
        for (int j = start; j < numbers.length; j++) {
            if (j < index) {
                left[j - start] = numbers[j];
            } else if (j > index) {
                left[j - start - 1] = numbers[j];
            }
        }
        return left;
    }

    /**
     * Try 4 possible operations recursively
     * @param numbers
     * @param target
     * @param picked
     * @param expression
     * @return
     */
    private boolean tryFourOperations(double[] numbers, double target, double picked, String expression) {
        // Get 4 possible new targets
        // 1) newTarget + picked = target
        double newTarget = target - picked;
        if (recursiveSolver(numbers, newTarget)) {
            result = "(" + result + "+" + expression + ")";
            return true;
        }
        // 2.1) newTarget - picked = target
        newTarget = target + picked;
        if (recursiveSolver(numbers, newTarget)) {
            result = "(" + result + "-" + expression + ")";
            return true;
        }
        // 2.2) picked - newTarget = target
        newTarget = picked - target;
        if (recursiveSolver(numbers, newTarget)) {
            result = "(" + expression + "-" + result + ")";
            return true;
        }
        // Don't try * or / if picked number is 0
        if (picked != 0) {
            // 3) newTarget * picked = target
            newTarget = target / picked;
            if (recursiveSolver(numbers, newTarget)) {
                result = "(" + result + "x" + expression + ")";
                return true;
            }
            // 4.1) newTarget / picked = target
            newTarget = target * picked;
            if (recursiveSolver(numbers, newTarget)) {
                result = "(" + result + "/" + expression + ")";
                return true;
            }
            // 4.2) picked / newTarget = target
            newTarget = picked / target;
            if (recursiveSolver(numbers, newTarget)) {
                result = "(" + expression + "/" + result + ")";
                return true;
            }
        }

        return false;
    }

    private boolean recursiveSolver(double[] numbers, double target) {
        if (numbers.length == 1) {
            // Only one number left, check against target
            // We don't need exact match because of floating point arithmetic
            if (Math.abs(numbers[0] - target) < 0.000001) {
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
                double picked = numbers[i];
                // Get a new array of left numbers
                double[] left = removeElement(numbers, i, 0);

                boolean success = tryFourOperations(left, target, picked, Integer.toString((int)picked));
                if (success) return true;
            }
            return false;
        }
    }

    /**
     * In some cases the solution is like (A + B) * (C - D). The recursive solver can not get there because it always
     * pick one number. This function pick a pair and use A + B or ABS(A - B) as a starting point.
     * @param numbers 4 integers between 1 and 13
     * @param target 24
     * @return true if a solution is found
     */
    private boolean solveStartingWithPair(double[] numbers, double target) {
        if ((numbers.length != 4)) throw new AssertionError();
        // Check (A,B), (A,C) and (A,D), which is sufficient
        for (int i = 1; i < 4; i++) {
            // Get a new array of 2 numbers left
            double[] left = removeElement(numbers, i, 1);

            // Start with A + X
            double picked = numbers[0] + numbers[i];
            String pair = "(" + (int)numbers[0] + "+" + (int)numbers[i] + ")";

            boolean success = tryFourOperations(left, target, picked, pair);
            if (success) return true;

            // Start with abs(A - X)
            picked = numbers[0] - numbers[i];
            if (picked < 0) {
                picked = 0 - picked;
                pair = "(" + (int)numbers[i] + "-" + (int)numbers[0] + ")";
            } else {
                pair = "(" + (int)numbers[0] + "-" + (int)numbers[i] + ")";
            }

            success = tryFourOperations(left, target, picked, pair);
            if (success) return true;
        }
        return false;
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
                // Resolve
                boolean success = recursiveSolver(numbers, 24);
                // Try again starting with a pair if failed
                if (!success) {
                    success = solveStartingWithPair(numbers, 24);
                }

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
