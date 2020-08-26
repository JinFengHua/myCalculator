package com.example.calculate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    static int program_state = 0;

    Button b_clear,b_division,b_multiplication,b_delete, b_subtraction,b_addition,b_equal,
           b_left_bracket,b_right_bracket,b_point,b_1,b_2,b_3,b_4,b_5,b_6,b_7,b_8,b_9,b_0;

    Double result;
    String operator = "";
    Boolean in_state = true;//true表示上一个输入为数字，false表示上一个输入为运算符

    TextView in;
    TextView out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setClickListener();
    }

    private void findView(){
        b_clear = findViewById(R.id.num_clear);
        b_division = findViewById(R.id.num_division);
        b_multiplication = findViewById(R.id.num_multiplication);
        b_delete = findViewById(R.id.num_delete);
        b_subtraction = findViewById(R.id.num_subtraction);
        b_addition = findViewById(R.id.num_addition);
        b_equal = findViewById(R.id.num_equal);
        b_point = findViewById(R.id.num_point);
        b_left_bracket = findViewById(R.id.num_left_bracket);
        b_right_bracket = findViewById(R.id.num_right_bracket);
        b_0 = findViewById(R.id.num_0);
        b_1 = findViewById(R.id.num_1);
        b_2 = findViewById(R.id.num_2);
        b_3 = findViewById(R.id.num_3);
        b_4 = findViewById(R.id.num_4);
        b_5 = findViewById(R.id.num_5);
        b_6 = findViewById(R.id.num_6);
        b_7 = findViewById(R.id.num_7);
        b_8 = findViewById(R.id.num_8);
        b_9 = findViewById(R.id.num_9);
        in = findViewById(R.id.in);
        in.setText("");
        out = findViewById(R.id.out);
        out.setText("");
    }
//将字符串转化为数组
    public static ArrayList<String> toZhong(String str){
        ArrayList<String> arrayList = new ArrayList<>();
        int bracket_test = 0;
        String temp = "";
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)>='0'&&str.charAt(i)<='9'){
                temp = temp+str.charAt(i);
            }else if (str.charAt(i) == '.'){
                temp = temp +str.charAt(i);
            }else {
                if (temp.length()!=0) arrayList.add(temp);
                temp = "";
                arrayList.add(str.charAt(i)+"");
            }
        }
        arrayList.add(temp);
        String t = "";
        for (String s: arrayList){
            if (t.equals("/")&&s.equals("0")){
                arrayList.clear();
                arrayList.add("error");
                return arrayList;
            }
            t = s;
            if (s.equals("("))
                bracket_test++;
            if (s.equals(")"))
                bracket_test--;
        }
        if (bracket_test!=0){
            arrayList.clear();
            arrayList.add("error");
            return arrayList;
        }
        return arrayList;
    }
//转化为逆波兰式
    public static ArrayList<String> toRPN(ArrayList<String> list){
        ArrayList<String> arr = new ArrayList<>();
        Stack<String> opeStack = new Stack<>();
        Boolean flag ;
        for (String s:list){
            try {
                Double.parseDouble(s);
                flag = true;
            }catch (NumberFormatException e){
                flag = false;
            }
            if (flag){
                arr.add(s);
            }else if(s.equals("(")){
                opeStack.push(s);
            }else if (s.equals(")")){
                while (!opeStack.peek().equals("(")){
                    arr.add(opeStack.pop());
                }
                opeStack.pop();
            }else{
                while (opeStack.size()!=0&& (getValue(s)<=getValue(opeStack.peek()))){
                    arr.add(opeStack.pop());
                }
                opeStack.push(s);
            }
        }
        while (opeStack.size()!=0){
            arr.add(opeStack.pop());
        }
        return arr;
    }

    public static int getValue(String s){
        if (s.equals("+")||s.equals("-")){
            return 1;
        }else if (s.equals("*")||s.equals("/")){
            return 2;
        }
        return 0;
    }

    private Double getResult(ArrayList<String> list){
        Stack<BigDecimal> result = new Stack<>();
        Boolean flag;
        for (String s:list){
            try {
                Double.parseDouble(s);
                flag = true;
            }catch (NumberFormatException e){
                flag = false;
            }
            if (flag){
                result.push(BigDecimal.valueOf(Double.parseDouble(s)));
            }else if (result.size()>=2){
                BigDecimal b = result.pop();
                BigDecimal a = result.pop();
                if (s.equals("+")){
                    result.push(a.add(b));
                }else if (s.equals("-")){
                    result.push(a.subtract(b));
                }else if (s.equals("*")){
                    result.push(a.multiply(b));
                }else if (s.equals("/")){
                    result.push(a.divide(b));
                }
            }
        }

        return Double.parseDouble(result.pop().stripTrailingZeros().toPlainString());
    }
