package ru.macdroid.onemorecalculator;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.faendir.rhino_android.RhinoAndroidHelper;

import org.mozilla.javascript.Scriptable;

import java.text.DecimalFormat;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_one, btn_two, btn_three, btn_four, btn_five, btn_six, btn_seven, btn_eight, btn_nine, btn_zero, btn_plus, btn_minus, btn_multiply, btn_divide, btn_equal, btn_dot, btn_delete, btn_small_bracket, btn_percentage, btn_back;

    private TextView tv_process, tv_result;

    private String processListener; // слушает вводимые данные и ставит рядом к введенному числу

    private Boolean isSmallBracketOpen;

    private DecimalFormat decimalFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        decimalFormat = new DecimalFormat("#.##########");


        isSmallBracketOpen = false;

        btn_one = (Button)findViewById(R.id.btn_one);
        btn_two = (Button)findViewById(R.id.btn_two);
        btn_three = (Button)findViewById(R.id.btn_three);
        btn_four = (Button)findViewById(R.id.btn_four);
        btn_five = (Button)findViewById(R.id.btn_five);
        btn_six = (Button)findViewById(R.id.btn_six);
        btn_seven = (Button)findViewById(R.id.btn_seven);
        btn_eight = (Button)findViewById(R.id.btn_eight);
        btn_nine = (Button)findViewById(R.id.btn_nine);
        btn_zero = (Button)findViewById(R.id.btn_zero);

        btn_plus =     (Button)findViewById(R.id.btn_plus);
        btn_minus =    (Button)findViewById(R.id.btn_minus);
        btn_multiply = (Button)findViewById(R.id.btn_multiply);
        btn_divide =   (Button)findViewById(R.id.btn_divide);
        btn_equal =   (Button)findViewById(R.id.btn_equal);
        btn_dot =   (Button)findViewById(R.id.btn_dot);

        btn_delete =        (Button)findViewById(R.id.btn_delete);
        btn_small_bracket = (Button)findViewById(R.id.btn_small_bracket);
        btn_percentage =    (Button)findViewById(R.id.btn_percentage);
        btn_back =          (Button)findViewById(R.id.btn_back);

        tv_process = (TextView) findViewById(R.id.tv_process);
        tv_result =  (TextView) findViewById(R.id.tv_result);


        tv_process.setText(""); // обнуление поля при старте
        tv_result.setText("");

        tv_result.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        tv_process.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        btn_delete.setOnClickListener(this);
        btn_small_bracket.setOnClickListener(this);
        btn_percentage.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_multiply.setOnClickListener(this);
        btn_divide.setOnClickListener(this);
        btn_equal.setOnClickListener(this);
        btn_one.setOnClickListener(this);
        btn_two.setOnClickListener(this);
        btn_three.setOnClickListener(this);
        btn_four.setOnClickListener(this);
        btn_five.setOnClickListener(this);
        btn_six.setOnClickListener(this);
        btn_seven.setOnClickListener(this);
        btn_eight.setOnClickListener(this);
        btn_nine.setOnClickListener(this);
        btn_zero.setOnClickListener(this);
        btn_dot.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        processListener = tv_process.getText().toString();

        switch (view.getId()) {

            case R.id.btn_delete:
                tv_result.setText("");
                tv_process.setText("");
                break;

            case R.id.btn_one:

                tv_process.setText(processListener + "1");
                break;

            case R.id.btn_two:
                tv_process.setText(processListener + "2");
                break;

            case R.id.btn_three:
                tv_process.setText(processListener + "3");
                break;

            case R.id.btn_four:
                tv_process.setText(processListener + "4");
                break;

            case R.id.btn_five:
                tv_process.setText(processListener + "5");
                break;

            case R.id.btn_six:
                tv_process.setText(processListener + "6");
                break;

            case R.id.btn_seven:
                tv_process.setText(processListener + "7");
                break;

            case R.id.btn_eight:
                tv_process.setText(processListener + "8");
                break;

            case R.id.btn_nine:
                tv_process.setText(processListener + "9");
                break;

            case R.id.btn_zero:
                tv_process.setText(processListener + "0");
                break;

            case R.id.btn_minus:
                tv_process.setText(processListener + " - ");
                break;

            case R.id.btn_multiply:
                tv_process.setText(processListener + " \u00d7 ");
                break;

            case R.id.btn_divide:
                tv_process.setText(processListener + " / ");
                break;

            case R.id.btn_plus:
                tv_process.setText(processListener + " + ");
                break;

            case R.id.btn_dot:
                tv_process.setText(processListener + ".");
                break;

            case R.id.btn_back:
                if (processListener.length() > 0) {
                    processListener = processListener.substring(0, processListener.length()-1);
                    tv_process.setText(processListener);
                }
                break;

            case R.id.btn_percentage:
                tv_process.setText(processListener + " % ");
                break;

            case R.id.btn_small_bracket:
                if (isSmallBracketOpen) {
                    tv_process.setText(processListener + ") ");
                    isSmallBracketOpen = false;
                } else {
                    tv_process.setText(processListener + " (");
                    isSmallBracketOpen = true;
                }
                break;

            case R.id.btn_equal:
                processListener = processListener.replaceAll("\u00d7", "*");
                processListener = processListener.replaceAll("%", "/100");

                org.mozilla.javascript.Context rhino = org.mozilla.javascript.Context.enter();
                rhino.setOptimizationLevel(-1);

                String result = "";

                try {
                    Scriptable scope = rhino.initSafeStandardObjects();

                    result = rhino.evaluateString(scope, processListener, "JavaScript", 1, null).toString();

                } catch (Exception e) {
                    result = "Error";
                }


                tv_result.setText(result);

                break;


            default:
                break;

        }





    }
}
