package com.example.weatherdemo.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.weatherdemo.R;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class HomeFragment extends Fragment {

    private TextView processTextView;
    private TextView resultTextView;
    private ImageView resetImageView;
    private ImageView deleteImageView;
    private ImageView percentImageView;
    private ImageView divideImageView;
    private TextView sevenTextView, eightTextView, nineTextView;
    private ImageView multiplyImageView;
    private TextView fourTextView, fiveTextView, sixTextView;
    private ImageView subtractImageView;
    private TextView oneTextView, twoTextView, threeTextView;
    private ImageView addImageView;
    private TextView zeroTextView, pointTextView;
    private ImageView equalsImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        initView(root);
        setListeners();

        return root;
    }

    private void initView(View root) {
        processTextView = root.findViewById(R.id.process_textView);
        resultTextView = root.findViewById(R.id.result_textView);
        resetImageView = root.findViewById(R.id.reset_imageView);
        deleteImageView = root.findViewById(R.id.delete_imageView);
        percentImageView = root.findViewById(R.id.percent_imageView);
        divideImageView = root.findViewById(R.id.divide_imageView);
        sevenTextView = root.findViewById(R.id.seven_textView);
        eightTextView = root.findViewById(R.id.eight_textView);
        nineTextView = root.findViewById(R.id.nine_textView);
        multiplyImageView = root.findViewById(R.id.multiply_imageView);
        fourTextView = root.findViewById(R.id.four_textView);
        fiveTextView = root.findViewById(R.id.five_textView);
        sixTextView = root.findViewById(R.id.six_textView);
        subtractImageView = root.findViewById(R.id.subtract_imageView);
        oneTextView = root.findViewById(R.id.one_textView);
        twoTextView = root.findViewById(R.id.two_textView);
        threeTextView = root.findViewById(R.id.three_textView);
        addImageView = root.findViewById(R.id.add_imageView);
        zeroTextView = root.findViewById(R.id.zero_textView);
        pointTextView = root.findViewById(R.id.point_textView);
        equalsImageView = root.findViewById(R.id.equals_imageView);
    }

    private void setListeners() {
        resetImageView.setOnClickListener(v -> reset());
        deleteImageView.setOnClickListener(v -> delete());
        percentImageView.setOnClickListener(v -> percent());
        divideImageView.setOnClickListener(v -> appendOperator("÷"));
        multiplyImageView.setOnClickListener(v -> appendOperator("×"));
        subtractImageView.setOnClickListener(v -> appendOperator("-"));
        addImageView.setOnClickListener(v -> appendOperator("+"));
        equalsImageView.setOnClickListener(v -> calculate());

        numberClickListener(sevenTextView, "7");
        numberClickListener(eightTextView, "8");
        numberClickListener(nineTextView, "9");
        numberClickListener(fourTextView, "4");
        numberClickListener(fiveTextView, "5");
        numberClickListener(sixTextView, "6");
        numberClickListener(oneTextView, "1");
        numberClickListener(twoTextView, "2");
        numberClickListener(threeTextView, "3");
        numberClickListener(zeroTextView, "0");
        pointTextView.setOnClickListener(v -> appendDecimal());
    }

    private void numberClickListener(TextView textView, String number) {
        textView.setOnClickListener(v -> appendNumber(number));
    }

    @SuppressLint("SetTextI18n")
    private void appendNumber(String number) {
        String currentValue = processTextView.getText().toString();
        processTextView.setText(currentValue + number);
    }

    @SuppressLint("SetTextI18n")
    private void appendDecimal() {
        String currentValue = processTextView.getText().toString();
        if (!currentValue.contains(".")) {
            processTextView.setText(currentValue + ".");
        }
    }

    @SuppressLint("SetTextI18n")
    private void appendOperator(String operator) {
        String currentValue = processTextView.getText().toString();
        if (!currentValue.isEmpty() && !currentValue.endsWith(" ")) {
            processTextView.setText(currentValue + " " + operator + " ");
        }
    }

    private void percent() {
        String currentValue = processTextView.getText().toString();
        try {
            BigDecimal value = new BigDecimal(currentValue);
            BigDecimal percentValue = value.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
            processTextView.setText(percentValue.toPlainString());
        } catch (NumberFormatException e) {
            processTextView.setText("有东西吗？你就百分号");
        }
    }

    private void reset() {
        processTextView.setText("");
        resultTextView.setText("");
    }

    private void delete() {
        String currentValue = processTextView.getText().toString();
        if (currentValue.length() > 1) {
            processTextView.setText(currentValue.substring(0, currentValue.length() - 1));
        } else {
            reset();
        }
    }

    private void calculate() {
        String expression = processTextView.getText().toString();
        try {
            expression = expression.replaceAll("\\s+", "");
            BigDecimal result = evaluateExpression(expression);
            resultTextView.setText(result.toPlainString());
        } catch (ArithmeticException e) {
            resultTextView.setText("除以“0”？算的明白吗你");
        } catch (Exception e) {
            resultTextView.setText("真是错到家了");
        }
    }

    private BigDecimal evaluateExpression(String expression) {
        StringBuilder number = new StringBuilder();
        BigDecimal firstOperand = BigDecimal.ZERO;
        BigDecimal secondOperand;
        String operator = "";
        boolean isSecondOperand = false;

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                number.append(ch);
            } else //noinspection StatementWithEmptyBody
                if (ch == ' ') {
            } else {
                if (!isSecondOperand) {
                    firstOperand = new BigDecimal(number.toString());
                    isSecondOperand = true;
                    number.setLength(0);
                } else {
                    secondOperand = new BigDecimal(number.toString());
                    number.setLength(0);

                    switch (operator) {
                        case "+":
                            firstOperand = firstOperand.add(secondOperand);
                            break;
                        case "-":
                            firstOperand = firstOperand.subtract(secondOperand);
                            break;
                        case "×":
                            firstOperand = firstOperand.multiply(secondOperand);
                            break;
                        case "÷":
                            if (secondOperand.compareTo(BigDecimal.ZERO) == 0) {
                                throw new ArithmeticException("Division by zero.");
                            }
                            firstOperand = firstOperand.divide(secondOperand, 10, RoundingMode.HALF_UP);
                            break;
                    }
                }
                operator = String.valueOf(ch);
            }
        }

        if (!isSecondOperand) {
            return new BigDecimal(number.toString());
        } else {
            secondOperand = new BigDecimal(number.toString());
            switch (operator) {
                case "+":
                    return firstOperand.add(secondOperand);
                case "-":
                    return firstOperand.subtract(secondOperand);
                case "×":
                    return firstOperand.multiply(secondOperand);
                case "÷":
                    if (secondOperand.compareTo(BigDecimal.ZERO) == 0) {
                        throw new ArithmeticException("Division by zero.");
                    }
                    return firstOperand.divide(secondOperand, 10, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.ZERO;
    }
}