//设置各个按钮的监听事件
    private void setClickListener(){
        b_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_state = true;
                in.setText("");
                out.setText("");
                operator = "";
            }
        });

        b_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator.length()<2){
                    operator = "";
                    in_state = true;
                }else {
                    char temp = operator.charAt(operator.length()-2);
                    if ("+".equals(temp) || "-".equals(temp) || "*".equals(temp) || "/".equals(temp)){
                        in_state = false;
                    }else{
                        in_state = true;
                    }
                    operator = operator.substring(0,operator.length()-1);
                }
                in.setText(operator);
            }
        });

        b_division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (in_state&&operator.length()>0){
                    operator = operator + "/";
                    in.setText(operator);
                    in_state = false;
                }
            }
        });

        b_multiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (in_state&&operator.length()>0){
                    operator = operator + "*";
                    in.setText(operator);
                    in_state = false;
                }
            }
        });

        b_addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (in_state&&operator.length()>0){
                    operator = operator + "+";
                    in.setText(operator);
                    in_state = false;
                }
            }
        });

        b_subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (in_state&&operator.length()>0){
                    operator = operator + "-";
                    in.setText(operator);
                    in_state = false;
                }
            }
        });

        b_equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (in_state&&operator.length()>0){
                    ArrayList<String> temp = toZhong(operator);
                    if (temp.get(0).equals("error")){
                        out.setText("错误");
                    }else {
                        result = getResult(toRPN(temp));
                        out.setText(result.toString());
                    }
                    operator = "";
                }
            }
        });

        b_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operator.length() == 0){
                    operator+="0";
                    in_state = true;
                    in.setText(operator);
                    return;
                }
                operator = operator + "0";
                in_state =true;
                in.setText(operator);
            }
        });

        b_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = operator + "1";
                in_state =true;
                in.setText(operator);
            }
        });

        b_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = operator + "2";
                in_state =true;
                in.setText(operator);
            }
        });

        b_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = operator + "3";
                in_state =true;
                in.setText(operator);
            }
        });

        b_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = operator + "4";
                in_state =true;
                in.setText(operator);
            }
        });

        b_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = operator + "5";
                in_state =true;
                in.setText(operator);
            }
        });

        b_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = operator + "6";
                in_state =true;
                in.setText(operator);
            }
        });

        b_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = operator + "7";
                in_state =true;
                in.setText(operator);
            }
        });

        b_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = operator + "8";
                in_state =true;
                in.setText(operator);
            }
        });

        b_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operator = operator + "9";
                in_state =true;
                in.setText(operator);
            }
        });

        b_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (in_state&&operator.length()>0){
                    operator = operator + ".";
                    in_state = false;
                    in.setText(operator);
                }
            }
        });

        b_left_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!in_state||operator.length() == 0){
                    operator = operator + "(";
                    in_state = false;
                    in.setText(operator);
                }
            }
        });

        b_right_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (in_state){
                    operator = operator + ")";
                    in_state = true;
                    in.setText(operator);
                }
            }
        });


    }

}