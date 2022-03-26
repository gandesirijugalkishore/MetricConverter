package com.example.metricconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] length = { "Miles", "Kilometer",
            "Feet", "Inches",
            "Meter" };
    String[] temprature = { "Fahrenheit", "Kelvin",
            "Degree", "Celcius"};
    String[] currency = { "US Dollar", "Indian Rupee",
            "Euro", "Australian Dollar",
            "Canadian Dollar" };
    String[] weight = { "Pound", "Kilogram",
            "Gram", "ounces",};

    //below is for text showing conversion rates
    TextView txtFirst, txtSecond,  txtThird, txtFourth,txtFifth;

    //below is for text suffix eg. kilometers meters on convert to box
    TextView txtFirstSuffix, txtSecondSuffix, txtThirdSuffix, txtFourthSuffix, txtFifthSuffix;

    LinearLayout llFirst, llSecond, llThird, llFourth, llFifth;

    RelativeLayout rlConvertTo;

    EditText etLength; //for getting input from user
    Button btnConvert;
    ImageView imgSetting;
    private String type="";
    ArrayAdapter adapter;
    Spinner spino;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    PopupMenu popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();  //it will hide the default action bar top

        //we will initialize our ui for calling below function
        initUI();


    }
    //below function change the spinner item list according to our setting.
    private void changeUnitType(String[] list) {
        adapter = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                list);
        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        spino.setAdapter(adapter);
    }

    void initUI(){

        //below is code for sharedpreference to get saved values.
        prefs =  getSharedPreferences("MySharedPRefs", MODE_PRIVATE);
        editor =prefs.edit();


        //we assign our xml ids to our variables so we can use later by variable
        txtFifth = findViewById(R.id.txtFifth);
        txtThird = findViewById(R.id.txtThird);
        txtSecond = findViewById(R.id.txtSecond);
        txtFirst = findViewById(R.id.txtFirst);
        txtFourth = findViewById(R.id.txtFourth);
        txtFifthSuffix = findViewById(R.id.txtFifthSuffix);
        txtThirdSuffix = findViewById(R.id.txtThirdSuffix);
        txtSecondSuffix = findViewById(R.id.txtSecondSuffix);
        txtFirstSuffix = findViewById(R.id.txtFirstSuffix);
        txtFourthSuffix = findViewById(R.id.txtFourthSuffix);
        llFifth = findViewById(R.id.llFifth);
        llThird = findViewById(R.id.llThird);
        llSecond = findViewById(R.id.llSecond);
        llFirst = findViewById(R.id.llFirst);
        llFourth = findViewById(R.id.llFourth);
        rlConvertTo = findViewById(R.id.rlConvertTo);
        etLength = findViewById(R.id.etLength);
        spino = findViewById(R.id.spinnerLength);
        imgSetting =findViewById(R.id.imgSetting);

        spino.setOnItemSelectedListener(this);// this is called when item selected from dropdown selction


        //below is popup menu for setting button click
        popup = new PopupMenu(HomeActivity.this, imgSetting);
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                editor.putString("settingType",item.getTitle().toString());
                editor.commit();
                item.setChecked(true);
                changeLayoutForSetting();
                return true;
            }
        });

        btnConvert = findViewById(R.id.btnConvert);//this is convert button

        //set listener forn click ol convert
        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if the input empty then it will show error otherwise it convert value according
                if(etLength.getText().toString().isEmpty()){
                    etLength.setText("Please Enter Value");
                }else {
                    //call our functioin for converting values
                    convertValues();
                }
            }
        });
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //to show popup menu
                popup.show();
            }
        });

        //this call change layout on create of activity and set the conversion type which saved in shaedpreferences.
        changeLayoutForSetting();

    }

    String settingType;
    //this call change layout on create of activity and set the conversion type which saved in shaedpreferences.
    private void changeLayoutForSetting() {
        //below we get value from our setting type where we saved
        settingType = prefs.getString("settingType", "Currency");//"No name defined" is the default value.

        //here we check the selected conversion type & assign values to its text view
        if(settingType.equals("Length")){
            popup.getMenu().getItem(0).setChecked(true);
            txtFirstSuffix.setText("Miles");
            txtSecondSuffix.setText("Kilometer");
            txtThirdSuffix.setText("Metere");
            txtFourthSuffix.setText("Inches");
            txtFifthSuffix.setText("Feet");
            changeUnitType(length);
        }else if(settingType.equals("Currency")){
            popup.getMenu().getItem(2).setChecked(true);
            txtFirstSuffix.setText("US Dollar");
            txtSecondSuffix.setText("Indian Rupee");
            txtThirdSuffix.setText("Euro");
            txtFourthSuffix.setText("Australian Dollar");
            txtFifthSuffix.setText("Canadian Dollar");
            changeUnitType(currency);
        }else if(settingType.equals("Temperature")){
            popup.getMenu().getItem(3).setChecked(true);
            txtFirstSuffix.setText("Fahrenheit");
            txtSecondSuffix.setText("Kelvin");
            txtThirdSuffix.setText("Degree");
            txtFourthSuffix.setText("Celcius");
            txtFifthSuffix.setText("");
            changeUnitType(temprature);
        }else if(settingType.equals("Weight")){
            popup.getMenu().getItem(1).setChecked(true);
            txtFirstSuffix.setText("Pound");
            txtSecondSuffix.setText("Kilogram");
            txtThirdSuffix.setText("Gram");
            txtFourthSuffix.setText("Ounches");
            txtFifthSuffix.setText("");
            changeUnitType(weight);
        }
    }

    //this function called to convert values of given unit.
    private void convertValues() {
        rlConvertTo.setVisibility(View.VISIBLE);
        llSecond.setVisibility(View.VISIBLE);
        llFirst.setVisibility(View.VISIBLE);
        llFifth.setVisibility(View.VISIBLE);
        llFourth.setVisibility(View.VISIBLE);
        llThird.setVisibility(View.VISIBLE);
        //below we show and hide necessary textview for better user experience
        switch (type){
            case "Miles":
            case "Pound":
            case "US Dollar":
            case "Fahrenheit": {
                llFirst.setVisibility(View.GONE);
                break;
            }
            case "Kilometer":
            case "Kilogram":
            case "Indian Rupee":
            case "Kelvin": {
                llSecond.setVisibility(View.GONE);
                break;
            }
            case "Meter":
            case "Gram":
            case "Euro":
            case "Degree": {
                llThird.setVisibility(View.GONE);
                break;
            }
            case "Inches":
            case "ounces":
            case "Australian Dollar":
            case "Celcius": {
                llFourth.setVisibility(View.GONE);
                break;
            }
            case "Feet":
            case "Canadian Dollar": {
                llFifth.setVisibility(View.GONE);
                break;
            }


        }
        ///below we check the type of setting and get the value according from unit
        if(settingType.equals("Length")){

            Double value = Double.valueOf(etLength.getText().toString());
            //type = length[i];
            Double meter = convertToMeter(value,type);
            Double kilometer = convertToKilometer(value,type);
            Double miles = convertToMiles(value,type);
            Double inches = convertToInches(value,type);
            Double feet = convertToFeet(value,type);

            //below we assign value to our textview which we converted
            txtFirst.setText(miles.toString());
            txtSecond.setText(kilometer.toString());
            txtThird.setText(meter.toString());
            txtFourth.setText(inches.toString());
            txtFifth.setText(feet.toString());

        }else if(settingType.equals("Weight")){
            llFifth.setVisibility(View.GONE);
            Double value = Double.valueOf(etLength.getText().toString());

            Double meter = convertToGram(value,type);
            Double kilometer = convertToKiloGram(value,type);
            Double miles = convertToPound(value,type);
            Double inches = convertToOunces(value,type);

            //below we assign value to our textview which we converted
            txtFirst.setText(miles.toString());
            txtSecond.setText(kilometer.toString());
            txtThird.setText(meter.toString());
            txtFourth.setText(inches.toString());

        }else if(settingType.equals("Currency")){
            Double value = Double.valueOf(etLength.getText().toString());
            Double meter = convertToUSD(value,type);
            Double kilometer = convertToINR(value,type);
            Double miles = convertToEURO(value,type);
            Double inches = convertToAusDollar(value,type);
            Double feet = convertToCanDollar(value,type);

            //below we assign value to our textview which we converted
            txtFirst.setText(meter.toString());
            txtSecond.setText(kilometer.toString());
            txtThird.setText(miles.toString());
            txtFourth.setText(inches.toString());
            txtFifth.setText(feet.toString());
        }else if(settingType.equals("Temperature")){
            llFifth.setVisibility(View.GONE);
            llThird.setVisibility(View.GONE);
            Double value = Double.valueOf(etLength.getText().toString());

            Double meter = convertToFahrenheit(value,type);
            Double kilometer = convertToKelvin(value,type);
            Double inches = convertToCelcius(value,type);

            //below we assign value to our textview which we converted
            txtFirst.setText(meter.toString());
            txtSecond.setText(kilometer.toString());
            txtFourth.setText(inches.toString());

        }

    }

    //this function is called when spinner optio item selected
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
        if(settingType.equals("Length")){
            type = length[i];
        }else if(settingType.equals("Currency")){
            type = currency[i];
        }else if(settingType.equals("Temperature")){
            type = temprature[i];
        }else if(settingType.equals("Weight")){
            type = weight[i];
        }
        //whem user select spinner item it will hide the second box.
        rlConvertTo.setVisibility(View.GONE);

    }

    //below is function for convert to meter of different unit
    private Double convertToMeter(Double value,String type) {
        if(type.equals("Meter")){
            return value;
        }else if(type.equals("Kilometer")){
            return value*1000;
        }else if(type.equals("Feet")){
            return value/3.281;
        }else if(type.equals("Inches")){
            return value/39.37;
        }else if(type.equals("Miles")){
            return value*1609.35;
        }
        return value;
    }
    //below is function for convert to Kilometer of different unit
    private Double convertToKilometer(Double value,String type) {
        if(type.equals("Meter")){
            return value/1000;
        }else if(type.equals("Kilometer")){
            return value;
        }else if(type.equals("Feet")){
            return value/3281;
        }else if(type.equals("Inches")){
            return value/39370;
        }else if(type.equals("Miles")){
            return value*1.609;
        }
        return value;
    }
    //below is function for convert to Miles of different unit
    private Double convertToMiles(Double value,String type) {
        if(type.equals("Meter")){
            return value/1609;
        }else if(type.equals("Kilometer")){
            return value/1.609;
        }else if(type.equals("Feet")){
            return value/5280;
        }else if(type.equals("Inches")){
            return value/63360;
        }else if(type.equals("Miles")){
            return value/1609;
        }
        return value;
    }
    //below is function for convert to Inches of different unit
    private Double convertToInches(Double value,String type) {
        if(type.equals("Meter")){
            return value*39.37;
        }else if(type.equals("Kilometer")){
            return value*39370;
        }else if(type.equals("Feet")){
            return value*12;
        }else if(type.equals("Inches")){
            return value;
        }else if(type.equals("Miles")){
            return value*63360;
        }
        return value;
    }
    //below is function for convert to Feet of different unit
    private Double convertToFeet(Double value,String type) {
        if(type.equals("Meter")){
            return value*3.281;
        }else if(type.equals("Kilometer")){
            return value*1000;
        }else if(type.equals("Feet")){
            return value;
        }else if(type.equals("Inches")){
            return value/12;
        }else if(type.equals("Miles")){
            return value*5280;
        }
        return value;
    }
    //below is function for convert to Gram of different unit
    private Double convertToGram(Double value,String type) {
        if(type.equals("Gram")){
            return value;
        }else if(type.equals("Kilogram")){
            return value*1000;
        }else if(type.equals("Pound")){
            return value*454;
        }else if(type.equals("ounces")){
            return value*28.35;
        }
        return value;
    }
    //below is function for convert to Kilogram of different unit
    private Double convertToKiloGram(Double value,String type) {
        if(type.equals("Gram")){
            return value/1000;
        }else if(type.equals("Kilogram")){
            return value;
        }else if(type.equals("Pound")){
            return value/2.205;
        }else if(type.equals("ounces")){
            return value/35.274;
        }
        return value;
    }
    //below is function for convert to Pound of different unit
    private Double convertToPound(Double value,String type) {
        if(type.equals("Gram")){
            return value/454;
        }else if(type.equals("Kilogram")){
            return value*2.205;
        }else if(type.equals("Pound")){
            return value;
        }else if(type.equals("ounces")){
            return value/16;
        }
        return value;
    }
    //below is function for convert to Ounces of different unit
    private Double convertToOunces(Double value,String type) {
        if(type.equals("Gram")){
            return value/28.35;
        }else if(type.equals("Kilogram")){
            return value*35.274;
        }else if(type.equals("Pound")){
            return value*16;
        }else if(type.equals("ounces")){
            return value;
        }
        return value;
    }
    //below is function for convert to US dollar of different unit
    private Double convertToUSD(Double value,String type) {
        if(type.equals("US Dollar")){
            return value;
        }else if(type.equals("Indian Rupee")){
            return value*0.013;
        }else if(type.equals("Euro")){
            return value*1.10;
        }else if(type.equals("Australian Dollar")){
            return value*0.75;
        }else if(type.equals("Canadian Dollar")){
            return value*0.80;
        }
        return value;
    }
    //below is function for convert to indian rupee of different unit
    private Double convertToINR(Double value,String type) {
        if(type.equals("US Dollar")){
            return value*76.28;
        }else if(type.equals("Indian Rupee")){
            return value;
        }else if(type.equals("Euro")){
            return value*83.84;
        }else if(type.equals("Australian Dollar")){
            return value*57.32;
        }else if(type.equals("Canadian Dollar")){
            return value*61.11;
        }
        return value;
    }
    //below is function for convert to euro of different unit
    private Double convertToEURO(Double value,String type) {
        if(type.equals("US Dollar")){
            return value*0.91;
        }else if(type.equals("Indian Rupee")){
            return value*0.012;
        }else if(type.equals("Euro")){
            return value;
        }else if(type.equals("Australian Dollar")){
            return value*0.68;
        }else if(type.equals("Canadian Dollar")){
            return value*0.73;
        }
        return value;
    }
    //below is function for convert to australian dollar of different unit
    private Double convertToAusDollar(Double value,String type) {
        if(type.equals("US Dollar")){
            return value*1.33;
        }else if(type.equals("Indian Rupee")){
            return value*0.017;
        }else if(type.equals("Euro")){
            return value*1.46;
        }else if(type.equals("Australian Dollar")){
            return value;
        }else if(type.equals("Canadian Dollar")){
            return value*1.07;
        }
        return value;
    }
    //below is function for convert to canadian dollar of different unit
    private Double convertToCanDollar(Double value,String type) {
        if(type.equals("US Dollar")){
            return value*1.25;
        }else if(type.equals("Indian Rupee")){
            return value*0.016;
        }else if(type.equals("Euro")){
            return value*1.37;
        }else if(type.equals("Australian Dollar")){
            return value*0.94;
        }else if(type.equals("Canadian Dollar")){
            return value;
        }
        return value;
    }
    //below is function for convert to fahreneheit of different unit
    private Double convertToFahrenheit(Double value,String type) {
        if(type.equals("Fahrenheit")){
            return value;
        }else if(type.equals("Kelvin")){
            return ((value - 273.15) * 9/5 + 32);
        }else if(type.equals("Celcius")){
            return ((value * 9/5) + 32);
        }
        return value;
    }
    //below is function for convert to kelvin of different unit
    private Double convertToKelvin(Double value,String type) {
        if(type.equals("Fahrenheit")){
            return ((value - 32) * 5/9 + 273.15);
        }else if(type.equals("Kelvin")){
            return value;
        }else if(type.equals("Celcius")){
            return value+273.15;
        }
        return value;
    }
    //below is function for convert to celcius of different unit
    private Double convertToCelcius(Double value,String type) {
        if(type.equals("Fahrenheit")){
            return ((value - 32) * 5/9);
        }else if(type.equals("Kelvin")){
            return value-273.15;
        }else if(type.equals("Celcius")){
            return value;
        }
        return value;
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}